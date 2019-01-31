package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.Events;

public class ServerScreen extends ScreenAdapter
{
    private SocketIOServer serverSocket;

    public ServerScreen()
    {
    }

    @Override
    public void show()
    {
        Configuration config = new Configuration();
        config.setPort(Constants.PORT);
        config.setHostname(Constants.HOST);

        serverSocket = new SocketIOServer(config);

        serverSocket.addConnectListener(client -> {
                Gdx.app.log("server", "client connected with id: " + client.getSessionId());
        });
        serverSocket.addDisconnectListener(client -> {
                Gdx.app.log("server", "client disconnected with id: " + client.getSessionId());
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
