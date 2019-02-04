package com.luciano.fobal.utils;

import com.luciano.fobal.levels.Level;
import com.luciano.fobal.entities.Jugador;
import com.luciano.fobal.entities.Pelota;

public class AIRival
{
    private Level level;
    private Jugador aiPlayer;
    private Jugador rival;
    private Pelota pelota;

    public enum State{REMATAR, BUSCAR, ATAJAR};
    private State currentState;

    public AIRival(Level level)
    {
        this.level = level;

        aiPlayer = level.players[0];
        rival = level.players[1];
        pelota = level.pelota;

        currentState = State.BUSCAR;
    }

    public void update(float delta)
    {
        if(isBallInShootRange())
        {
           aiPlayer.kick();
        }
        else if(isBallInHeadRange())
        {
            aiPlayer.cabecear();
        }
        else if(isBallCloserToMe() && isBallInFront())
        {
            aiPlayer.avanzar();
        }
        else if(!underTheGoal())
        {
            aiPlayer.retroceder();
        }
    }

    private boolean isBallInFront()
    {
        return aiPlayer.body.getPosition().x < pelota.body.getPosition().x;
    }

    private boolean underTheGoal()
    {
        if(aiPlayer.body.getPosition().x <= Constants.ARCO_IZQUIERDO_POS.x+Constants.ARCO_WIDTH)
        {
            return true;
        }
        return false;
    }

    private boolean isBallCloserToMe()
    {
        if(aiPlayer.body.getPosition().dst(pelota.body.getPosition()) <
                rival.body.getPosition().dst(pelota.body.getPosition()))
        {
            return true;
        }

        return false;
    }

    private boolean isBallInShootRange()
    {
        if(aiPlayer.body.getPosition().dst(pelota.body.getPosition()) < 0.46f)
        {
            return true;
        }
        return false;
    }

    private boolean isBallInHeadRange()
    {
        if(aiPlayer.body.getPosition().dst(pelota.body.getPosition()) < 1.0f)
        {
            if(pelota.body.getPosition().y > 0.8f)
                return true;
        }
        return false;
    }
}
