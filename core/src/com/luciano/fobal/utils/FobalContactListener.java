package com.luciano.fobal.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.luciano.fobal.entities.Arco;
import com.luciano.fobal.entities.Pelota;

public class FobalContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getUserData() instanceof Arco && fixB.getUserData() instanceof Pelota)||
           (fixA.getUserData() instanceof Pelota && fixB.getUserData() instanceof Arco))
        {
            Arco arco;

            if(fixA.getUserData() instanceof Arco)
                arco = (Arco)fixA.getUserData();
            else
                arco = (Arco)fixB.getUserData();

            arco.pelotaAdentro();
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getUserData() instanceof Arco && fixB.getUserData() instanceof Pelota)||
                (fixA.getUserData() instanceof Pelota && fixB.getUserData() instanceof Arco))
        {
            Arco arco;

            if(fixA.getUserData() instanceof Arco)
                arco = (Arco)fixA.getUserData();
            else
                arco = (Arco)fixB.getUserData();

            arco.pelotaAfuera();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
