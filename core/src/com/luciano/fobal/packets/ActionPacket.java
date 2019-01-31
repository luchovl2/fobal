package com.luciano.fobal.packets;

import com.badlogic.gdx.math.Vector2;

public class ActionPacket
{
    public enum Action
    {
        KICK,
        JUMP,
        LEFT,
        RIGHT,
        SPAWN,
        NONE
    }

    private Vector2 playerPos;
    private Vector2 playerVel;
    private float footAngPos;
    private float footAngVel;
    private Action action;

    public ActionPacket()
    {
    }

    public ActionPacket(Vector2 playerPos, Vector2 playerVel, float footAngPos, float footAngVel, Action action)
    {
        this.playerPos = playerPos;
        this.playerVel = playerVel;
        this.footAngPos = footAngPos;
        this.footAngVel = footAngVel;
        this.action = action;
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Vector2 getPlayerPos()
    {
        return playerPos;
    }

    public void setPlayerPos(Vector2 playerPos)
    {
        this.playerPos = playerPos;
    }

    public Vector2 getPlayerVel()
    {
        return playerVel;
    }

    public void setPlayerVel(Vector2 playerVel)
    {
        this.playerVel = playerVel;
    }

    public float getFootAngPos()
    {
        return footAngPos;
    }

    public void setFootAngPos(float footAngPos)
    {
        this.footAngPos = footAngPos;
    }

    public float getFootAngVel()
    {
        return footAngVel;
    }

    public void setFootAngVel(float footAngVel)
    {
        this.footAngVel = footAngVel;
    }

    @Override
    public String toString()
    {
        return "ActionPacket{" +
                "playerPos=" + playerPos +
                ", playerVel=" + playerVel +
                ", footAngPos=" + footAngPos +
                ", footAngVel=" + footAngVel +
                '}';
    }
}
