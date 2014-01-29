/**
 * komponuje w sobie readery danej serii pomiarowej (np pleth overlay 1) i
 * udostępnia renderery agregacji gdzie kluczem jest range i type
 */
package pl.adamborowski.medcharts.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import pl.adamborowski.medcharts.data.aggregationreader.ActReader;

/**
 *
 * @author adam
 */
public class SerieReader {

    public final SerieImporter.SerieConfig config;

    public SerieReader(ObjectInputStream configStream) throws IOException {
        try {
            config = (SerieImporter.SerieConfig) configStream.readObject();
            configStream.close();

        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    public AggregationReader actAggregationReader;

    private static class MapKey {

        int range;
        AggregationDescription.Type type;

        public MapKey(int range, AggregationDescription.Type type) {
            this.range = range;
            this.type = type;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + this.range;
            hash = 73 * hash + Objects.hashCode(this.type);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MapKey other = (MapKey) obj;
            if (this.range != other.range) {
                return false;
            }
            if (this.type != other.type) {
                return false;
            }
            return true;
        }
    }
    private final Map<MapKey, AggregationReader> map = new HashMap<>(7);

    public AggregationReader getAggregationReader(int range, AggregationDescription.Type type) {
        return map.get(new MapKey(range, type));
    }

    public int getNumAggregationReaders() {
        return map.values().size();
    }

    public Collection<AggregationReader> getAggregationReaders() {
        return map.values();
    }

    public AggregationReader getFitAggregationReader(int range) {
        return null;
    }

    public AggregationReader addAggregationReader(ObjectInputStream dataStream, ObjectInputStream configStream) throws IOException {
        AggregationReader aggregationReader = new AggregationReader(dataStream, configStream);
        map.put(new MapKey(aggregationReader.getRange(), aggregationReader.getType()), aggregationReader);
        return aggregationReader;
    }
    public AggregationReader addActAggregationReader(ObjectInputStream dataStream, ObjectInputStream configStream) throws IOException {
        AggregationReader aggregationReader = new ActReader(dataStream, configStream);
        map.put(new MapKey(aggregationReader.getRange(), aggregationReader.getType()), aggregationReader);
        return aggregationReader;
    }

}
