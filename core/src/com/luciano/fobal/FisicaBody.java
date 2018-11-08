package com.luciano.fobal;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

import static com.luciano.fobal.utils.Constants.Formas;

public class FisicaBody
{
    public Body body = null;
    public float width;
    public float height;
    public Color color;
    public Formas forma;

    public FisicaBody()
    {
    }

    public FisicaBody(float width, float height, Color color, Formas forma)
    {
        this.width = width;
        this.height = height;
        this.color = color;
        this.forma = forma;
    }

    public void render(ShapeRenderer renderer)
    {
        renderer.setColor(this.color);

        if(forma == Formas.CAJA || forma == Formas.VIGA)
        {
            renderer.rect(
                    body.getPosition().x - width / 2,
                    body.getPosition().y - height / 2,
                    width / 2,
                    height / 2,
                    width,
                    height,
                    1,
                    1,
                    body.getAngle() * 180 / MathUtils.PI);
        }
        else if(forma == Formas.BOLA)
        {
            renderer.circle(body.getPosition().x,
                    body.getPosition().y,
                    width,
                    64);
        }
        else if(forma == Formas.RAMPA)
        {
            renderer.triangle(
                    body.getPosition().x,
                    body.getPosition().y,
                    body.getPosition().x+width,
                    body.getPosition().y,
                    body.getPosition().x+width,
                    body.getPosition().y+height);
        }
    }
}
