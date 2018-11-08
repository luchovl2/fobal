package com.luciano.fobal.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fobal.Level;
import com.luciano.fobal.utils.CreateBody;
import com.luciano.fobal.utils.Constants;

import static com.luciano.fobal.utils.Constants.PPM;

public class Arco
{
    private Level level;
    private Body body;
    private boolean derecho;
    private Sprite sprite = null;

    private boolean pelotaAdentro= false;

    public Arco(World world, boolean derecho, Level level)
    {
        this.derecho = derecho;
        this.level = level;

        crearArco(world);

        sprite = new Sprite(new Texture(Constants.ARCO_TEXTURE));
        sprite.setSize(Constants.ARCO_WIDTH, Constants.ARCO_HEIGHT);
        sprite.setCenter(Constants.ARCO_WIDTH / 2 + Constants.PARED_GROSOR / 2,
                Constants.ARCO_HEIGHT / 2 + Constants.PARED_GROSOR / 2);

        if (derecho)
        {
            sprite.flip(true, false);
            sprite.setCenter(-Constants.ARCO_WIDTH / 2 + Constants.WORLD_WIDTH / PPM - Constants.PARED_GROSOR / 2,
                    Constants.ARCO_HEIGHT / 2 + Constants.PARED_GROSOR / 2);
        }
    }

    public void update(float delta)
    {
        //detectar si la pelota está adentro
        if(pelotaAdentro)
        {
            if(derecho)
                level.gol(true);
            else
                level.gol(false);

            pelotaAfuera(); //baja el flag para no contar de nuevo
        }
    }

    private void crearArco(World world)
    {
        float posX;
        float posY;
        Vector2 techoPos;
        float techoAngulo;
        float width = Constants.ARCO_WIDTH -
                Constants.ARCO_PALO_WIDTH - Constants.PELOTA_RADIO * 2;

        if (derecho)
        {
            posX = Constants.ARCO_DERECHO_POS.x - width / 2;
            posY = Constants.ARCO_DERECHO_POS.y +
                    (Constants.ARCO_HEIGHT - Constants.ARCO_PALO_WIDTH) / 2;
            techoPos = new Vector2(-Constants.PELOTA_RADIO - Constants.ARCO_PALO_WIDTH / 2,
                    Constants.ARCO_HEIGHT / 2);
            techoAngulo = 0.006f;
        }
        else
        {
            posX = Constants.ARCO_IZQUIERDO_POS.x + width / 2;
            posY = Constants.ARCO_IZQUIERDO_POS.y +
                    (Constants.ARCO_HEIGHT - Constants.ARCO_PALO_WIDTH) / 2;
            techoPos = new Vector2(Constants.PELOTA_RADIO + Constants.ARCO_PALO_WIDTH / 2,
                    Constants.ARCO_HEIGHT / 2);
            techoAngulo = -0.006f;
        }

        body = CreateBody.createBodyBox(world,
                posX,
                posY,
                width,
                Constants.ARCO_HEIGHT - Constants.ARCO_PALO_WIDTH, 1, true);

        body.getFixtureList().get(0).setSensor(true);
        body.getFixtureList().get(0).setUserData(this);

        PolygonShape techo = new PolygonShape();
        techo.setAsBox(Constants.ARCO_WIDTH / 2,
                Constants.ARCO_PALO_WIDTH / 2,
                techoPos,
                techoAngulo);

        CreateBody.addBodyPart(body, techo);
        techo.dispose();
    }

    public boolean esDerecho()
    {
        return derecho;
    }

    public void pelotaAdentro()
    {
        pelotaAdentro =  true;
    }

    public void pelotaAfuera()
    {
        pelotaAdentro = false;
    }

    public boolean hayPelota()
    {
        return pelotaAdentro;
    }

    public void render(SpriteBatch batch)
    {
        if (sprite != null)
        {
            sprite.draw(batch);
        }
    }
}
