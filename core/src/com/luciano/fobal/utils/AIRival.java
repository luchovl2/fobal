package com.luciano.fobal.utils;

import com.luciano.fobal.Level;
import com.luciano.fobal.entities.Jugador;
import com.luciano.fobal.entities.Pelota;

public class AIRival
{
    private Level level;
    private Jugador player;
    private Jugador rival;
    private Pelota pelota;

    public enum State{REMATAR, BUSCAR, ATAJAR};
    private State currentState;

    public AIRival(Level level)
    {
        this.level = level;

        player = level.player1;
        rival = level.player2;
        pelota = level.pelota;

        currentState = State.BUSCAR;
    }

    public void update(float delta)
    {
        if(isBallInShootRange())
        {
           player.patear();
        }
        else if(isBallInHeadRange())
        {
            player.cabecear();
        }
        else if(isBallCloserToMe() && isBallInFront())
        {
            player.avanzar();
        }
        else if(!underTheGoal())
        {
            player.retroceder();
        }
    }

    private boolean isBallInFront()
    {
        return player.body.getPosition().x < pelota.body.getPosition().x;
    }

    private boolean underTheGoal()
    {
        if(player.body.getPosition().x <= Constants.ARCO_IZQUIERDO_POS.x+Constants.ARCO_WIDTH)
        {
            return true;
        }
        return false;
    }

    private boolean isBallCloserToMe()
    {
        if(player.body.getPosition().dst(pelota.body.getPosition()) <
                rival.body.getPosition().dst(pelota.body.getPosition()))
        {
            return true;
        }

        return false;
    }

    private boolean isBallInShootRange()
    {
        if(player.body.getPosition().dst(pelota.body.getPosition()) < 0.46f)
        {
            return true;
        }
        return false;
    }

    private boolean isBallInHeadRange()
    {
        if(player.body.getPosition().dst(pelota.body.getPosition()) < 1.0f)
        {
            if(pelota.body.getPosition().y > 0.8f)
                return true;
        }
        return false;
    }
}
