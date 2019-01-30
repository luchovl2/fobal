package com.luciano.fobal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Optional;

public class FobalGame extends Game
{
    public static FobalScreen screen;
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 200;

    private boolean isServer = false;

    @Override
    public void create()
    {
        Optional.ofNullable(System.getProperty("server"))
                .ifPresent(value -> isServer = value.equals("true"));

        if(isServer)
        {
            screen = new ServerScreen();
            setScreen(screen);
        }
        else
        {
            screen = new FobalScreen();
            setScreen(screen);
        }

    }
}
