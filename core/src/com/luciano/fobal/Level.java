package com.luciano.fobal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.luciano.fobal.Scenes.Hud;
import com.luciano.fobal.entities.Arco;
import com.luciano.fobal.entities.Jugador;
import com.luciano.fobal.entities.Pared;
import com.luciano.fobal.entities.Pelota;
import com.luciano.fobal.utils.AIRival;
import com.luciano.fobal.utils.Constants;

import static com.luciano.fobal.utils.Constants.PPM;

public class Level
{
    public int score1, score2;
    public Viewport viewport;
    public boolean gameOver;
    private Hud hud;
    public Jugador player1;
    public Jugador player2;
    private Arco arcoDer;
    private Arco arcoIzq;
    public Pelota pelota;
    private Array<Pared> contorno;
    private World world;
    private boolean pause;
    private int gameTime;
    private float timeCounter;
    private Sprite background;
    private AIRival aiRival;

    public Level(World world, Hud hud)
    {
        this.world = world;
        this.hud = hud;

        pause = false;
        score1 = 0;
        score2 = 0;
        gameOver = false;
        gameTime = Constants.TIEMPO_JUEGO;
        timeCounter = 0;

        background = new Sprite(new Texture(Constants.BACKGROUN_TEXTURE));
        background.setSize(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);
        background.setOriginCenter();

        viewport = new FitViewport(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);

        player1 = new Jugador(world, Constants.JUGADOR_SPAWN, true);
        player2 = new Jugador(world, Constants.JUGADOR_SPAWN_2, false);

        pelota = new Pelota(world, Constants.PELOTA_SPAWN);

        aiRival = new AIRival(this);

        arcoDer = new Arco(world, true, this);
        arcoIzq = new Arco(world, false, this);

        contorno = new Array<Pared>(4);
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        contorno.add(new Pared(world,
                new Vector2(width/2/PPM, 0),
                width/PPM,
                Constants.PARED_GROSOR));
        contorno.add(new Pared(world,
                new Vector2(width/PPM, height/2/PPM),
                Constants.PARED_GROSOR,
                height/PPM));
        contorno.add(new Pared(world,
                new Vector2(width/2/PPM, height/PPM),
                width/PPM,
                Constants.PARED_GROSOR));
        contorno.add(new Pared(world,
                new Vector2(0, height/2/PPM),
                Constants.PARED_GROSOR,
                height/PPM));
    }

    public void update(float delta)
    {
        timeCounter += delta;

        if(timeCounter >= 1f)
        {
            if(--gameTime < 0)
            {
                gameTime = Constants.TIEMPO_JUEGO;
                score1 = 0;
                score2 = 0;
            }

            hud.setTime(gameTime);
            timeCounter = 0;
        }

        aiRival.update(delta);
        player1.update(delta);
        player2.update(delta);
        pelota.update(delta);
        arcoIzq.update(delta);
        arcoDer.update(delta);

        hud.setScore1(score1);
        hud.setScore2(score2);

        if(Gdx.input.isKeyJustPressed(Input.Keys.P))
        {
            pause = !pause;
        }
    }

    public void gol(boolean arcoDerecho)
    {
        if(arcoDerecho)
            score1++;
        else
            score2++;

        sacarDelMedio();
    }

    private void sacarDelMedio()
    {
        player1.respawn();
        player2.respawn();
        pelota.respawn();
    }

    public void render(SpriteBatch batch)
    {
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        background.draw(batch);
        player1.render(batch);
        player2.render(batch);
        pelota.render(batch);
        arcoIzq.render(batch);
        arcoDer.render(batch);

        for(Pared pared:contorno)
        {
            pared.render(batch);
        }

        batch.end();
    }
}
