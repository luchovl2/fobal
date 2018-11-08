package com.luciano.fobal.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fobal.utils.CreateBody;
import com.luciano.fobal.utils.Constants;

import java.util.Random;

public class Pelota
{
    private Sprite sprite = null;
    public Body body;
    private World world;
    private Random random;

    public Pelota(World world, Vector2 position)
    {
        this.world = world;
        random = new Random();

        body = CreateBody.createBodyBall(world,
                position.x, position.y, Constants.PELOTA_RADIO, false);

        body.getFixtureList().get(0).setDensity(Constants.PELOTA_DENSIDAD);
        body.resetMassData();
        body.getFixtureList().get(0).setRestitution(Constants.PELOTA_RESTITUCION);
        body.getFixtureList().get(0).setUserData(this);

        body.setLinearDamping(0.3f);

        sprite = new Sprite(new Texture(Constants.PELOTA_TEXTURE));
        sprite.setSize(Constants.PELOTA_RADIO*2, Constants.PELOTA_RADIO*2);
        sprite.setOriginCenter();
    }

    public void update(float delta)
    {

    }

    public void respawn()
    {

        body.setLinearVelocity(5f*(random.nextFloat()-0.5f), 0);
        body.setAngularVelocity(0);
        body.setTransform(Constants.PELOTA_SPAWN, 0);
    }

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
            sprite.setRotation(body.getAngle() * 180f / MathUtils.PI);
            sprite.draw(batch);
        }
    }
}
