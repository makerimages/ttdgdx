package trappedtildoom.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.gamelogic.terrain.Terrain;
import trappedtildoom.gamelogic.terrain.TerrainType;

import java.util.HashMap;

public class TerrainRenderer implements Disposable, Renderer<Terrain> {

    private final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("res/textures/blocks/blocks.atlas"));
    private final HashMap<TerrainType, TextureRegion> textures = new HashMap<>();

    public TerrainRenderer() {
        for (TerrainType terrainType : TerrainType.values()) {
            textures.put(terrainType, textureAtlas.findRegion(terrainType.name()));
        }
    }

    @Override
    public void render(Terrain terrain) {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }
}
