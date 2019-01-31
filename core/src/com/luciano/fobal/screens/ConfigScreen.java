package com.luciano.fobal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.luciano.fobal.FobalGame;
import com.luciano.fobal.utils.Constants;

public class ConfigScreen extends ScreenAdapter
{
    private final SpriteBatch batch;
    private final FobalGame game;

    private final Stage stage;

    public TextButton multiButton;

    public ConfigScreen(FobalGame game, SpriteBatch batch)
    {
        this.game = game;
        this.batch = batch;

        final Skin skin = new Skin(Gdx.files.internal("skins/craftacular/craftacular-ui.json"));

        stage = new Stage();
        final Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        table.top();

        Label titleLable = new Label("Fobal", skin, "title", Color.FIREBRICK);
        table.add(titleLable).expandX().padTop(20);
        table.row();

        Label serverIpLabel = new Label("Ingrese IP de servidor", skin);
        table.add(serverIpLabel).expandX().padTop(20);

        TextField serverIp = new TextField("", skin);
        serverIp.setMaxLength(32);
        table.add(serverIp).expandX().padTop(20);
        table.row();

        multiButton = new TextButton("Multiplayer", skin);
        multiButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setMultiPlayerScreen();
            }
        });
        table.add(multiButton).padTop(20);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        super.dispose();
    }
}
