package trappedtildoom.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.gamelogic.gameobject.Player;
import trappedtildoom.gamelogic.item.Item;
import trappedtildoom.core.util.ClassUtil;
import trappedtildoom.gamelogic.item.PorkChopItem;
import trappedtildoom.gamelogic.item.WoodenAxeItem;

import java.util.Hashtable;

public class PlayerRenderer implements Disposable, Renderer<Player> {

    public static final float CHARACTER_SIZE = 32;

    public final Hashtable<Class<? extends Item>, Texture> itemTextures = new Hashtable<>();
    private final SpriteBatch spriteBatch;

    private Texture hairTexture;
    private Texture headTexture;
    private Texture bodyTexture;
    private Texture legTexture;
    private Texture shoeTexture;

    public PlayerRenderer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;

        itemTextures.put(PorkChopItem.class, new Texture(Gdx.files.internal("res/textures/items/porkChops.png")));
        itemTextures.put(WoodenAxeItem.class, new Texture(Gdx.files.internal("res/textures/items/WoodAxe.png")));

        for (Texture texture : itemTextures.values()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    @Override
    public void render(Player player) {
        Vector2 position = player.getPosition();

        drawTexture(spriteBatch, hairTexture, position, player);
        drawTexture(spriteBatch, headTexture, position, player);
        drawTexture(spriteBatch, bodyTexture, position, player);
        drawTexture(spriteBatch, legTexture, position, player);
        drawTexture(spriteBatch, shoeTexture, position, player);

        //drawItem(spriteBatch, player, position);
    }

    @Override
    public void dispose() {
        for (Texture texture : itemTextures.values()) {
            ClassUtil.dispose(texture);
        }
    }

    public void setHairTexture(Texture texture) {
        hairTexture = texture;
        hairTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void setHeadTexture(Texture texture) {
        headTexture = texture;
        headTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void setBodyTexture(Texture texture) {
        bodyTexture = texture;
        bodyTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void setLegTexture(Texture texture) {
        legTexture = texture;
        legTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void setShoeTexture(Texture texture) {
        shoeTexture = texture;
        shoeTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    /* private void drawItem(SpriteBatch spriteBatch, Player player, Vector2 position) {
        Item item = player.getCurrentItem();
        if (item == null)
            return;

        Texture texture = itemTextures.get(item.getClass());
        if (texture == null)
            return;

        drawTexture(spriteBatch, texture, position, player);
    }
        */
    private static void drawTexture(SpriteBatch spriteBatch, Texture texture, Vector2 position, Player player) {
        if (texture != null) {
            spriteBatch.draw(texture, position.x, position.y, CHARACTER_SIZE, CHARACTER_SIZE, 0, 0, texture.getWidth(), texture.getHeight(), player.flipX, false);
        }
    }
}
