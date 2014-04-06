package trappedtildoom.gamelogic.terrain;

import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

public class Voxel implements Serializable {

    public final TerrainType type;
    public float health = 100;
    public transient Body body;

    public Voxel(TerrainType type) {
        this.type = type;
    }
}
