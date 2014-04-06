package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.model.world.Block;
import trappedtildoom.model.world.BlockType;
import trappedtildoom.model.world.Chunk;
import trappedtildoom.model.world.ChunkManager;

public class SandboxScreen extends TrappedTilDoomScreen {

    private final int SPRITE_SIZE = 8;

    private final ChunkManager chunkManager = new ChunkManager();
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Texture texture = new Texture(Gdx.files.internal("res/textures/blocks/Empty.png"));

    public SandboxScreen(TrappedTilDoomGame game) {
        super(game);
    }

    @Override
    public void show() {
        chunkManager.initializeWorld();

        Gdx.input.setInputProcessor(commonInputProcessor);

        Chunk chunk = chunkManager.getActiveChunk();
        chunk.resize(Gdx.graphics.getWidth() / SPRITE_SIZE, Gdx.graphics.getHeight() / SPRITE_SIZE);
    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();

        float dx = 0;
        float dy = 0;
        float dz = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            dx += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            dx -= delta;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            dy -= delta;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            dy += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            dz += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            dz -= delta;

        if (dx != 0 || dy != 0) {
            chunkManager.getActiveChunk().offset(250 * dx, 250 * dy);
        }

        if (dz != 0) {
            chunkManager.getActiveChunk().scale(100 * dz);
        }
    }

    @Override
    protected void draw(float delta) {
        Chunk chunk = chunkManager.getActiveChunk();
        if (!chunk.requestDraw)
            return;

        spriteBatch.begin();

        for (int y = 0; y < chunk.height; y++) {
            for (int x = 0; x < chunk.width; x++) {
                Block block = chunk.getBlock(x, y);
                spriteBatch.setColor(block.getBlockType().color);
                spriteBatch.draw(texture, x * SPRITE_SIZE, Gdx.graphics.getHeight() - y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
            }
        }

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        Chunk chunk = chunkManager.getActiveChunk();
        chunk.resize(Gdx.graphics.getWidth() / SPRITE_SIZE, Gdx.graphics.getHeight() / SPRITE_SIZE);
    }
}
