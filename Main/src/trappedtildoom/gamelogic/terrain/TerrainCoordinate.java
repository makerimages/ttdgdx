package trappedtildoom.gamelogic.terrain;

public class TerrainCoordinate {
    public final int x;
    public final int y;

    public TerrainCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TerrainCoordinate offset(int x, int y) {
        return new TerrainCoordinate(this.x + x, this.y + y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
