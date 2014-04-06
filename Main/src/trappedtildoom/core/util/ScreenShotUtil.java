package trappedtildoom.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotUtil {
    public static void makeCapture() {
        saveCapture();
    }

    public static void saveCapture() {
        int counter = 0;
        String fileNamePrefix = "captures/Capture_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        FileHandle file;
        do {
            file = Gdx.files.local(fileNamePrefix + (counter > 0 ? ("_" + counter) : "") + ".png");
            counter++;
        } while (file.exists());

        PixmapIO.writePNG(file, getCapture(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));
    }

    public static Pixmap getCapture(int x, int y, int width, int height, boolean flipY) {
        Gdx.gl.glPixelStorei(GL10.GL_PACK_ALIGNMENT, 1);

        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixels);

        final int numBytes = width * height * 4;
        byte[] lines = new byte[numBytes];
        if (flipY) {
            final int numBytesPerLine = width * 4;
            for (int i = 0; i < height; i++) {
                pixels.position((height - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        } else {
            pixels.clear();
            pixels.get(lines);
        }

        return pixmap;
    }
}
