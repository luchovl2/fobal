package com.luciano.fobal.packets;

import com.luciano.fobal.utils.FobalInput;

public class InputPacket
{
    private int frame;
    private FobalInput input;

    public InputPacket()
    {
    }

    public InputPacket(FobalInput input)
    {
        this.input = input;
    }

    public InputPacket(int frame, FobalInput input)
    {
        this.frame = frame;
        this.input = input;
    }

    public int getFrame()
    {
        return frame;
    }

    public void setFrame(int frame)
    {
        this.frame = frame;
    }

    public FobalInput getInput()
    {
        return input;
    }

    public void setInput(FobalInput input)
    {
        this.input = input;
    }
}
