package trappedtildoom.model.world;

import com.badlogic.gdx.graphics.Color;

public enum BlockType {
    Open(0, Color.BLACK),
    Dirt(1, Color.ORANGE),
    Stone(2, Color.DARK_GRAY),
    SemiRare(3, Color.LIGHT_GRAY),
    Rare(4, Color.YELLOW),
    Bedrock(5, Color.RED);

    public final int blockId;
    public final Color color;

    private BlockType(int id, Color color) {
        this.blockId = id;
        this.color = color;
    }

    public static BlockType parse(int id) {
        for (BlockType blockType : values()) {
            if (blockType.blockId == id) {
                return blockType;
            }
        }
        return Open;
    }
}
