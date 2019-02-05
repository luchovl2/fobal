package com.luciano.fobal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.CreateBody;

public class Pared
{
    public Body body;
    private World world;

    public Pared(World world, Vector2 posicion, float ancho, float largo)
    {
        this.world = world;

        crearPared(posicion, ancho, largo);
    }

    private void crearPared(Vector2 posicion, float ancho, float largo)
    {
        body = CreateBody.createBodyBox(world,
                posicion.x,
                posicion.y,
                ancho,
                largo,
                Constants.PARED_ROZAMIENTO,
                true);
    }

    public void render(SpriteBatch batch)
    {

    }
}
