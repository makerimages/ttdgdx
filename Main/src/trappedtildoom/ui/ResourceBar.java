package trappedtildoom.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class ResourceBar extends Actor implements Disposable {
    private final Texture textureOn;
    private final Texture textureOff;

    public int currentValue = 0;
    public int maximumValue = 10;

    public boolean inverse = false;

    public ResourceBar(Texture textureOn, Texture textureOff) {
        textureOn.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textureOff.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        this.textureOn = textureOn;
        this.textureOff = textureOff;
    }

    @Override
    public void dispose() {
        textureOn.dispose();
        textureOff.dispose();
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        for (int valueIndex = 0; valueIndex < maximumValue; valueIndex++) {
            Texture texture = (currentValue > valueIndex) ? textureOn : textureOff;
            float x = getX() + 16 * (inverse ? (maximumValue - valueIndex - 1) : valueIndex);
            batch.draw(texture, x, getY(), getOriginX(), getOriginY(),
                    texture.getWidth(), texture.getHeight(), getScaleX(), getScaleY(), getRotation(),
                    0, 0, texture.getWidth(), texture.getHeight(), false, false);
        }
    }
}
