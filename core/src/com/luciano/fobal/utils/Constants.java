package com.luciano.fobal.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants
{
    public static final float PPM = 100;

    public static final Vector2 GRAVITY = new Vector2(0f, -9.8f);

    public static final float WORLD_WIDTH = 1024;   //mismo que en DesktopLauncher
    public static final float WORLD_HEIGHT = 620;   //mismo que en DesktopLauncher

    public static final int TIEMPO_JUEGO = 60;

    public static final float PARED_GROSOR = 20/PPM;
    public static final float PARED_ROZAMIENTO = 0.4f;

    public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
    public static final String BACKGROUND_TEXTURE = "images/background3.jpg";

    public static final String JUGADOR_TEXTURE = "images/yo.png";
    public static final String JUGADOR_PIE_TEXTURE = "images/botin.png";
    public static final Vector2 JUGADOR_SPAWN = new Vector2(200/PPM, 150/PPM);
    public static final Vector2 JUGADOR_SPAWN_2 = new Vector2(800/PPM, 150/PPM);
    public static final float JUGADOR_VELOCIDAD = 2f;
    public static final float JUGADOR_SALTO = 4f;

    public static final float JUGADOR_ALTURA = 80/PPM;
    public static final float JUGADOR_CABEZA_RADIO = JUGADOR_ALTURA/3;
    public static final float JUGADOR_CABEZA_DENSIDAD = 10f;

    public static final float JUGADOR_PATA_WIDTH = 19/PPM;
    public static final float JUGADOR_PATA_HEIGHT = 20/PPM;
    public static final float JUGADOR_PATA_Y = -JUGADOR_CABEZA_RADIO - JUGADOR_PATA_HEIGHT/2;
    public static final float JUGADOR_PATA_DENSIDAD = 5f;

    public static final float JUGADOR_PIE_WIDTH = 30/PPM;
    public static final float JUGADOR_PIE_HEIGHT = 15/PPM;
    public static final float JUGADOR_PIE_Y = -JUGADOR_CABEZA_RADIO - JUGADOR_PATA_HEIGHT +
                                                JUGADOR_PIE_HEIGHT/2 + 3/PPM;
    public static final float JUGADOR_PIE_DENSIDAD = 5f;

    public static final String PELOTA_TEXTURE = "images/pelota.png";
    public static final Vector2 PELOTA_SPAWN = new Vector2(500/PPM, 400/PPM);

    public static final float PELOTA_RADIO = 15/PPM;
    public static final float PELOTA_DENSIDAD = 0.8f;
    public static final float PELOTA_RESTITUCION = 0.6f;

    public static final String ARCO_TEXTURE = "images/arco_blanco.png";

    public static final float ARCO_WIDTH = 80/PPM;
    public static final float ARCO_HEIGHT = 190/PPM;
    public static final float ARCO_PALO_WIDTH = 10/PPM;
    public static final Vector2 ARCO_DERECHO_POS =
            new Vector2((WORLD_WIDTH/PPM - PARED_GROSOR/2), PARED_GROSOR/2);
    public static final Vector2 ARCO_IZQUIERDO_POS =
            new Vector2(PARED_GROSOR/2, PARED_GROSOR/2);

    public static final int PORT = 50505;
    public static final String HOST = "localhost";


    public enum Formas
    {
        CAJA,
        BOLA,
        RAMPA,
        VIGA,
        POLEA
    }
}
