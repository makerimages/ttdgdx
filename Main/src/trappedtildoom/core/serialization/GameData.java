package trappedtildoom.core.serialization;

import trappedtildoom.gamelogic.gameobject.Player;
import trappedtildoom.gamelogic.terrain.Terrain;
import trappedtildoom.gamelogic.world.GameWorld;

import java.util.*;

public class GameData {
    public Terrain terrain;
    public GameWorld gameWorld;
    public Player player;

    public Object[] toArray() {
        return new Object[] {
            terrain,
            gameWorld,
            player
        };
    }

    public static GameData fromArray(Object[] saveData) {
        if (saveData == null || saveData.length == 0) {
            return null;
        }

        LinkedList<Object> queue = new LinkedList<>(Arrays.asList(saveData));
        GameData gameData = new GameData();

        gameData.terrain = (Terrain)queue.pop();
        if (queue.isEmpty())
            return gameData;

        gameData.gameWorld = (GameWorld)queue.pop();
        gameData.player = (Player)queue.pop();

        return gameData;
    }
}
