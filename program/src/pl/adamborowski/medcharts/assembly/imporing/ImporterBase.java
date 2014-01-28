/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.utils.FileUtil;

/**
 *
 * @author test
 */
public abstract class ImporterBase<TReader extends IDataReader> {

    public final static int HEADER_SIZE = (Integer.SIZE + Long.SIZE) / Byte.SIZE;
    protected TReader dataReader;

    public final TReader getReader() {
        return dataReader;
    }
    protected final AssemblyImporter assemblyImporter;
    public final CacheFileManager cacheFileManager;
    protected final File sourceFile;
    protected BufferedReader sourceStream;
    protected float numSourceLines; // float, żeby nie konwertować potem

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ImporterBase(AssemblyImporter assemblyImporter, File sourceFile) throws FileNotFoundException {
        this.assemblyImporter = assemblyImporter;
        this.sourceFile = sourceFile;
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(sourceFile.getName());
        }
        this.cacheFileManager = new CacheFileManager(assemblyImporter.getBinPath(), sourceFile.toPath());
        isCacheValid = isCacheValidImpl();
    }

    /**
     * musi być wykonane w konstruktorze klasy pochodnej
     *
     * @param reader
     */
    final protected void setReader(TReader reader) {
        this.dataReader = reader;
    }

    final public File getSourceFile() {
        return sourceFile;
    }

    /**
     * pewne klasy importerów mają więcej do roboty z tym samym rozmiarem pliku,
     * więc, aby pasek poszerzal się jednostajnie, klasy domnażają stałe.
     *
     * @return
     */
    public long getComplexityRatio() {
        return sourceFile.length();
    }
    protected boolean isCacheValid;

    /**
     * Getter który mówi, czy importer musi coś importować. W przypadku
     * OverlayImporter trzeba nadpisać tę metodę, żeby sprawdzać również plik
     * "*.sel.bin".
     *
     * @return
     */
    public final boolean isCacheValid() {
        return isCacheValid;
    }

    protected boolean isCacheValidImpl() {
        return hasValidBinary(sourceFile, "");
    }

    public final void doImport(boolean forceImport) throws IOException, ParseException {
        if (forceImport || !isCacheValid) {
            forceImport();
        }
        //skoro już zaimportowano, można zainicjować reader.
        dataReader.initialize();
    }

    /**
     * rozpoczyna konwersję tekstowego pliku źródłowego do binarnych wersji
     *
     * @throws FileNotFoundException
     */
    private void forceImport() throws IOException, ParseException {
        numSourceLines = FileUtil.countLines(sourceFile);
        try {
            sourceStream = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
            importImpl();
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            sourceStream.close();
        }
    }

    /**
     * implementacja importowania do napisania w klasach pochodnych
     */
    protected abstract void importImpl() throws IOException, ParseException;

    /**
     * Sprawdza, czy dla danego pliku źródłowego plik binarny istnieje i czy
     * jest odpowiedni
     *
     * @param sourceFile
     * @param infix
     * @return
     */
    final protected boolean hasValidBinary(File sourceFile, String infix) {
        try {
            File binaryFile = getBinaryFile(sourceFile, infix);
            if (binaryFile.exists() == false) {
                return false;
            }
            int sourceFileNameHashCode;
            long sourceDateModifiedMust;
            try (RandomAccessFile binaryStream = new RandomAccessFile(binaryFile, "r")) {
                sourceFileNameHashCode = binaryStream.readInt();
                sourceDateModifiedMust = binaryStream.readLong();
            }
            if (sourceFileNameHashCode != sourceFile.getName().hashCode()) {
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

    final protected boolean hasValidBinaryObjectStream(File sourceFile, String infix) {
        try {
            File binaryFile = getBinaryFile(sourceFile, infix);
            if (binaryFile.exists() == false) {
                return false;
            }
            int sourceFileNameHashCode;
            long sourceDateModifiedMust;
            try (ObjectInputStream binaryStream = new ObjectInputStream(new FileInputStream(binaryFile))) {
                sourceFileNameHashCode = binaryStream.readInt();
                sourceDateModifiedMust = binaryStream.readLong();
            }
            if (sourceFileNameHashCode != sourceFile.getName().hashCode()) {
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

    /**
     * Tworzy nowy plik binarny odpowiadający plikowi źródłowemu. Zapisuje do
     * niego nagłówek identyfikujący źródło (nazwa oraz data modyf)
     *
     * @param sourceFile
     * @param infix
     * @return
     */
    final protected RandomAccessFile createBinary(File sourceFile, String infix) throws IOException {
        // hashowanie to nazwa pliku oraz data modyfikacji
        String sourceFileName = sourceFile.getName();
        long lastModified = sourceFile.lastModified();
        File binaryFile = getBinaryFile(sourceFile, infix);
        binaryFile.delete();
        RandomAccessFile binaryStream = new RandomAccessFile(binaryFile, "rw");
        binaryStream.writeInt(sourceFileName.hashCode());
        binaryStream.writeLong(lastModified);
        return binaryStream;
    }

    final protected ObjectOutputStream createBinaryObjectStream(File sourceFile, String infix) throws IOException {
        // hashowanie to nazwa pliku oraz data modyfikacji
        String sourceFileName = sourceFile.getName();
        long lastModified = sourceFile.lastModified();
        File binaryFile = getBinaryFile(sourceFile, infix);
        binaryFile.delete();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(binaryFile));
        stream.writeInt(sourceFileName.hashCode());
        stream.writeLong(lastModified);
        return stream;
    }

    /**
     * inicjalizuje dostęp do pliku binarnego. Pomija nagłówek służący do
     * porównania z plikiem źródłowym. Jest publiczna, gdyż Reader potrzebuje
     * dostać się do pliku binarengo
     *
     * @param binaryFile
     * @return
     * @throws IOException
     */
    final public RandomAccessFile openBinary(File binaryFile) throws IOException {
        RandomAccessFile binaryStream = new RandomAccessFile(binaryFile, "r");
        binaryStream.readInt(); // przy odczytaniu trzeba pominąć nagłówek
        binaryStream.readLong();
        return binaryStream;
    }

    final public ObjectInputStream openBinaryInputStream(File binaryFile) throws IOException {
        ObjectInputStream binaryStream = new ObjectInputStream(new FileInputStream(binaryFile));
        binaryStream.readInt(); // przy odczytaniu trzeba pominąć nagłówek
        binaryStream.readLong();
        return binaryStream;
    }

    final public ObjectInputStream openBinaryInputStream(String infix) throws IOException {
        return openBinaryInputStream(getBinaryFile(sourceFile, infix));
    }

    final public RandomAccessFile openBinary(String infix) throws IOException {
        return openBinary(getBinaryFile(sourceFile, infix));
    }

    /**
     * Zwraca plik binarny odpowiadający plikowi źródłowemu
     *
     * @param sourceFile
     * @param infix wrostek, gdy chcemy pobrać plik *.sel.bin, a nie *.bin dla
     * źródła
     * @return
     */
    final protected File getBinaryFile(File sourceFile, String infix) {
        final String binaryFileName = FileUtil.getName(sourceFile) + infix + ".bin";
        return assemblyImporter.getBinPath().resolve(binaryFileName).toFile();
    }

    final protected File getSourceFile(String name) {
        return assemblyImporter.getSourcePath().resolve(name).toFile();
    }

    final protected File getBinaryFile(String sourceFileName, String infix) {
        return getBinaryFile(assemblyImporter.getSourcePath().resolve(sourceFileName).toFile(), infix);
    }
    private final int cycle = 1000;
    private int counter = cycle;

    final protected void setLineProgress(float line) {
        counter--;
        if (counter == 0) {
            counter = cycle;
            assemblyImporter.progressChanged(line / numSourceLines);
        }
    }
}
