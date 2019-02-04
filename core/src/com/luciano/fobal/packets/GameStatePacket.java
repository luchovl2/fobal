package com.luciano.fobal.packets;

import com.badlogic.gdx.math.Vector2;

public class GameStatePacket
{
    private Vector2 p1Pos;
    private Vector2 p1Vel;
    private Vector2 p2Pos;
    private Vector2 p2Vel;
    private Vector2 ballPos;
    private Vector2 ballVel;
    private float ballAngVel;

    public GameStatePacket()
    {
    }

    public GameStatePacket(Vector2 p1Pos, Vector2 p1Vel, Vector2 p2Pos, Vector2 p2Vel, Vector2 ballPos, Vector2 ballVel, float ballAngVel)
    {
        this.p1Pos = p1Pos;
        this.p1Vel = p1Vel;
        this.p2Pos = p2Pos;
        this.p2Vel = p2Vel;
        this.ballPos = ballPos;
        this.ballVel = ballVel;
        this.ballAngVel = ballAngVel;
    }

    public float getBallAngVel()
    {
        return ballAngVel;
    }

    public void setBallAngVel(float ballAngVel)
    {
        this.ballAngVel = ballAngVel;
    }

    public Vector2 getP1Pos()
    {
        return p1Pos;
    }

    public void setP1Pos(Vector2 p1Pos)
    {
        this.p1Pos = p1Pos;
    }

    public Vector2 getP1Vel()
    {
        return p1Vel;
    }

    public void setP1Vel(Vector2 p1Vel)
    {
        this.p1Vel = p1Vel;
    }

    public Vector2 getP2Pos()
    {
        return p2Pos;
    }

    public void setP2Pos(Vector2 p2Pos)
    {
        this.p2Pos = p2Pos;
    }

    public Vector2 getP2Vel()
    {
        return p2Vel;
    }

    public void setP2Vel(Vector2 p2Vel)
    {
        this.p2Vel = p2Vel;
    }

    public Vector2 getBallPos()
    {
        return ballPos;
    }

    public void setBallPos(Vector2 ballPos)
    {
        this.ballPos = ballPos;
    }

    public Vector2 getBallVel()
    {
        return ballVel;
    }

    public void setBallVel(Vector2 ballVel)
    {
        this.ballVel = ballVel;
    }
}
