package trappedtildoom.core.serialization;

import java.io.*;
import trappedtildoom.core.service.AbsoluteLogger;

public class GameSerializer {

    private static final int SAVE_FILE_VERSION = 2;

    public GameSerializer() {
        AbsoluteLogger.info("[GameSerializer] Save file version is: " + SAVE_FILE_VERSION);
    }

    public void serialize(File file, GameData gameData) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
        stream.writeChars("VERSION:");
        stream.writeChars(String.valueOf(SAVE_FILE_VERSION));
        stream.writeChar('#');
        stream.writeObject(gameData.toArray());
        stream.close();
    }

    public GameData deserialize(File file) throws IOException, ClassNotFoundException {
        try (GameInputStream stream = new GameInputStream(new FileInputStream(file))) {
            return GameData.fromArray(stream.readGameData());
        }
    }
}
