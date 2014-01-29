/**
 * umożliwia iterację po konrentej agregacji (wczytuje z pliku) w danym
 * podprzedziale czasowym (getData from to)
 */
package pl.adamborowski.medcharts.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adam
 */
public class AggregationReader {

    protected final ObjectInputStream configInput;
    protected final ObjectInputStream input;
    protected AggregationDescription description;
    protected float[] data;
    protected AggregationDescription ad;

    public AggregationReader(ObjectInputStream input, ObjectInputStream configInput) throws IOException {
        this.input = input;
        this.configInput = configInput;
        parseInput();
    }

    public int getRange() {
        return config.ad.range;
    }

    public long getStart() {
        return config.ad.startTime;
    }

    public long getEnd() {
        return getStart() + config.numSamples * config.ad.range;
    }

    public AggregationDescription.Type getType() {
        return config.ad.type;
    }

    public int getSampleIndex(long time) {
        if (description.range == 0) {
            throw new RuntimeException("zero range");
        }
        return (int) ((time - description.startTime) / description.range);
    }

    public float getSample(long time) {
        return data[getSampleIndex(time)];
    }
    protected AggregationImporter.AggregationConfig config;

    protected void parseInput() throws IOException {

        try {
            config = (AggregationImporter.AggregationConfig) configInput.readObject();
            ad = config.ad;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("corrupted binary file");
        }
        final int numSamples = config.numSamples;
        data = new float[numSamples];
        for (int i = 0; i < numSamples; i++) {
            data[i] = input.readFloat();
        }
        input.close();
        configInput.close();
        description = config.ad;

    }

    public DataCollection getData(long from, long to) {
        return new DataCollection(from, to);
    }

    public class Point {

        public long time;
        public float value;
        public int i;

        @Override
        public String toString() {
            return "Point{" + "time=" + time + ", value=" + value + ", i=" + i + ", sampleIndex=" + sampleIndex + '}';
        }
        public int sampleIndex;
    }

    /**
     * ready for iterate object in specified range
     */
    public class DataCollection implements Iterable<Point> {

        public final long from;
        public final long to;
        protected final int toIndex;
        protected final int fromIndex;

        public DataCollection(long from, long to) {
            this.from = from;
            this.to = to;
            fromIndex = getSampleIndex(from);
            toIndex = getSampleIndex(to);
        }

        @Override
        public Iterator<Point> iterator() {
            return new DataCollectionIterator();
        }

        public class DataCollectionIterator implements Iterator<Point> {

            protected final Point iterator;

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }

            public DataCollectionIterator() {
                iterator = new Point();
                iterator.i = -1;
                iterator.sampleIndex = getSampleIndex(from)-1;
                iterator.time=from-ad.range;
            }

            @Override
            public boolean hasNext() {
                return iterator.sampleIndex < toIndex;
            }

            @Override
            public Point next() {
                iterator.i++;
                iterator.sampleIndex++;
                iterator.time += ad.range;
                iterator.value = data[iterator.sampleIndex];
                return iterator;
            }

        }

    }
}
