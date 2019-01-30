package com.luciano.fobal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.luciano.fobal.utils.Constants;

public class ServerScreen extends FobalScreen
{
    private SocketIOServer serverSocket;

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
