package com.luciano.fobal.packets;

import com.luciano.fobal.utils.FobalInput;

public class InputPacket
{
    private FobalInput input;

    public InputPacket()
    {
    }

    public InputPacket(FobalInput input)
    {
        this.input = input;
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
