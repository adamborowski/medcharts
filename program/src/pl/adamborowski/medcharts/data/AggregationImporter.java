/*
 ma metody process, flush, 
 */
package pl.adamborowski.medcharts.data;

import pl.adamborowski.medcharts.data.AggregationDescription.AggregationType;
import static pl.adamborowski.medcharts.data.AggregationDescription.AggregationType.ACT;
import static pl.adamborowski.medcharts.data.AggregationDescription.AggregationType.AVG;
import static pl.adamborowski.medcharts.data.AggregationDescription.AggregationType.MAX;
import static pl.adamborowski.medcharts.data.AggregationDescription.AggregationType.MED;
import static pl.adamborowski.medcharts.data.AggregationDescription.AggregationType.MIN;

/**
 *
 * @author adam
 */
public class AggregationImporter {

    public static AggregationImporter create(AggregationType type) {
        switch (type) {
            case MIN:
                break;
            case MAX:
                break;
            case ACT:
                break;
            case AVG:
                break;
            case MED:
                break;
            default:
                throw new AssertionError(type.name());

        }
        return null;
    }

    private int samplesPerAggregation;
    private int samplesCounter = 0;

    public void process(float value) {

        samplesCounter++;
        if (samplesCounter == samplesPerAggregation) {
            flush();
            samplesCounter = 0;
        }
    }

    protected void flush() {

    }

    public void save() {
        flush();
    }
}
