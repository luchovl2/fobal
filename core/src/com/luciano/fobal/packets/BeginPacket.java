package com.luciano.fobal.packets;

import com.badlogic.gdx.math.Vector2;

public class BeginPacket
{
    private Vector2 ballPos;
    private Vector2 ballVel;
    private Vector2 p1Pos;
    private Vector2 p2Pos;

    public BeginPacket(Vector2 ballPos, Vector2 ballVel, Vector2 p1Pos, Vector2 p2Pos)
    {
        this.ballPos = ballPos;
        this.ballVel = ballVel;
        this.p1Pos = p1Pos;
        this.p2Pos = p2Pos;
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

    public Vector2 getP1Pos()
    {
        return p1Pos;
    }

    public void setP1Pos(Vector2 p1Pos)
    {
        this.p1Pos = p1Pos;
    }

    public Vector2 getP2Pos()
    {
        return p2Pos;
    }

    public void setP2Pos(Vector2 p2Pos)
    {
        this.p2Pos = p2Pos;
    }
}
