/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.aggregation.MinMaxImporter;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;

/**
 *
 * @author adam
 */
public abstract class AggregationImporter {

    public static AggregationImporter create(Assembly.Serie.Aggregations.Aggregation aggregation) {
        Type type = Type.valueOf(aggregation.getType());
        switch (type) {
            case minmax:
                return new MinMaxImporter(aggregation);
            case average:
                break;
            case min:
                break;
            case max:
                break;
            default:
                throw new AssertionError(type.name());

        }
        throw new RuntimeException("trzeba podać dobry typ agregacji");
    }
    private int fromScale;
    protected float flushStep;

    public enum Type {

        minmax, average, min, max
    };
    /**
     * in milliseconds, number of msc per pixel
     */
    private int range;
    private Type type;
    String name;

    private void parseRange(String range) {
        Pattern reg = Pattern.compile("(\\d+)(ms|s|m|h)+?");
        name = range;
        Matcher m = reg.matcher(range);
        final int second = 1000, minute = 60 * second, hour = 60 * minute;
        int num;
        String unit;
        this.range = 0;
        while (m.find()) {
            num = Integer.valueOf(m.group(1));
            unit = m.group(2).toLowerCase();
            switch (unit) {
                case "s":
                    num *= second;
                    break;
                case "ms":
                    num *= 1;
                    break;
                case "m":
                    num *= minute;
                    break;
                case "h":
                    num *= hour;
                    break;
            }
            this.range += num;
        }
    }

    public AggregationImporter(Assembly.Serie.Aggregations.Aggregation aggregation) {
        this.type = Type.valueOf(aggregation.getType());
        parseRange(aggregation.getRange());
        final String scl = aggregation.getFromScale();
        this.fromScale = scl.equals("auto") ? -1 : Integer.valueOf(scl);
    }

    public void init(DataSequence sequence, int estimatedNumProbes) {
        if (fromScale == -1) {
            //auto calculate fromScale based on sequence length
            //skala = ile milisekund na piksel
            fromScale = range;
        }

        flushStep = (float) range / sequence.getSequenceLength() * sequence.getSequenceCount();
        if (flushStep <1) {
            flushStep = 1;
        }
        System.out.println("numprobesperaggr " + flushStep);
    }
    int counter = 0;

    public final void process(float value) {
        processSample(value);
        counter++;
        if (counter == flushStep) {
            //aggregation is ready to flush and new data will be acquisited
            this.flush();
            //reset flush counter
            counter = 0;
        }
    }

    public abstract void processSample(float value);

    public static class DataProvider implements Serializable {

        public Type type;
        public int range;
        public float samplesPerAggregation;
        Object[] data;
        public String name;

        public Object getAggregation(int index) {
//            System.out.println("aggregation get ratio:// sample: " + sample + " data i: " + (sample / samplesPerAggregation) + " data ength:" + data.length);
            return data[(int) (index)];
        }

        public boolean isEmpty() {

            return data.length == 0;
        }

    }

    public DataProvider getDataProvider() {
        flush();//flush last not full aggregation
        DataProvider dp = new DataProvider();
        dp.type = type;
        dp.range = range;
        dp.samplesPerAggregation = flushStep;
        dp.data = getResult();
        dp.name = name;
        return dp;
    }

    protected abstract Object[] getResult();

    public abstract void flush();

    @Override
    public String toString() {
        return "AggregationImporter{" + "fromScale=" + fromScale + ", range=" + range + ", type=" + type + '}';
    }

}
