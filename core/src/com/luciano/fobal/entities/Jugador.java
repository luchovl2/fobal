package com.luciano.fobal.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.luciano.fobal.packets.ActionPacket;
import com.luciano.fobal.utils.CreateBody;
import com.luciano.fobal.utils.Constants;

import static java.lang.Math.abs;

public class Jugador
{
    private Sprite sprite = null;
    private Sprite spritePie = null;
    public Body body;
    public Body foot;
    public RevoluteJoint hip;
    private boolean miraDerecha;
    private float upperAngle;
    private float lowerAngle;
    private boolean saltando;

    public int up;
    public int left;
    public int right;
    private int down;
    public int kick;

    public boolean remote = false;

    public ActionPacket delayedAction;

    public Jugador(World world, Vector2 position, boolean miraDer)
    {
        this.miraDerecha = miraDer;
        saltando = false;

        crearJugador(world, position);

        sprite = new Sprite(new Texture(Constants.JUGADOR_TEXTURE));
        sprite.setSize(Constants.JUGADOR_CABEZA_RADIO*2, Constants.JUGADOR_CABEZA_RADIO*2);
        sprite.setOriginCenter();

        spritePie = new Sprite(new Texture(Constants.JUGADOR_PIE_TEXTURE));
        spritePie.setSize(Constants.JUGADOR_PIE_WIDTH, Constants.JUGADOR_PIE_HEIGHT);
        spritePie.setOriginCenter();

        if(miraDer)
        {
            sprite.flip(true, false);
            spritePie.flip(true, false);

            up = Input.Keys.W;
            left = Input.Keys.A;
            right = Input.Keys.D;
            kick = Input.Keys.K;
        }
        else
        {
            up = Input.Keys.UP;
            left = Input.Keys.LEFT;
            right = Input.Keys.RIGHT;
            kick = Input.Keys.SPACE;
        }
    }

    public void update(float delta)
    {
        doDelayedAction();
        saltando = abs(body.getLinearVelocity().y) > 0.001;

        float flip = 1;
        if(!miraDerecha)
        {
            flip = -1;  //si mira izquierda invertir el angulo para comparar igual
            //que si mirara derecha
        }

        if ((flip* hip.getJointAngle()) >= upperAngle)
        {
            hip.enableMotor(false);
            hip.setMotorSpeed(0);
        }
        else if ((flip* hip.getJointAngle()) <= lowerAngle)
        {
            hip.enableMotor(true);
            //hip.setMotorSpeed(0);
        }
    }

    private void doDelayedAction()
    {
        if(delayedAction == null)
        {

        }
        else if(delayedAction.getAction() == ActionPacket.Action.KICK)
        {
//            body.setTransform(delayedAction.getPlayerPos(), 0);
//            body.setLinearVelocity(delayedAction.getPlayerVel());
            kick();
        }
        else if(delayedAction.getAction() == ActionPacket.Action.JUMP)
        {
//            body.setTransform(delayedAction.getPlayerPos(), 0);
//            body.setLinearVelocity(delayedAction.getPlayerVel());
            jump();
        }
        else if(delayedAction.getAction() == ActionPacket.Action.LEFT)
        {
//            body.setTransform(delayedAction.getPlayerPos(), 0);
//            body.setLinearVelocity(delayedAction.getPlayerVel());
            moveLeft();
        }
        else if(delayedAction.getAction() == ActionPacket.Action.RIGHT)
        {
//            body.setTransform(delayedAction.getPlayerPos(), 0);
//            body.setLinearVelocity(delayedAction.getPlayerVel());
            moveRight();
        }
        delayedAction = null;   //una vez realizada, eliminarla (garbage-collector-earla)
    }

    public void jump()
    {
        Vector2 velocidad = body.getLinearVelocity();

        if(!saltando)
        {
            body.setLinearVelocity(velocidad.x, Constants.JUGADOR_SALTO);
            saltando = true;
        }
    }

    public void avanzar()
    {
        Vector2 velocidad = body.getLinearVelocity();

        if(miraDerecha)
            body.setLinearVelocity(Constants.JUGADOR_VELOCIDAD, velocidad.y);
        else
            body.setLinearVelocity(-Constants.JUGADOR_VELOCIDAD, velocidad.y);
    }

