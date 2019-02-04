package com.luciano.fobal.packets;

import com.badlogic.gdx.math.Vector2;

public class GameStatePacket
{
    private Vector2 p1Pos;
    private Vector2 p1Vel;
    private Vector2 p1FootPos;
    private float p1FootAng;
    private Vector2 p2Pos;
    private Vector2 p2Vel;
    private Vector2 p2FootPos;
    private float p2FootAng;
    private Vector2 ballPos;
    private Vector2 ballVel;
    private float ballAngVel;
    private int score1;
    private int score2;

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

    public GameStatePacket(Vector2 p1Pos, Vector2 p1Vel, Vector2 p1FootPos, Vector2 p2Pos, Vector2 p2Vel, Vector2 p2FootPos, Vector2 ballPos, Vector2 ballVel, float ballAngVel, int score1, int score2)
    {
        this.p1Pos = p1Pos;
        this.p1Vel = p1Vel;
        this.p1FootPos = p1FootPos;
        this.p2Pos = p2Pos;
        this.p2Vel = p2Vel;
        this.p2FootPos = p2FootPos;
        this.ballPos = ballPos;
        this.ballVel = ballVel;
        this.ballAngVel = ballAngVel;
        this.score1 = score1;
        this.score2 = score2;
    }

    public GameStatePacket(Vector2 p1Pos, Vector2 p1Vel, Vector2 p1FootPos, float p1FootAng, Vector2 p2Pos, Vector2 p2Vel, Vector2 p2FootPos, float p2FootAng, Vector2 ballPos, Vector2 ballVel, float ballAngVel, int score1, int score2)
    {
        this.p1Pos = p1Pos;
        this.p1Vel = p1Vel;
        this.p1FootPos = p1FootPos;
        this.p1FootAng = p1FootAng;
        this.p2Pos = p2Pos;
        this.p2Vel = p2Vel;
        this.p2FootPos = p2FootPos;
        this.p2FootAng = p2FootAng;
        this.ballPos = ballPos;
        this.ballVel = ballVel;
        this.ballAngVel = ballAngVel;
        this.score1 = score1;
        this.score2 = score2;
    }

    public float getP1FootAng()
    {
        return p1FootAng;
    }

    public void setP1FootAng(float p1FootAng)
    {
        this.p1FootAng = p1FootAng;
    }

    public float getP2FootAng()
    {
        return p2FootAng;
    }

    public void setP2FootAng(float p2FootAng)
    {
        this.p2FootAng = p2FootAng;
    }

    public Vector2 getP1FootPos()
    {
        return p1FootPos;
    }

    public void setP1FootPos(Vector2 p1FootPos)
    {
        this.p1FootPos = p1FootPos;
    }

    public Vector2 getP2FootPos()
    {
        return p2FootPos;
    }

    public void setP2FootPos(Vector2 p2FootPos)
    {
        this.p2FootPos = p2FootPos;
    }

    public int getScore1()
    {
        return score1;
    }

    public void setScore1(int score1)
    {
        this.score1 = score1;
    }

    public int getScore2()
    {
        return score2;
    }

    public void setScore2(int score2)
    {
        this.score2 = score2;
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
