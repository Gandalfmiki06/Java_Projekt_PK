package io.github.java_projekt_pk;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation {
    public static Animation<TextureRegion> fromAtlas(TextureAtlas atlas, String name) {
        var region = atlas.findRegion(name);
        if (region == null) {
            throw new RuntimeException("Cannot find animation in texture atlas: " + name);
        }
        var texture = region.getTexture();
        int frameCount = region.getRegionWidth() / region.getRegionHeight();
        TextureRegion frames[] = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, region.getRegionX() + i * region.getRegionHeight(),
                    region.getRegionY(), region.getRegionHeight(), region.getRegionHeight());
        }
        var spriteAnimation = new Animation<TextureRegion>(0.1f, frames);
        return spriteAnimation;
    }
}
