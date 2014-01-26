/**
 * komponuje w sobie readery danej serii pomiarowej (np pleth overlay 1) i
 * udostępnia renderery agregacji gdzie kluczem jest range i type
 */
package pl.adamborowski.medcharts.data;

/**
 *
 * @author adam
 */
public class SerieReader {

    public AggregationReader getAggregationReader(int range, AggregationType type) {
        return null;
    }

    public int getNumAggregationReaders() {
        return 0;
    }

    public AggregationReader getAggregationReader(int index) {
        return null;
    }
}
