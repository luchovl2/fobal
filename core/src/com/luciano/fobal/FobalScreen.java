package com.luciano.fobal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.luciano.fobal.Scenes.Hud;
import com.luciano.fobal.utils.Constants;
import com.luciano.fobal.utils.FobalContactListener;

public class FobalScreen extends ScreenAdapter
{
    private SpriteBatch batch;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Level level;
    private Hud hud;

    public AssetManager manager;
    private Music music;

    public FobalScreen()
    {}

    @Override
    public void show()
    {
        batch = new SpriteBatch();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        hud = new Hud(batch);

        world = new World(new Vector2(0f, -9.8f), false);
        level = new Level(world, hud);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new FobalContactListener());

        manager = new AssetManager();
        manager.load("audio/circus_theme.mp3", Music.class);
        manager.finishLoading();

        music = manager.get("audio/circus_theme.mp3", Music.class);
        music.setLooping(true);
        music.play();
        music.setVolume(0.02f);
    }

    @Override
    public void render(float delta)
    {
        world.step(1/60f, 6, 2);

        level.update(delta);
        hud.update(delta);

        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.render(batch);

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //debugRenderer.render(world, level.viewport.getCamera().combined);

        if(Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            if (music.isPlaying())
                music.pause();
            else
                music.play();
        }
    }

    @Override
    public void dispose()
    {
        manager.dispose();
        batch.dispose();
        debugRenderer.dispose();
        world.dispose();
        hud.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        level.viewport.update(width, height, true);
        hud.viewport.update(width, height, true);
    }
}
