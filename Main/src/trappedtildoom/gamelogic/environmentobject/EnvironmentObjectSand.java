package trappedtildoom.gamelogic.environmentobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EnvironmentObjectSand extends EnvironmentObject {
    private Texture texture = new Texture(Gdx.files.internal("res/textures/blocks/sand.png"));

    public EnvironmentObjectSand(String name) {
        super(name);

        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    protected Texture getTexture() {
        return texture;
    }
}
