package trappedtildoom.gamelogic.terrain;

public enum TerrainType {
    Stone,
    Wood,
    Sand,
    Quicksand,
    Sandstone,
    Dirt,
    CoalOre,
    IronOre,
    Grass,
    WaterStill,
    WorldGem(false);

    public final boolean breakable;

    private TerrainType() {
        this(true);
    }

    private TerrainType(boolean breakable) {
        this.breakable = breakable;
    }
}
