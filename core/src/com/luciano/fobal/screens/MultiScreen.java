package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.luciano.fobal.FobalGame;
import com.luciano.fobal.levels.Level;
import com.luciano.fobal.Scenes.Hud;
import com.luciano.fobal.packets.ActionPacket;
import com.luciano.fobal.packets.GameStatePacket;
import com.luciano.fobal.packets.InputPacket;
import com.luciano.fobal.utils.*;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class MultiScreen extends ScreenAdapter
{
    private final SpriteBatch batch;
    private final FobalGame game;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Level level;
    private Hud hud;

    private AssetManager manager;
    private Music music;

    private Socket socket;
    private String serverIp;

    private int remotePlayer;

    private boolean isPaused = true;
    private int currentFrame = 0;
    private int lastFrameSended = 0;

    public MultiScreen(FobalGame fobalGame, SpriteBatch batch, String serverIp)
    {
        this.game = fobalGame;
        this.batch = batch;
        this.serverIp = serverIp;

        remotePlayer = 0;
    }

    @Override
    public void show()
    {
        try
        {
            socket = IO.socket(serverIp);
            socket.on(Socket.EVENT_CONNECT, args->{
                Gdx.app.log("socket", "connected with id: " + socket.id());
            });

            socket.on(Events.MY_PLAYER.name(), args -> {
                //el servidor envía qué player asigna a este cliente (índice del array)

                //puede tirar format exception
                //puede estar fuera de rango (0 o 1, porque son dos players)
    //                int index = Integer.valueOf((String)args[0]);
                int index = (int) args[0];
                if(0 <= index && index < 2)
                {
                    Gdx.app.log("socket", "I have player number " + index);
                    remotePlayer = (index + 1) % 2; //si soy 1, el es 0 y viceversa
                    level.players[remotePlayer].remote = true;
                }
            });

            socket.on(Events.ACTION.name(), args->{
                String id = (String) args[0];

                if(!id.equals(socket.id()))
                {
                    ActionPacket packet = new Json().fromJson(ActionPacket.class,
                            (String) args[1]);

                    level.players[remotePlayer].delayedAction = packet;
                }
            });

            socket.on(Events.BEGIN.name(), args -> {
//                BeginPacket packet = new Json().fromJson(BeginPacket.class, (String) args[0]);
                beginGame();
            });

            socket.on(Events.GAME_STATE.name(), args->{
                GameStatePacket packet = new Json().fromJson(GameStatePacket.class,
                        (String)args[0]);

                level.applyDelayedState(packet);
            });

            Gdx.app.log("socket", "connecting to server");
            socket.connect();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        hud = new Hud(batch);

        world = new World(Constants.GRAVITY, false);
        debugRenderer = new Box2DDebugRenderer();
        level = new Level(world, hud, GameMode.MULTI_PLAYER);

        world.setContactListener(new FobalContactListener());

        manager = new AssetManager();
        manager.load("audio/circus_theme.mp3", Music.class);
        manager.finishLoading();

        music = manager.get("audio/circus_theme.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.02f);
    }

    private void beginGame()
    {
        music.play();
        isPaused = false;
    }

    @Override
    public void render(float delta)
    {
        if(!isPaused)
        {
            world.step(1/60f, 6, 2);

            level.update(delta);

            if (level.currentInput != FobalInput.NONE && socket != null)
            {
                socket.emit(Events.INPUT.name(),
                        new Json().toJson(new InputPacket(currentFrame, level.currentInput)));
            }

            currentFrame++;
//            if((currentFrame - lastFrameSended) == 2)
//            {
//                lastFrameSended = currentFrame;
//
//            }

            hud.update(delta);

            Gdx.gl.glClearColor(
                    Constants.BACKGROUND_COLOR.r,
                    Constants.BACKGROUND_COLOR.g,
                    Constants.BACKGROUND_COLOR.b,
                    Constants.BACKGROUND_COLOR.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//            debugRenderer.render(world, level.viewport.getCamera().combined);

            level.render(batch);

            batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            if (music.isPlaying())
                music.pause();
            else
                music.play();
        }
    }

    @Override
    public void dispose()
    {
        manager.dispose();
        debugRenderer.dispose();
        world.dispose();
        hud.dispose();
        socket.disconnect();
    }

    @Override
    public void resize(int width, int height)
    {
        level.viewport.update(width, height, true);
        hud.viewport.update(width, height, true);
    }
}
