/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.data.aggregationreader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import pl.adamborowski.medcharts.data.AggregationReader;

/**
 *
 * @author adam
 */
public class ActReader extends AggregationReader {

    @Override
    public DataCollection getData(long from, long to) {
        return new DataCollection(from, to);
    }

    @Override
    public float getSample(long time) {
        return data[config.sequence.toProbeIndex(time)];
    }

    @Override
    public int getSampleIndex(long time) {
        return config.sequence.toProbeIndex(time);
    }

    @Override
    public long getEnd() {
        return config.sequence.toTime(data.length - 1);
    }

    @Override
    public long getStart() {
        return super.getStart(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRange() {
        return -1;
    }

    public ActReader(ObjectInputStream input, ObjectInputStream configInput) throws IOException {
        super(input, configInput);
    }

    /**
     * ready for iterate object in specified range
     */
    public class DataCollection extends AggregationReader.DataCollection {

        public DataCollection(long from, long to) {
            super(from, to);
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
                iterator.sampleIndex = getSampleIndex(from) - 1;
            }

            public boolean hasNext() {
                return iterator.sampleIndex < toIndex;
            }

            @Override
            public Point next() {
                iterator.i++;
                iterator.sampleIndex++;
                iterator.time =config.sequence.toTime(iterator.sampleIndex);
                iterator.value = data[iterator.sampleIndex];
                return iterator;
            }

        }

    }
}
