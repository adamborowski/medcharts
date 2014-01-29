/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.reading;

import java.io.IOException;
import pl.adamborowski.medcharts.assembly.data.DataCollection;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.MainImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.utils.builders.HierarchyBuilder;

/**
 * Czytnik formatu binarnego do używania przez drivery
 *
 * @author test
 */
public class MainReader implements IDataReader<DataCollection> {

    /*
     * [x][a][b][start] // sekwencja int int int long
     * [maxModule] //miejsce na maksymalny moduł float
     */
    private DataSequence sequence;
    private float maxModule;
    public SerieReader serieReader;

    public float getMaxModule() {
        return Math.max(Math.abs(serieReader.config.maxValue), Math.abs(serieReader.config.minValue));
    }
    private final MainImporter importer;

    @Override
    public long getStart() {
        return serieReader.actAggregationReader.getStart();
    }

    public long getNumProbes() {
        return numProbes;
    }

    @Override
    public long getEnd() {
        return serieReader.actAggregationReader.getEnd();
    }
    private int numProbes;
    private long end;

    public MainReader(MainImporter importer) {
        this.importer = importer;
    }

    @Override
    public void initialize() throws IOException {

        serieReader = HierarchyBuilder.buildReaderHierarchyFromImporterHierarchy(this.importer.serieImporter);
        sequence = serieReader.config.dataSequence;
        int e = 2;
////        Collections.reverse(aggregations);//XXX  będziemy przeszuiwali agregacje od najgrubszej
//        //odczyt z pliku binarnego takich danych jak ilość próbek, pierwszy x, ostatni x
//        int x = raf.readInt();
//        int a = raf.readInt();
//        int b = raf.readInt();
//        long start = raf.readLong();
//        maxModule = raf.readFloat();
//        sequence = new DataSequence(x, a, b, start);
//        numProbes = (int) ((raf.length() - raf.getFilePointer()) / FRAME_BYTES_Y);
//        end = sequence.toTime(numProbes - 1);
//        //
//        AggregationImporter.DataProvider noAggregation = aggregations.get(aggregations.size()-1);
//        noAggregation.range = sequence.getSequenceLength();//zapewnij dobry dobór dla tych próbek przy automatycznym dostosowaniu
    }

    @Override
    public DataCollection getData(SpaceManager sp, int fromPixel, int toPixel) throws IOException {
        final int rangeCount = toPixel - fromPixel + 1 + 1;//ostatnia jedynka dla dużych powiększeń - tam zawsze umieścimy to co poza ekranem

        Float[] yData = new Float[rangeCount];
        long[] xData = new long[rangeCount];
        int counter = 0;
        for (int p = fromPixel; p <= toPixel; p++) {
            yData[counter] = readProbe(sp.toDataX(p), sp.getScaleX());
            xData[counter] = sequence.toTime(readProbe_probeIndex);
            counter++;
        }
        //znajdź próbkę pierwszą poza eranem
        final int lastProbe = sp.getSequence().toProbeIndex(xData[counter - 1]) + 1;
        final long lastProbeTime = sp.getSequence().toTime(lastProbe);
        yData[counter - 1] = readProbe(lastProbeTime, sp.getScaleX());
        xData[counter - 1] = lastProbeTime;
        return new DataCollection(yData, xData, new DataRange(xData[0], xData[xData.length - 1], (int) sp.getScaleX()));
    }
    private int readProbe_probeIndex;

    final public float readProbe(long x, float scale) throws IOException {
        //TODO później tutaj będzie bardziej zaawansowany algorytm wyznaczania próbki, 
        //w zależności od skali: dla dużego powiększenia będzie to interpolacja liniami
        //prostymi, dla małego będzie to uśrednianie/maxymalizacja z sąsiednich próbek
        readProbe_probeIndex = moveTo(x);
        int probeIndex = sequence.toProbeIndex(x);

//        if (aggregations.isEmpty()) {
        float tmp = 0;// raf.readFloat();
        return tmp;
//        }
//        AggregationImporter.DataProvider currentAggregation = aggregations.get(aggregations.size() - 1);
//        //idziemy od najbardziej szczegółowej agregacji do ogólnej i dopóki range mieści się w maxRange (1/scaleX) to doputy dobieramy skalę
//        float minRange = (scale);
////        if (minRange < aggregations.get(aggregations.size() - 1).range) {
////        System.out.println("tmp");
////            return tmp;
////        }
//        for (AggregationImporter.DataProvider a : aggregations) {
//            if (a.range < minRange) {
//                break;
//            }
//            currentAggregation = a;
//        }
//        System.out.println("a: " + currentAggregation.name);
//        final int aggregationIndex;
//        if (currentAggregation.name.equals("1ms")) {
//            aggregationIndex = probeIndex;
//        } else {
//            aggregationIndex = (int) (probeIndex / (currentAggregation.range / sequence.getSequenceLength() * sequence.getSequenceCount()));
//        }
//        return ((MinMaxImporter.MinMax) currentAggregation.getAggregation(aggregationIndex)).max;

    }

    /**
     *
     * @param x wartość faktyczna dziedziny (niepodzielona przez interval)
     * @throws IOException
     */
    public int moveTo(long x) throws IOException {
        if (x > end) {
            x = end;
        }
        final int probeIndex = sequence.toProbeIndex(x);
        //RandomA
//        raf.seek(FRAME_START + FRAME_BYTES_Y * probeIndex);
        return probeIndex;
    }

    public void skip(long deltaX) throws IOException {
//        raf.skipBytes((int) (deltaX * FRAME_BYTES_Y));
    }

    public void close() throws IOException {
//        raf.close();
    }

    @Override
    public DataSequence getSequence() {
        return sequence;
    }
}
