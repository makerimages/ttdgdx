package trappedtildoom.gamelogic.terrain;

import trappedtildoom.gamelogic.gameobject.GameObject;
import trappedtildoom.graphics.TerrainRenderer;

import java.io.Serializable;
import java.util.Random;

public class Terrain extends GameObject implements Serializable {

    private transient TerrainRenderer renderer;

    public  final Voxel[][] terrain;
    public final int width;
    public final int height;
    public static int groundLevel;
    private static final int SPACE_BETWEEN_TREES = 3;
    
    public Terrain(int width, int height) {
        this.width = width < 0 ? 0 : width;
        this.height = height < 0 ? 0 : height;
        this.terrain = new Voxel[this.width][this.height];
    }

    public void generate() {
        Random random = new Random();
        
        groundLevel = (int)(0.7f * height);

        for (int y = groundLevel; y >= 0; y--) {
            int depth = groundLevel - y;
            for (int x = 0; x < width; x++) {
                TerrainType type = selectTerrainType(random, depth);
                if (type != null) {
                    terrain[x][y] = new Voxel(type);
                }
            }
        }

        terrain[random.nextInt(width)][groundLevel - 1] = new Voxel(TerrainType.WorldGem);
 

        generateTrees(random, groundLevel);
    }

    private static TerrainType selectTerrainType(Random random, int depth) {
        if (depth > 2) {
            int decide = random.nextInt(100);
            if (decide > 55) return TerrainType.Stone;
            if (decide > 45) return TerrainType.CoalOre;
            if (decide > 35) return TerrainType.IronOre;
            return null;
        }

        if (depth > 1) {
            return random.nextInt(30) > 19 ? TerrainType.Sand : TerrainType.Dirt;
        }

        if (depth > 0) {
            return random.nextInt(21) > 10 ? TerrainType.Grass : TerrainType.Dirt;
        
        }

        return null;
    }

    private void generateTrees(Random random, int groundLevel) {
        int numTrees = 6 + random.nextInt(3);
        for (int treeIndex = 0; treeIndex < numTrees; treeIndex++) {
            int initialPosition = random.nextInt(width);

            int position = initialPosition;
            while (true) {
                position++;
                if (position >= width) {
                    position = 0;
                }
                if (position == initialPosition) {
                    return;
                }

                boolean tooClose = false;
                for (int i = Math.max(0, position - SPACE_BETWEEN_TREES); i < Math.min(width, position + SPACE_BETWEEN_TREES + 1); i++) {
                    Voxel voxel = terrain[i][groundLevel];
                    if (voxel != null && voxel.type == TerrainType.Wood) {
                        tooClose = true;
                        break;
                    }
                }

                if (!tooClose) {
                    int treeHeight = 4 + random.nextInt(5);
                    for (int i = 0; i < treeHeight; i++) {
                        terrain[position][groundLevel + i] = new Voxel(TerrainType.Wood);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void draw() {
        renderer.render(this);
    }

    public void setRenderer(TerrainRenderer renderer) {
        this.renderer = renderer;
    }
}
