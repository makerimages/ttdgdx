package trappedtildoom.gamelogic.environmentobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class EnvironmentObjectWorldGem extends EnvironmentObject implements Disposable {
    private Texture texture = new Texture(Gdx.files.internal("res/textures/blocks/worldGem.png"));

    public EnvironmentObjectWorldGem(String name) {
        super(name);

        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    protected Texture getTexture() {
        return texture;
    }
}
