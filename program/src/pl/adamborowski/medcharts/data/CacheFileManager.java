/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import pl.adamborowski.utils.FileUtil;

/**
 * Set of tools to create bin-cache/... files depentent from source file
 *
 * @author adam
 */
public class CacheFileManager {
//    public static final int CACHE_FILE_BUFFER_SIZE = 8 * 1024;
    public static final int CACHE_FILE_BUFFER_SIZE = 64 * 1024;

    protected final Path binPath;
    protected final Path sourceFilePath;
    protected final String sourceFileName;
    public final File sourceFile;

    public CacheFileManager(Path binPath, Path sourceFilePath) {
        this.binPath = binPath;
        this.sourceFilePath = sourceFilePath;
        this.sourceFile = sourceFilePath.toFile();
        sourceFileName = FileUtil.getName(sourceFilePath.toFile());
    }

    public File getCacheFile(String infix) {
        return binPath.resolve(sourceFileName + infix + ".bin").toFile();
    }

    public ObjectInputStream openCacheFile(File binaryFile) throws IOException {
        ObjectInputStream binaryStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(binaryFile)));
        binaryStream.readInt(); // przy odczytaniu trzeba pominąć nagłówek
        binaryStream.readLong();
        return binaryStream;
    }

    public ObjectInputStream openCacheFile(String infix) throws IOException {
        return openCacheFile(getCacheFile(infix));
    }

    public ObjectOutputStream createCacheFile(String infix) throws IOException {
        // hashowanie to nazwa pliku oraz data modyfikacji
        long lastModified = sourceFile.lastModified();
        File binaryFile = getCacheFile(infix);
        binaryFile.delete();
        ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(binaryFile), CACHE_FILE_BUFFER_SIZE));
        stream.writeInt(sourceFileName.hashCode());
        stream.writeLong(lastModified);
        return stream;
    }

    public boolean isCacheFileValid(String infix) {
        try {
            File binaryFile = getCacheFile(infix);
            if (binaryFile.exists() == false) {
                return false;
            }
            int sourceFileNameHashCode;
            long sourceDateModifiedMust;
            try (ObjectInputStream binaryStream = new ObjectInputStream(new FileInputStream(binaryFile))) {
                sourceFileNameHashCode = binaryStream.readInt();
                sourceDateModifiedMust = binaryStream.readLong();
            }
            if (sourceFileNameHashCode != sourceFileName.hashCode()) {
                return false;
            }
            if (sourceDateModifiedMust != sourceFile.lastModified()) {
                return false;
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
