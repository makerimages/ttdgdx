package trappedtildoom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.gamelogic.world.GameWorld;

public class GuiWorldgem extends Gui implements Disposable {

    private final Texture texture = new Texture(Gdx.files.internal("res/textures/guis/WorldGem.png"));
    private final GameWorld gameWorld;

    public GuiWorldgem(String name, float w, float h, GameWorld gameWorld) {
        super(name, w, h);
        this.gameWorld = gameWorld;
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    protected Texture getTexture() {
        return texture;
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y) {
        super.draw(batch, x, y);

        CharSequence rainStatus = "World rain status is: " + String.valueOf(gameWorld.rain);
        CharSequence snowStatus = "World snow status is: " + String.valueOf(gameWorld.snow);
        CharSequence thunderStatus = "World thunder status is: " + String.valueOf(gameWorld.thunder);
        CharSequence earthquakeStatus = "World earthquake status is: " + String.valueOf(gameWorld.earthquake);

        BitmapFont font = new BitmapFont();
        font.draw(batch, rainStatus, Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 205);
        font.draw(batch, snowStatus, Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 180);
        font.draw(batch, thunderStatus, Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 155);
        font.draw(batch, earthquakeStatus, Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 130);
        font.draw(batch, "Time: " + gameWorld.getTime(), Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 105);
    }
}
