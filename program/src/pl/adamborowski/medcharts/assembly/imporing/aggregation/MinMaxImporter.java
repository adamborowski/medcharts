/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing.aggregation;

import java.io.Serializable;
import java.util.ArrayList;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.AggregationImporter;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;

/**
 *
 * @author adam
 */
public class MinMaxImporter extends AggregationImporter {

    public MinMaxImporter(Assembly.Serie.Aggregations.Aggregation aggregation) {
        super(aggregation);
    }

    private float min = Float.MAX_VALUE;
    private float max = Float.MIN_VALUE;

    public static class MinMax implements Serializable {

        public float min;
        public float max;

        public MinMax(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }
    private ArrayList<MinMax> data;

    @Override
    public void init(DataSequence sequence, int estimatedNumProbes) {
        super.init(sequence, estimatedNumProbes); 
        final int initialCapacity = (int) (estimatedNumProbes / this.flushStep);
        data = new ArrayList<>(initialCapacity);
    }

    @Override
    public void processSample(float value) {
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
    }

    @Override
    protected Object[] getResult() {
        return data.toArray();
    }

    @Override
    public void flush() {
        data.add(new MinMax(min, max));
        min = Float.MAX_VALUE;
        max = -Float.MAX_VALUE;
    }

}
