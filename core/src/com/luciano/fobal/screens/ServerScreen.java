package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.Events;

import java.util.*;

public class ServerScreen extends ScreenAdapter
{
    private SocketIOServer serverSocket;

    private Map<String, Integer> players = new HashMap<>(2);
    private Queue<Integer> indexesAvailable = new LinkedList<>();

    public ServerScreen()
    {
    }

    @Override
    public void show()
    {
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
                    int index = players.remove(client.getSessionId().toString());
                    indexesAvailable.add(index);
                    Gdx.app.log("server", "freeing player number " + index);
                }
        });

        serverSocket.addEventListener(Events.ACTION.name(), String.class,
                (client, data, ackSender) -> {
                    Gdx.app.log("server", "action event with data: " + data);
                    serverSocket.getBroadcastOperations().sendEvent(Events.ACTION.name(),
                            client.getSessionId().toString(), data);
                });

        Gdx.app.log("server", "starting server");
        serverSocket.start();

        try
        {
            Thread.sleep(Integer.MAX_VALUE);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        serverSocket.stop();
    }
}
