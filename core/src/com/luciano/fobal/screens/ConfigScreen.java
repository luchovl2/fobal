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
    private final Label messageLabel;

    public ConfigScreen(FobalGame game, SpriteBatch batch)
    {
        this.game = game;
        this.batch = batch;

        final Skin skin = new Skin(Gdx.files.internal("skins/craftacular/craftacular-ui.json"));

        stage = new Stage();
        final Table table = new Table();
//        table.setDebug(true);
        table.setFillParent(true);
        table.top();

        Label titleLable = new Label("Fobal", skin, "title", Color.FIREBRICK);
        table.add(titleLable).expandX().padTop(20).colspan(2);
        table.row();

        Label serverIpLabel = new Label("IP y puerto de servidor:", skin);
        table.add(serverIpLabel).uniform().right().padTop(60);

        TextField serverIp = new TextField(Constants.HOST+":"+Constants.PORT, skin);
        serverIp.setMaxLength(28);
        table.add(serverIp).uniform().fillX().padTop(60).padRight(10).spaceLeft(10);
        table.row();

        multiButton = new TextButton("Let's play", skin);
        table.add(multiButton).padTop(60).colspan(2).padTop(50);

        messageLabel = new Label("waiting for player...", skin);
        messageLabel.setVisible(false);
        table.row();
        table.add(messageLabel).colspan(2);

        stage.addActor(table);

        multiButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                messageLabel.setVisible(true);
                render(0);
                game.setMultiPlayerScreen("http://"+serverIp.getText());
            }
        });

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
