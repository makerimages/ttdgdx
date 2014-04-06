package trappedtildoom.core.serialization;


import junit.framework.TestCase;
import org.junit.Test;
import trappedtildoom.gamelogic.gameobject.Player;
import trappedtildoom.gamelogic.terrain.Terrain;
import trappedtildoom.gamelogic.terrain.TerrainType;
import trappedtildoom.gamelogic.terrain.Voxel;
import trappedtildoom.gamelogic.world.GameWorld;

import java.io.File;
import java.io.FileInputStream;

public class GameInputStreamTest extends TestCase {
    @Test
    public void testReadVersion_1_0a1() throws Exception {
        File saveFile = new File("Test/data/Save_1.0a1.sav");
        assertTrue(saveFile.exists());

        Object[] saveData;
        try (GameInputStream stream = new GameInputStream(new FileInputStream(saveFile))) {
            saveData = stream.readGameData();
        }

        assertNotNull(saveData);
        assertEquals(1, saveData.length);
        assertNotNull(saveData[0]);
        assertTrue(saveData[0] instanceof Terrain);

        Terrain terrain = (Terrain)saveData[0];
        assertEquals(41, terrain.width);
        assertEquals(33, terrain.height);

        assertNotNull(terrain.terrain);
        assertEquals(41, terrain.terrain.length);
        for (Voxel[] voxels : terrain.terrain) {
            assertNotNull(voxels);
            assertEquals(33, voxels.length);
        }

        assertNotNull(terrain.terrain[0][31]);
        assertEquals(TerrainType.Stone, terrain.terrain[0][31].type);

        assertNotNull(terrain.terrain[1][31]);
        assertEquals(TerrainType.Wood, terrain.terrain[1][31].type);

        assertNotNull(terrain.terrain[2][31]);
        assertEquals(TerrainType.Sand, terrain.terrain[2][31].type);

        assertNotNull(terrain.terrain[3][31]);
        assertEquals(TerrainType.Quicksand, terrain.terrain[3][31].type);

        assertNotNull(terrain.terrain[4][31]);
        assertEquals(TerrainType.Sandstone, terrain.terrain[4][31].type);

        assertNotNull(terrain.terrain[5][31]);
        assertEquals(TerrainType.Dirt, terrain.terrain[5][31].type);

        assertNotNull(terrain.terrain[6][31]);
        assertEquals(TerrainType.CoalOre, terrain.terrain[6][31].type);

        assertNotNull(terrain.terrain[7][31]);
        assertEquals(TerrainType.IronOre, terrain.terrain[7][31].type);

        assertNotNull(terrain.terrain[8][31]);
        assertEquals(TerrainType.Grass, terrain.terrain[8][31].type);
        assertEquals(77f, terrain.terrain[8][31].health);
    }

    @Test
    public void testReadVersion_1_0a2() throws Exception {
        File saveFile = new File("Test/data/Save_1.0a2.sav");
        assertTrue(saveFile.exists());

        Object[] saveData;
        try (GameInputStream stream = new GameInputStream(new FileInputStream(saveFile))) {
            saveData = stream.readGameData();
        }

        assertNotNull(saveData);
        assertEquals(3, saveData.length);
        assertNotNull(saveData[0]);
        assertTrue(saveData[0] instanceof Terrain);

        Terrain terrain = (Terrain)saveData[0];
        assertEquals(41, terrain.width);
        assertEquals(33, terrain.height);

        assertNotNull(terrain.terrain);
        assertEquals(41, terrain.terrain.length);
        for (Voxel[] voxels : terrain.terrain) {
            assertNotNull(voxels);
            assertEquals(33, voxels.length);
        }

        assertNotNull(terrain.terrain[0][31]);
        assertEquals(TerrainType.Stone, terrain.terrain[0][31].type);

        assertNotNull(terrain.terrain[1][31]);
        assertEquals(TerrainType.Wood, terrain.terrain[1][31].type);

        assertNotNull(terrain.terrain[2][31]);
        assertEquals(TerrainType.Sand, terrain.terrain[2][31].type);

        assertNotNull(terrain.terrain[3][31]);
        assertEquals(TerrainType.Quicksand, terrain.terrain[3][31].type);

        assertNotNull(terrain.terrain[4][31]);
        assertEquals(TerrainType.Sandstone, terrain.terrain[4][31].type);

        assertNotNull(terrain.terrain[5][31]);
        assertEquals(TerrainType.Dirt, terrain.terrain[5][31].type);

        assertNotNull(terrain.terrain[6][31]);
        assertEquals(TerrainType.CoalOre, terrain.terrain[6][31].type);

        assertNotNull(terrain.terrain[7][31]);
        assertEquals(TerrainType.IronOre, terrain.terrain[7][31].type);

        assertNotNull(terrain.terrain[8][31]);
        assertEquals(TerrainType.Grass, terrain.terrain[8][31].type);
        assertEquals(77f, terrain.terrain[8][31].health);

        assertNotNull(saveData[1]);
        assertTrue(saveData[1] instanceof GameWorld);

        GameWorld gameWorld = (GameWorld)saveData[1];
        assertFalse(gameWorld.rain);
        assertFalse(gameWorld.snow);
        assertFalse(gameWorld.thunder);
        assertFalse(gameWorld.earthquake);
        assertFloatEquals(22.4848f, gameWorld.time);
        assertEquals(0, gameWorld.numberOfDays);

        assertNotNull(saveData[2]);
        assertTrue(saveData[2] instanceof Player);

        Player player = (Player)saveData[2];
        assertFloatEquals(959.0661f, player.getPosition().x);
        assertFloatEquals(607.8398f, player.getPosition().y);
        assertEquals(5, player.xp);
        assertEquals(1, player.level);
        assertEquals(70, player.health);
        assertEquals(30, player.food);
        assertEquals(1, player.hairTextureId);
        assertEquals(2, player.headTextureId);
        assertEquals(1, player.bodyTextureId);
        assertEquals(3, player.legTextureId);
        assertEquals(1, player.shoeTextureId);
        assertTrue(player.flipX);

//        assertNotNull(player.currentItem);
  //      assertFalse(player.currentItem.edible);
    //    assertEquals("Wooden Axe", player.currentItem.name);
      ///  assertFloatEquals(10f, player.currentItem.durability);
       // assertEquals(ItemType.TOOL, player.currentItem.type);
        //assertFloatEquals(100f, player.currentItem.health);
    }

    private static void assertFloatEquals(float x, float y) {
        assertTrue(Math.abs(x - y) < 0.0001f);
    }
}
