package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.luciano.fobal.levels.ServerLevel;
import com.luciano.fobal.packets.InputPacket;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.Events;
import com.luciano.fobal.utils.FobalContactListener;
import com.luciano.fobal.utils.FobalInput;

import java.util.*;

public class ServerScreen extends ScreenAdapter
{
    private SocketIOServer serverSocket;

    private Map<String, Integer> players = new HashMap<>(2);
    private Queue<Integer> indexesAvailable = new LinkedList<>();

    private World world;
    private ServerLevel level;

    private int currentFrame = 0;
    private int lastFrameSended = 0;

    private boolean pause = true;

    public ServerScreen()
    {
    }

    //el servidor es autoritario
    //recibe los comandos de los clientes y los aplica
    //envía el estado real del juego a 10Hz
    //ignora las entradas de teclado
    //no renderiza
    //

    //el cliente envía los comandos al servidor
    //los aplica localmente y corrije al recibir la posta
    //para los elementos ajenos aplica interpolacion
    //no cobrar gol localmente

    @Override
    public void show()
    {
        world = new World(Constants.GRAVITY, true);
        level = new ServerLevel(world);

        world.setContactListener(new FobalContactListener());

        indexesAvailable.add(0);
        indexesAvailable.add(1);

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
                        level.inputs[index] = packet.getInput();
                    }
                }));

//        serverSocket.addEventListener(Events.ACTION.name(), String.class,
//                (client, data, ackSender) -> {
//                    Gdx.app.log("server", "action event with data: " + data);
//                    serverSocket.getBroadcastOperations().sendEvent(Events.ACTION.name(),
//                            client.getSessionId().toString(), data);
//                });

        Gdx.app.log("server", "starting server");
        serverSocket.start();

//        try
//        {
//            Thread.sleep(Integer.MAX_VALUE);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        serverSocket.stop();
    }

    @Override
    public void render(float delta)
    {
        if(!pause)
        {
            world.step(1 / 60f, 6, 2);

            level.update(delta);

            currentFrame++;

            if((currentFrame - lastFrameSended) == 6)
            {
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
