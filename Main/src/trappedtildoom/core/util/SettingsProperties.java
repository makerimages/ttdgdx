package trappedtildoom.core.util;

import com.badlogic.gdx.Gdx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import trappedtildoom.core.service.AbsoluteLogger;

public class SettingsProperties {
    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 100;

    public enum Property {
        FULLSCREEN("fullscreen"),
        VSYNCENABLED("vsync"),
        SOUNDVOLUME("soundvolume");

        private final String name;

        private Property(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final Properties properties = new Properties();
    private final String fileName = "settings.properties";

    public SettingsProperties() {
        load();
    }

    private void load() {
        try {
            properties.load(new FileInputStream(fileName));
        } catch (IOException ignored) {
        }
    }

    private void save() {
        try {
            properties.store(Gdx.files.local("settings.properties").writer(false), null);
        } catch (IOException e) {
            AbsoluteLogger.severe(null, e);
        }
    }

    public boolean getBoolean(Property property, boolean defaultValue) {
        String stringValue = properties.getProperty(property.getName());
        return stringValue != null ? Boolean.parseBoolean(stringValue) : defaultValue;
    }

    public int getInteger(Property property, int defaultValue) {
        String stringValue = properties.getProperty(property.getName());
        int integerValue;
        try {
            integerValue = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            integerValue = defaultValue;
        }
        return stringValue != null ? integerValue : defaultValue;
    }

    public <V> void setProperty(Property property, V value) {
        properties.setProperty(property.getName(), String.valueOf(value));
        save();
    }
}