    public void retroceder()
    {
        Vector2 velocidad = body.getLinearVelocity();

        if(miraDerecha)
            body.setLinearVelocity(-Constants.JUGADOR_VELOCIDAD, velocidad.y);
        else
            body.setLinearVelocity(Constants.JUGADOR_VELOCIDAD, velocidad.y);
    }

    public void kick()
    {
        hip.enableMotor(true);

        if(miraDerecha)
            hip.setMotorSpeed(40f);
        else
            hip.setMotorSpeed(-40f);
    }

    public void cabecear()
    {
        jump();
    }

    public void respawn()
    {
        if(miraDerecha)
        {
            body.setTransform(Constants.JUGADOR_SPAWN, 0);
            foot.setTransform(Constants.JUGADOR_SPAWN.x,
                    Constants.JUGADOR_SPAWN.y + Constants.JUGADOR_PIE_Y, 0);
        }
        else
        {
            body.setTransform(Constants.JUGADOR_SPAWN_2, 0);
            foot.setTransform(Constants.JUGADOR_SPAWN_2.x,
                    Constants.JUGADOR_SPAWN_2.y + Constants.JUGADOR_PIE_Y, 0);
        }
    }

    public void render(SpriteBatch batch)
    {
        if(sprite != null && body != null)
        {
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
            sprite.setRotation(body.getAngle()*180f/MathUtils.PI);
            sprite.draw(batch);

            if(spritePie != null)
            {
                spritePie.setCenter(foot.getPosition().x, foot.getPosition().y);
                spritePie.setRotation(foot.getAngle()*180f/MathUtils.PI);
                spritePie.draw(batch);
            }
        }
    }

    private void crearJugador(World world, Vector2 position)
    {
        float sentido = 1;

        if(miraDerecha)
            sentido = -1;

        body = CreateBody.createBodyBall(world, position.x, position.y,
                Constants.JUGADOR_CABEZA_RADIO, false);

        PolygonShape pata = new PolygonShape();
        pata.setAsBox(Constants.JUGADOR_PATA_WIDTH/2,
                Constants.JUGADOR_PATA_HEIGHT/2,
                new Vector2(0, Constants.JUGADOR_PATA_Y), 0);

        CreateBody.addBodyPart(body, pata);
        pata.dispose();

        body.setFixedRotation(true);
        body.getFixtureList().get(0).setDensity(Constants.JUGADOR_CABEZA_DENSIDAD);
        body.getFixtureList().get(1).setDensity(Constants.JUGADOR_PATA_DENSIDAD);
        body.resetMassData();

        PolygonShape cunia = new PolygonShape();

        float[] vertices = {(-1)*sentido*Constants.JUGADOR_PIE_WIDTH/2, -Constants.JUGADOR_PIE_HEIGHT/2,
                sentido*Constants.JUGADOR_PIE_WIDTH/2, -Constants.JUGADOR_PIE_HEIGHT/2,
                sentido*Constants.JUGADOR_PIE_WIDTH/2, Constants.JUGADOR_PIE_HEIGHT/2,
                (-1)*sentido*Constants.JUGADOR_PIE_WIDTH/2, -Constants.JUGADOR_PIE_HEIGHT/4};
        cunia.set(vertices);

        foot = CreateBody.createBody(world,
                position.x,
                position.y + Constants.JUGADOR_PIE_Y,
                cunia,
                1f,
                false);

        foot.getFixtureList().get(0).setDensity(Constants.JUGADOR_PIE_DENSIDAD);
        cunia.dispose();

        lowerAngle = -0.3f;
        upperAngle = MathUtils.PI/2;

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, foot, body.getWorldCenter());
        jointDef.enableLimit = true;
        jointDef.maxMotorTorque = 30f;
        jointDef.upperAngle = upperAngle;
        jointDef.lowerAngle = lowerAngle;
        if(!miraDerecha)
        {
            jointDef.upperAngle = -lowerAngle;
            jointDef.lowerAngle = -upperAngle;
        }

        hip = (RevoluteJoint)world.createJoint(jointDef);
    }

    public void moveLeft()
    {
        body.setLinearVelocity(-Constants.JUGADOR_VELOCIDAD, body.getLinearVelocity().y);
    }

    public void moveRight()
    {
        body.setLinearVelocity(Constants.JUGADOR_VELOCIDAD, body.getLinearVelocity().y);
    }
}
