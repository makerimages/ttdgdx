package trappedtildoom.core.serialization;

import java.io.*;
import trappedtildoom.core.service.AbsoluteLogger;

public class GameSlot {
    public final String fileName;
    public final File directory;

    public GameSlot(String fileName) {
        this(fileName, new File("saves"));
    }

    public GameSlot(String fileName, File directory) {
        this.fileName = fileName;
        this.directory = directory;
    }

    public synchronized void save(GameData gameData) {
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File saveFile = new File(directory, fileName + ".sav");
        if (saveFile.exists()) {
            saveFile.delete();
        }

        try {
            GameSerializer serializer = new GameSerializer();
            serializer.serialize(saveFile, gameData);
            AbsoluteLogger.info("[GameSlot] Game saved!");
        } catch (IOException e) {
            AbsoluteLogger.severe(null, e);
        }
    }

    public synchronized GameData load() {
        File file = new File(directory, fileName + ".sav");
        if (!file.exists()) {
            return null;
        }

        GameData gameData = null;

        try {
            GameSerializer serializer = new GameSerializer();
            gameData = serializer.deserialize(file);
            AbsoluteLogger.info("[GameSlot] Game loaded!");
        } catch (IOException | ClassNotFoundException e) {
            AbsoluteLogger.severe("Save file is corrupt: ", e);
        }

        return gameData;
    }

    public synchronized boolean validate() {
        File file = new File(directory, fileName + ".sav");
        if (!file.exists()) {
            return false;
        }

        try {
            GameSerializer serializer = new GameSerializer();
            serializer.deserialize(file);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public synchronized void delete() {
        File file = new File(directory, fileName + ".sav");
        if (file.exists()) {
            file.delete();
        }
    }
}
