package trappedtildoom.core.serialization;

import java.io.*;
import trappedtildoom.core.service.AbsoluteLogger;

public class GameInputStream extends ObjectInputStream {
    private int version;

    public GameInputStream(InputStream in) throws IOException {
        super(in);
       
    }

    public Object[] readGameData() throws IOException, ClassNotFoundException {
        version = getVersion();
        AbsoluteLogger.info("[GameInputstream] Got version: " + version);

        Object obj = super.readObject();

        if (obj instanceof Object[]) {
            return (Object[])obj;
        }

        return new Object[] { obj };
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        String className = resultClassDescriptor.getName();
        if (!className.contains("trappedtildoom")) {
            return resultClassDescriptor;
        }

        return ObjectStreamClass.lookup(Class.forName(className));
    }

    private int getVersion() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char ch;
        do {
            try {
                ch = readChar();
            } catch (EOFException e) {
                return 0;
            }
            stringBuilder.append(ch);
        } while (ch != '#');

        String version = stringBuilder.toString();
        if (!version.startsWith("VERSION:")) {
            return 0;
        }

        return Integer.parseInt(version.substring(8, version.length() - 1));
    }
}
