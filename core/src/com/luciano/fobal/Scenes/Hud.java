package com.luciano.fobal.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.luciano.fobal.FobalGame;
import com.luciano.fobal.utils.Constants;

import java.util.Locale;

public class Hud implements Disposable
{
    public Viewport viewport;
    public Stage stage;

    private int score1;
    private int score2;

    Label scoreLabel1;
    Label scoreLabel2;

    private int time;
    Label timeLabel;

    public Hud(SpriteBatch batch)
    {
        score1 = 0;
        score2 = 0;

        viewport = new FitViewport(FobalGame.V_WIDTH, FobalGame.V_HEIGHT,
                new OrthographicCamera());

        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel1 = new Label(String.format(Locale.getDefault(), "%d", score1),
                new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        scoreLabel2 = new Label(String.format(Locale.getDefault(), "%d", score2),
                new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        timeLabel = new Label(String.format(Locale.getDefault(),
                "%02d:%02d", time/60, time%60),
                new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        table.add(scoreLabel1).expandX();
        table.add(timeLabel).expandX();
        table.add(scoreLabel2).expandX();

        stage.addActor(table);
    }

    public void update(float delta)
    {
        timeLabel.setText(String.format(Locale.getDefault(),
                "%02d:%02d", time/60, time%60));
        scoreLabel1.setText(String.valueOf(score1));
        scoreLabel2.setText(String.valueOf(score2));
    }

    public void setScore1(int currentScore)
    {
        score1 = currentScore;
    }

    public void setScore2(int currentScore)
    {
        score2 = currentScore;
    }

    public void setTime(int currentTime)
    {
        time = currentTime;
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
