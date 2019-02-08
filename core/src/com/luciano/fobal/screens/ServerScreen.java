package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.luciano.fobal.levels.ServerLevel;
import com.luciano.fobal.packets.GameStatePacket;
import com.luciano.fobal.packets.InputPacket;
import com.luciano.fobal.utils.*;

import java.util.*;

public class ServerScreen extends ScreenAdapter
{
    private SocketIOServer serverSocket;

    private Map<String, Integer> players = new HashMap<>(2);
    private Queue<Integer> indexesAvailable = new LinkedList<>();

    private final int OLD_STATES_SIZE = 4;
    private Map<Integer, GameStatePacket> oldStates = new HashMap<>(OLD_STATES_SIZE);
    private int oldestState = 0;

    private World world;
    private ServerLevel level;

    private ServerMode serverMode;

    private int currentFrame = 0;
    private int lastFrameSended = 0;
    private final int FRAME_DISTANCE = 4;

    private boolean pause = true;
    private boolean rewind = false;
    private int rewindToFrame;
    private FobalInput[] pastInputs = new FobalInput[2];
    private List<FobalInput[]> listInputs = new ArrayList<>(6);

    private Map<Integer, FobalInput[]> inputMap = new HashMap<>(OLD_STATES_SIZE);

    public ServerScreen()
    {
    }

    @Override
    public void show()
    {
        serverMode = ServerMode.NAIVE;
//        serverMode = ServerMode.RETROACTIVE;

        world = new World(Constants.GRAVITY, true);
        level = new ServerLevel(world);

        world.setContactListener(new FobalContactListener());

        indexesAvailable.add(0);
        indexesAvailable.add(1);

        pastInputs[0] = FobalInput.NONE;
        pastInputs[1] = FobalInput.NONE;

        Configuration config = new Configuration();
        config.setPort(Constants.PORT);
        config.setHostname(Constants.HOST);

        serverSocket = new SocketIOServer(config);

        serverSocket.addConnectListener(client -> {
                Gdx.app.log("server", "client connected with id: " + client.getSessionId());

                if(indexesAvailable.peek() != null)
                {
                    int index = indexesAvailable.poll();
                    players.put(client.getSessionId().toString(), index);
                    Gdx.app.log("server", "assigning player number " + index);
                    client.sendEvent(Events.MY_PLAYER.name(), index);

                    if(indexesAvailable.isEmpty()) //si está vacía hay dos players
                    {
                        pause = false; //comienza el juego

                        serverSocket.getBroadcastOperations()
                                .sendEvent(Events.BEGIN.name(), "");
                    }
                }
                else
                {
                    Gdx.app.log("server", "no player number available right now");
                    client.disconnect();
                }

        });
        serverSocket.addDisconnectListener(client -> {
                Gdx.app.log("server", "client disconnected with id: " + client.getSessionId());

                if(players.containsKey(client.getSessionId().toString()))
                {
                    pause = true;

                    int index = players.remove(client.getSessionId().toString());
                    indexesAvailable.add(index);
                    Gdx.app.log("server", "freeing player number " + index);
                }
        });

        serverSocket.addEventListener(Events.INPUT.name(), String.class,
                ((client, data, ackSender) -> {
                    if(players.containsKey(client.getSessionId().toString()))
                    {
                        int index = players.get(client.getSessionId().toString());
                        InputPacket packet = new Json().fromJson(InputPacket.class, data);

//                        rewind = true;
//                        rewindToFrame = packet.getFrame();
//                        pastInputs[index] = packet.getInput();

                        pastInputs[index] = packet.getInput();

                        if(serverMode == ServerMode.RETROACTIVE)
                        {
                            inputMap.put(packet.getFrame(), pastInputs.clone());
                        }
                    }
                }));

        Gdx.app.log("server", "starting server");
        serverSocket.start();
    }

    private void rewindAndCatchUp(float delta)
    {
        //preguntar si se tiene el estado al que hay que ir
        //si no ir al más viejo que haya
        if(rewindToFrame < oldestState)
        {
            rewindToFrame = oldestState;
        }
        Gdx.app.log("server", "curr: " + currentFrame + "; rewind: " + rewindToFrame);

        //rewind
        level.applyState(oldStates.get(rewindToFrame));
        level.inputs = pastInputs.clone();
        pastInputs[0] = FobalInput.NONE;
        pastInputs[1] = FobalInput.NONE;

        //catch up
        for (int i = 0; i < (currentFrame - rewindToFrame); i++)
        {
            world.step(1/60f, 6, 2);
            level.update(delta);

            //actualizar el mapa de estados viejos
            oldStates.replace(rewindToFrame+i, level.getState());
        }

        //ojo que si llega otra input para el mismo frame viejo se pierde la
        //primera input recibida
    }

    private void catchUp()
    {
        //avanzar los pasos aplicando las entradas correspondientes
        for(int i=0; i<(6); i++)
        {
            world.step(1/60f, 6, 2);
            level.inputs = listInputs.get(i);
            level.update(1/60f);
        }
        listInputs.clear();
    }

    private void resimulate()
    {
        //retrotraer al estado más viejo registrado y resimular desde ahí con
        //las entradas recibidas

        int contador = oldestState;

        if(oldStates.containsKey(oldestState))
        {
            level.applyState(oldStates.get(oldestState));
        }

        for(int i=0; i<oldStates.size(); i++)
        {
            for(int j=0; j<FRAME_DISTANCE; j++)
            {
                contador++;

                world.step(1/60f, 6, 2);

                if(inputMap.containsKey(oldestState + i*FRAME_DISTANCE + j))
                    level.inputs = inputMap.remove(oldestState + i*FRAME_DISTANCE + j);

                level.update(1/60f);
            }
            //actualizar si existe
            oldStates.replace(oldestState + i*FRAME_DISTANCE, level.getState());
        }
    }

    @Override
    public void render(float delta)
    {
        if(!pause)
        {
            if(serverMode == ServerMode.NAIVE)
            {
                //aplicar el step
                world.step(1 / 60f, 6, 2);
                level.inputs = pastInputs.clone();
                level.update(delta);
            }

            //enlistar las entradas más recientes recibidas
            listInputs.add(pastInputs.clone());
            pastInputs[0] = FobalInput.NONE;
            pastInputs[1] = FobalInput.NONE;

            currentFrame++;

            //agregar nuevo estado al historial
            //actualizar "oldestState" si corresponde
//            oldStates.put(currentFrame, level.getState());
//
//            if (oldStates.size() >= OLD_STATES_SIZE) //cuando se llena quitar el más viejo
//            {
//                oldStates.remove(oldestState);
//                oldestState++;
//            }

            //enviar si corresponde
            if((currentFrame - lastFrameSended) == FRAME_DISTANCE)
            {
                if(serverMode == ServerMode.RETROACTIVE)
                {
                    //antes de enviar el próximo estado
                    //iterar desde el estado más viejo guardado
                    //aplicando las entradas que se hayan registrado hasta ahora
                    //hay que actualizar los estados guardados

                    resimulate();

                    oldStates.put(currentFrame, level.getState());

                    if (oldStates.size() >= OLD_STATES_SIZE) //cuando se llena quitar el más viejo
                    {
                        oldStates.remove(oldestState);
                        oldestState += FRAME_DISTANCE;
                    }
                }

                lastFrameSended = currentFrame;

                serverSocket.getBroadcastOperations()
                        .sendEvent(Events.GAME_STATE.name(),
                                new Json().toJson(level.getState()));
            }
        }
    }

    @Override
    public void dispose()
    {
        world.dispose();
        serverSocket.stop();
    }
}
