package dev.sturex.tree;

import java.io.*;
import java.nio.file.Path;

public enum TraitTreeDump {
    ;

    public static boolean save(TraitTree<?> traitTree, Path path) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            objectOutputStream.writeObject(traitTree);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static <T extends Enum<T>> TraitTree<T> restore(Path path) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path.toFile()));
            return (TraitTree<T>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
