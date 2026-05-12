package io.github.java_projekt_pk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.java_projekt_pk.globals.RenderingGlobals;
import io.github.java_projekt_pk.monsters.EnemyManager;
import io.github.java_projekt_pk.monsters.Slime;
import io.github.java_projekt_pk.monsters.SlimeSpecies;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    float time;
    TextureAtlas atlas;
    SpriteBatch spriteBatch;

    SlimeSpecies redSlimeSpecies;
    SlimeSpecies greenSlimeSpecies;
    SlimeSpecies blueSlimeSpecies;

    Slime slime1;
    Slime slime2;
    Slime slime3;
    Slime slime4;
    Slime slime5;

    public FirstScreen() {
        atlas = new TextureAtlas(Gdx.files.internal("texture_atlas.atlas"));
        RenderingGlobals.init();

        redSlimeSpecies = new SlimeSpecies(atlas, "Red_Slime");
        greenSlimeSpecies = new SlimeSpecies(atlas, "Green_Slime");
        blueSlimeSpecies = new SlimeSpecies(atlas, "Blue_Slime");

        slime1 = new Slime(redSlimeSpecies);
        slime1.setAnimation("Attack3");
        slime1.setPos(800, 500);
        EnemyManager.enemies.add(slime1);

        slime2 = new Slime(greenSlimeSpecies);
        slime2.setAnimation("Hurt");
        slime2.setPos(400, 600);
        EnemyManager.enemies.add(slime2);

        slime3 = new Slime(greenSlimeSpecies);
        slime3.setAnimation("Run");
        slime3.setScale(0.5f);
        EnemyManager.enemies.add(slime3);

        slime4 = new Slime(blueSlimeSpecies);
        slime4.setAnimation("Jump");
        slime4.setPos(300, 300);
        EnemyManager.enemies.add(slime4);

        slime5 = new Slime(blueSlimeSpecies);
        slime5.setAnimation("Walk");
        slime5.setPos(600, 400);
        EnemyManager.enemies.add(slime5);

        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        time += delta;

        handleInput();

        ScreenUtils.clear(Color.BLACK);

        slime3.setPos(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        spriteBatch.begin();
        slime1.draw(spriteBatch, time);
        slime2.draw(spriteBatch, time);
        slime3.draw(spriteBatch, time);
        slime4.draw(spriteBatch, time);
        slime5.draw(spriteBatch, time);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height
        // are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a
        // normal size before updating.
        if (width <= 0 || height <= 0)
            return;
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        atlas.dispose();
        spriteBatch.dispose();
        RenderingGlobals.font.dispose();
    }

    private void handleInput()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB))
        {
            EnemyManager.selectNextEnemy();
        }
    }
}