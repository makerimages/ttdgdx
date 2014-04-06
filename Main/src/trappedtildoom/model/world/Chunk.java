package trappedtildoom.model.world;

import com.sudoplay.joise.module.Module;

public class Chunk {
    private float offsetX = 0;
    private float offsetY = 0;
    public int width = 0;
    public int height = 0;

    private float scale = 50;

    public boolean requestDraw;

    private Module module;

    private Block[][] blocks;

    public void initialize(Module module) {
        this.module = module;
        refresh();
    }

    public Block getBlock(int x, int y) {
        return blocks[y][x];
    }

    public void offset(float dx, float dy) {
        this.offsetX += dx;
        this.offsetY += dy;
        refresh();
    }

    public void scale(float s) {
        scale = Math.max(1f, scale + s);
        refresh();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        blocks = new Block[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                blocks[y][x] = new Block();
            }
        }
        refresh();
    }

    private void refresh() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                blocks[y][x].setBlockType(BlockType.parse((int)module.get((x + (int)offsetX) / scale, (y + (int)offsetY) / scale)));
            }
        }
        requestDraw = true;
    }
}
