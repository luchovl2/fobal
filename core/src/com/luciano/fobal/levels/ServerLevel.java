package com.luciano.fobal.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.luciano.fobal.entities.Arco;
import com.luciano.fobal.entities.Jugador;
import com.luciano.fobal.entities.Pared;
import com.luciano.fobal.entities.Pelota;
import com.luciano.fobal.packets.GameStatePacket;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.FobalInput;

import static com.luciano.fobal.utils.Constants.PPM;

public class ServerLevel
{
    private Jugador[] players = new Jugador[2];

    private Arco arcoDer;
    private Arco arcoIzq;
    private Pelota pelota;

    private Array<Pared> contorno;
    private World world;

    private int score1, score2;
    private boolean gameOver;
    private boolean pause;
    private int gameTime;
    private float timeCounter;

    public FobalInput[] inputs = new FobalInput[2];

    public ServerLevel(World world)
    {
        this.world = world;

        pause = false;
        score1 = 0;
        score2 = 0;
        gameOver = false;
        gameTime = Constants.TIEMPO_JUEGO;
        timeCounter = 0;

        players[0] = new Jugador(world, Constants.JUGADOR_SPAWN, true);
        players[1] = new Jugador(world, Constants.JUGADOR_SPAWN_2, false);

        pelota = new Pelota(world, Constants.PELOTA_SPAWN);

        arcoDer = new Arco(world, true);
        arcoIzq = new Arco(world, false);

        inputs[0] = FobalInput.NONE;
        inputs[1] = FobalInput.NONE;

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

            timeCounter = 0;
        }

        if (arcoDer.hayPelota())
        {
            arcoDer.pelotaAfuera();
            gol(true);
        }
        if (arcoIzq.hayPelota())
        {
            arcoIzq.pelotaAfuera();
            gol(false);
        }

        handleInput();

        for(Jugador player: players)
            player.update(delta);

        pelota.update(delta);
        arcoIzq.update(delta);
        arcoDer.update(delta);
    }

    private void handleInput()
    {
        for(int i=0; i<2; i++)
        {
            switch(inputs[i])
            {
                case LEFT:
                    players[i].moveLeft();
                    break;
                case RIGHT:
                    players[i].moveRight();
                    break;
                case UP:
                    players[i].jump();
                    break;
                case DOWN:
                    break;
                case KICK:
                    players[i].kick();
                    break;
                default:
                    break;
            }
            inputs[i] = FobalInput.NONE;
        }
    }

    public GameStatePacket getState()
    {
        return new GameStatePacket(
                players[0].body.getPosition(),
                players[0].body.getLinearVelocity(),
                players[0].foot.getPosition(),
                players[0].foot.getAngle(),
                players[1].body.getPosition(),
                players[1].body.getLinearVelocity(),
                players[1].foot.getPosition(),
                players[1].foot.getAngle(),
                pelota.body.getPosition(),
                pelota.body.getLinearVelocity(),
                pelota.body.getAngularVelocity(),
                score1,
                score2);
    }

    private void gol(boolean arcoDerecho)
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

    public void applyState(GameStatePacket newState)
    {
        players[0].body.setTransform(newState.getP1Pos(), 0);
        players[0].body.setLinearVelocity(newState.getP1Vel());
        players[0].foot.setTransform(newState.getP1FootPos(), newState.getP1FootAng());

        players[1].body.setTransform(newState.getP2Pos(), 0);
        players[1].body.setLinearVelocity(newState.getP2Vel());
        players[1].foot.setTransform(newState.getP2FootPos(), newState.getP2FootAng());

        pelota.body.setTransform(newState.getBallPos(), pelota.body.getAngle());
        pelota.body.setLinearVelocity(newState.getBallVel());
        pelota.body.setAngularVelocity(newState.getBallAngVel());

        score1 = newState.getScore1();
        score2 = newState.getScore2();
    }
}
