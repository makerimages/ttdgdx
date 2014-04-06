package trappedtildoom.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.core.util.ClassUtil;

public abstract class Gui implements Disposable {

    private float width;
    private float height;

    public final String name;

    protected Gui(String name, float w, float h) {
        this.name = name;
        this.width = w;
        this.height = h;
    }

    protected Texture getTexture() {
        return null;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        Texture texture = getTexture();
        if (texture == null) {
            return;
        }

        batch.draw(texture, x, y, width, height);
    }

    @Override
    public void dispose() {
        ClassUtil.dispose(getTexture());
    }
}
