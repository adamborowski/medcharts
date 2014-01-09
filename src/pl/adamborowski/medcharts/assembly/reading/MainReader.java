/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.reading;

import java.io.IOException;
import java.io.RandomAccessFile;
import pl.adamborowski.medcharts.assembly.data.DataCollection;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.ImporterBase;
import pl.adamborowski.medcharts.assembly.imporing.MainImporter;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.utils.TimeUtil;

/**
 * Czytnik formatu binarnego do używania przez drivery
 *
 * @author test
 */
public class MainReader implements IDataReader<DataCollection>
{

    public static final int FRAME_BYTES_X = Long.SIZE / Byte.SIZE;
    public static final int FRAME_BYTES_Y = Float.SIZE / Byte.SIZE;
    public static final int FRAME_BYTES = FRAME_BYTES_X + FRAME_BYTES_Y;
    /*
     * [x][a][b][start] // sekwencja int int int long
     * [maxModule] //miejsce na maksymalny moduł float
     */
    private static final int FRAME_START = (3 * Integer.SIZE + Long.SIZE + Float.SIZE) / Byte.SIZE + ImporterBase.HEADER_SIZE;
    private DataSequence sequence;
    private float maxModule;

    public float getMaxModule()
    {
        return maxModule;
    }
    private final MainImporter importer;

    @Override
    public long getStart()
    {
        return sequence.getStart();
    }

    public long getNumProbes()
    {
        return numProbes;
    }

    @Override
    public long getEnd()
    {
        return end;
    }
    private int numProbes;
    private long end;
    private RandomAccessFile raf;

//    public DataRange createRange(int startProbe, int endProbe, int resolution)
//    {
//        //zwróć nową rangę która będzie
//        return new DataRange(sequence.getStart() + startProbe * interval, sequence.getStart() + endProbe * interval, resolution);
//    }
    public MainReader(MainImporter importer)
    {
        this.importer = importer;
    }

    @Override
    public void initialize() throws IOException
    {

        raf = importer.openBinary("");
        //odczyt z pliku binarnego takich danych jak ilość próbek, pierwszy x, ostatni x
        int x = raf.readInt();
        int a = raf.readInt();
        int b = raf.readInt();
        long start = raf.readLong();
        maxModule = raf.readFloat();
        sequence = new DataSequence(x, a, b, start);
        numProbes = (int) ((raf.length() - raf.getFilePointer()) / FRAME_BYTES_Y);
        end = sequence.toTime(numProbes - 1);
    }

    @Override
    public DataCollection getData(SpaceManager sp, int fromPixel, int toPixel) throws IOException
    {
        final int rangeCount = toPixel - fromPixel + 1 + 1;//ostatnia jedynka dla dużych powiększeń - tam zawsze umieścimy to co poza ekranem

        Float[] yData = new Float[rangeCount];
        long[] xData = new long[rangeCount];
        int counter = 0;
        for (int p = fromPixel; p <= toPixel; p++)
        {
            yData[counter] = readProbe(sp.toDataX(p), sp.getScaleX());
            xData[counter] = sequence.toTime(readProbe_probeIndex);
            counter++;
        }
        //znajdź próbkę pierwszą poza eranem
        final int lastProbe = sp.getSequence().toProbeIndex(xData[counter - 1])+1;
        final long lastProbeTime = sp.getSequence().toTime(lastProbe);
        yData[counter - 1] = readProbe(lastProbeTime, sp.getScaleX());
        xData[counter - 1] = lastProbeTime;
        return new DataCollection(yData, xData, new DataRange(xData[0], xData[xData.length - 1], (int) sp.getScaleX()));
    }
    private int readProbe_probeIndex;

    final public float readProbe(long x, float scale) throws IOException
    {
        //TODO później tutaj będzie bardziej zaawansowany algorytm wyznaczania próbki, 
        //w zależności od skali: dla dużego powiększenia będzie to interpolacja liniami
        //prostymi, dla małego będzie to uśrednianie/maxymalizacja z sąsiednich próbek
        readProbe_probeIndex = moveTo(x);
        return raf.readFloat();
    }

    /**
     *
     * @param x wartość faktyczna dziedziny (niepodzielona przez interval)
     * @throws IOException
     */
    public int moveTo(long x) throws IOException
    {
        if (x > end)
        {
            x = end;
        }
        final int probeIndex = sequence.toProbeIndex(x);
        //RandomA
        raf.seek(FRAME_START + FRAME_BYTES_Y * probeIndex);
        return probeIndex;
    }

    public void skip(long deltaX) throws IOException
    {
        raf.skipBytes((int) (deltaX * FRAME_BYTES_Y));
    }

    public void close() throws IOException
    {
        raf.close();
    }

    @Override
    public DataSequence getSequence()
    {
        return sequence;
    }
}
