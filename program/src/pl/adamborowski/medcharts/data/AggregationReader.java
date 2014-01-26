/**
 * umożliwia iterację po konrentej agregacji (wczytuje z pliku) w danym podprzedziale czasowym (getData from to)
 */

package pl.adamborowski.medcharts.data;

import java.util.Iterator;

/**
 *
 * @author adam
 */
public class AggregationReader {

    private AggregationDescription description;
    private float[] data;

    public int getSampleIndex(long time) {
        return (int) ((time - description.startTime) / description.range);
    }

    public float getSample(long time) {
        return data[getSampleIndex(time)];
    }

    /**
     * ready for iterate object in specified range
     */
    public class DataCollection implements Iterable {

        public final long from;
        public final long to;

        public DataCollection(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Iterator iterator() {
            return new DataCollectionIterator();
        }

        public class DataCollectionIterator implements Iterator<Float> {

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }

            public DataCollectionIterator() {
            }

            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Float next() {
                //todo modify iteration element

                return 0f;
            }

        }

    }

    public DataCollection getData(long from, long to) {
        return new DataCollection(from, to);
    }
}
