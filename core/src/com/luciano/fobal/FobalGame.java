package com.luciano.fobal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.luciano.fobal.screens.*;

import java.util.Optional;

public class FobalGame extends Game
{
    private ScreenAdapter screen;
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 200;

    private boolean isServer = false;

    private SpriteBatch batch;

    @Override
    public void create()
    {
        batch = new SpriteBatch();

        Optional.ofNullable(System.getProperty("server"))
                .ifPresent(value -> isServer = value.equals("true"));

        if(isServer)
        {
            screen = new ServerScreen();
            setScreen(screen);
        }
        else
        {
//            screen = new FobalScreen(this, batch);
            screen = new IntroScreen(this, batch);
            setScreen(screen);
        }
    }

    public void setSinglePlayerScreen()
    {
        screen.dispose();
        screen = new FobalScreen(this, batch);
        setScreen(screen);
    }

    public void setMultiPlayerScreen(String serverIp)
    {
        screen.dispose();
        screen = new MultiScreen(this, batch, serverIp);
        setScreen(screen);
    }

    public void setConfigScreen()
    {
        screen.dispose();
        screen = new ConfigScreen(this, batch);
        setScreen(screen);
    }

    @Override
    public void dispose()
    {
        screen.dispose();
        batch.dispose();
        super.dispose();
    }
}
