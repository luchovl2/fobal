package com.luciano.fobal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.luciano.fobal.Scenes.Hud;
import com.luciano.fobal.entities.Arco;
import com.luciano.fobal.entities.Jugador;
import com.luciano.fobal.entities.Pared;
import com.luciano.fobal.entities.Pelota;
import com.luciano.fobal.packets.ActionPacket;
import com.luciano.fobal.utils.AIRival;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.Events;
import com.luciano.fobal.utils.GameMode;
import io.socket.client.Socket;

import java.util.Optional;

import static com.luciano.fobal.utils.Constants.PPM;

public class Level
{
    private Socket socket;


    public Viewport viewport;
    private Hud hud;

    public Jugador[] players = new Jugador[2];

    private Arco arcoDer;
    private Arco arcoIzq;
    public Pelota pelota;

    private Array<Pared> contorno;
    private World world;

    private int score1, score2;
    private boolean gameOver;
    private boolean pause;
    private int gameTime;
    private float timeCounter;

    private Sprite background;

    private AIRival aiRival;

    public Level(World world, Hud hud, GameMode gameMode)
    {
        this.world = world;
        this.hud = hud;

        pause = false;
        score1 = 0;
        score2 = 0;
        gameOver = false;
        gameTime = Constants.TIEMPO_JUEGO;
        timeCounter = 0;

        background = new Sprite(new Texture(Constants.BACKGROUND_TEXTURE));
        background.setSize(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);
        background.setOriginCenter();

        viewport = new FitViewport(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);

        players[0] = new Jugador(world, Constants.JUGADOR_SPAWN, true);
        players[1] = new Jugador(world, Constants.JUGADOR_SPAWN_2, false);

        pelota = new Pelota(world, Constants.PELOTA_SPAWN);

        if(gameMode == GameMode.SINGLE_PLAYER)
            aiRival = new AIRival(this);

        arcoDer = new Arco(world, true, this);
        arcoIzq = new Arco(world, false, this);

        contorno = new Array<>(4);
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

    public Level(World world, Hud hud, GameMode gameMode, Socket socket)
    {
        this(world, hud, gameMode);
        this.socket = socket;
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

        handleInput();

        Optional.ofNullable(aiRival)
                .ifPresent(aiPlayer->aiPlayer.update(delta));

        for(Jugador player: players)
            player.update(delta);

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

    private void handleInput()
    {
        for(Jugador player: players)
        {
            if(!player.remote)
            {
                ActionPacket packet = new ActionPacket(
                        player.body.getPosition(),
                        player.body.getLinearVelocity(),
                        player.foot.getAngle(),
                        player.foot.getAngularVelocity(),
                        null);

                if (Gdx.input.isKeyPressed(player.left))
                {
                    if (socket != null)
                    {
                        packet.setAction(ActionPacket.Action.LEFT);
                        socket.emit(Events.ACTION.name(), new Json().toJson(packet));
                    }
                    player.moveLeft();
                }
                if (Gdx.input.isKeyPressed(player.right))
                {
                    if (socket != null)
                    {
                        packet.setAction(ActionPacket.Action.RIGHT);
                        socket.emit(Events.ACTION.name(), new Json().toJson(packet));
                    }
                    player.moveRight();
                }
                if (Gdx.input.isKeyJustPressed(player.up))
                {
                    if (socket != null)
                    {
                        packet.setAction(ActionPacket.Action.JUMP);
                        socket.emit(Events.ACTION.name(), new Json().toJson(packet));
                    }
                    player.jump();
                }
                if (Gdx.input.isKeyJustPressed(player.kick))
                {
                    if (socket != null)
                    {
                        packet.setAction(ActionPacket.Action.KICK);
                        socket.emit(Events.ACTION.name(), new Json().toJson(packet));
                    }
                    player.kick();
                }
            }
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
        for(Jugador player: players)
            player.respawn();
        pelota.respawn();
    }

    public void render(SpriteBatch batch)
    {
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        background.draw(batch);

        for(Jugador player:players)
            player.render(batch);

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
