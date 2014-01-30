/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.reading;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.data.Mask;
import pl.adamborowski.medcharts.assembly.data.OverlayLinesCollection;
import pl.adamborowski.medcharts.assembly.imporing.ImporterBase;
import pl.adamborowski.medcharts.assembly.imporing.MainImporter;
import pl.adamborowski.medcharts.assembly.imporing.OverlayImporter;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Overlay;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.data.SerieImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.utils.builders.HierarchyBuilder;

/**
 *
 * @author test
 */
public class OverlayReader implements IDataReader {

    private static final int FRAME_START = Integer.SIZE / Byte.SIZE + ImporterBase.HEADER_SIZE;//na początku jest ilość linii
    private static final int FRAME_BYTES_Y = Float.SIZE / Byte.SIZE;
    private final MainImporter mainImporter;
    private RandomAccessFile raf;
    private RandomAccessFile masksInput;
    private ArrayList<Mask> masks;
    private long firstTime;
    private long lastTime;
    private DataSequence sequence;
    private int numLines;
    private final Overlay binding;
    public ArrayList<SerieReader> serieReaders = new ArrayList<SerieReader>();

    public Overlay getBinding() {
        return binding;
    }

    public int getNumLines() {
        return numLines;
    }
    private final OverlayImporter overlayImporter;

    public OverlayReader(MainImporter mainImporter, OverlayImporter overlayImporter, Overlay binding) {
        this.binding = binding;
        this.mainImporter = mainImporter;
        this.overlayImporter = overlayImporter;
    }

    @Override
    public void initialize() throws IOException {
        //TODO: serieReaders = for()...  HierarchyBuilder.buildReaderHierarchyFromImporterHierarchy(this.importer.serieImporter);
        for (SerieImporter overlaySerieImporter : overlayImporter.serieImporters) {
            this.serieReaders.add(HierarchyBuilder.buildReaderHierarchyFromImporterHierarchy(overlaySerieImporter));
        }
        firstTime = mainImporter.getReader().getStart();
        lastTime = mainImporter.getReader().getEnd();
        sequence = mainImporter.getReader().getSequence();
        raf = overlayImporter.openBinary("");
        masksInput = overlayImporter.openBinary(".mask");
        numLines = raf.readInt();

        readMasks();
    }

    @Override
    public OverlayLinesCollection getData(SpaceManager sp, int fromPixel, int toPixel) throws IOException {
        final int rangeCount = toPixel - fromPixel + 1 + 1;

        OverlayLinesCollection.Multipoint[] yData = new OverlayLinesCollection.Multipoint[rangeCount];
        long[] xData = new long[rangeCount];
        int j;
        int counter = 0;
        for (int p = fromPixel; p <= toPixel; p++) {
            if (counter == rangeCount) {
                //error;
                break;
            }
            yData[counter] = new OverlayLinesCollection.Multipoint(numLines);
            xData[counter] = sequence.toTime(moveTo(sp.toDataX(p)));
            for (j = 0; j < numLines; j++) {
                yData[counter].linePoints[j] = raf.readFloat();
            }
            counter++;
        }
        final int lastProbe = sp.getSequence().toProbeIndex(xData[counter - 1]) + 1;
        final long lastProbeTime = sp.getSequence().toTime(lastProbe);
        moveTo(lastProbeTime);
        yData[counter - 1] = new OverlayLinesCollection.Multipoint(numLines);
        xData[counter - 1] = lastProbeTime;
        for (j = 0; j < numLines; j++) {
            yData[counter - 1].linePoints[j] = raf.readFloat();
        }
//        for (int i = 0; i < rangeCount; i++)
//        {
//            multipoints[i] = new OverlayLinesCollection.Multipoint(numLines);
//            for (j = 0; j < numLines; j++)
//            {
//                multipoints[i].linePoints[j] = raf.readFloat();
//            }
//            raf.skipBytes(bytesToSkip);
//        }
        OverlayLinesCollection olc = new OverlayLinesCollection(yData, xData, new DataRange(xData[0], xData[xData.length - 1])); // przekażmy kolekcji jej początek oraz interwał
        return olc;
    }

    /**
     *
     * @param x
     * @return probe index of x
     * @throws IOException
     */
    public int moveTo(long x) throws IOException {
        if (x > lastTime) {
            x = lastTime;
        }
        final int probeIndex = sequence.toProbeIndex(x);
        //RandomA
        raf.seek(FRAME_START + FRAME_BYTES_Y * probeIndex * numLines);
        return probeIndex;
    }

    public ArrayList<Mask> getMasks() {
        return masks;
    }

    private void readMasks() throws IOException {
        //najpierw odczytaj ilość różnych masek
        int numMasks = masksInput.readInt();
        masks = new ArrayList<>(numMasks);
        for (int i = 0; i < numMasks; i++) {
            if (binding.getMask().get(i).getImportToSelection() != null) {
                continue; // nie czytamy, gdy jest zaimportowany do selection
            }
            int numItems = masksInput.readInt();
            Mask mask = new Mask(numItems);

            masks.add(mask);

            for (int j = 0; j < numItems; j++) {
                mask.addItem(masksInput.readLong(), masksInput.readLong());
            }
        }
        masksInput.close();
    }

    @Override
    public long getStart() {
        return firstTime;
    }

    @Override
    public long getEnd() {
        return lastTime;
    }

    @Override
    public DataSequence getSequence() {
        return sequence;
    }
}
