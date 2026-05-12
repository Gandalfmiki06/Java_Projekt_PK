package io.github.java_projekt_pk.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.java_projekt_pk.globals.RenderingGlobals;

public class Slime extends Enemy {
    static final float INITIAL_SCALE = 3.0f;

    private SlimeSpecies species;
    private float x = 0.0f;
    private float y = 0.0f;
    private float scale = 1.0f;
    private Animation<TextureRegion> animation;

    public Slime(SlimeSpecies species) {
        this.species = species;
        setAnimation("Idle");
    }

    public void setAnimation(String animationName) {
        animation = species.getAnimation(animationName);
    }

    // Set the position of the slime center ((0, 0) is lower left corner)
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Set slime scale (default: 1)
    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getPosX() {
        return x;
    }

    public float getPosY() {
        return y;
    }

    public float getScale() {
        return scale;
    }

    public void draw(SpriteBatch batch, float time) {
        var windowScale = Gdx.graphics.getHeight() / 720.0f * INITIAL_SCALE * scale;
        TextureRegion currentFrame = animation.getKeyFrame(time, true);
        float width = currentFrame.getRegionWidth() * windowScale;
        float height = currentFrame.getRegionHeight() * windowScale;
        // batch.draw(currentFrame, x - (int) (width / 2), Gdx.graphics.getHeight() - y - (int) (height * 0.15), width,
        //         height);
        batch.draw(currentFrame, x - (int) (width / 2), y, width, height);

        String renderString = getRenderString();
        GlyphLayout layout = new GlyphLayout(RenderingGlobals.font, renderString);
        RenderingGlobals.font.draw(batch, renderString, x - (int)(layout.width) / 2, y);
    }
}
