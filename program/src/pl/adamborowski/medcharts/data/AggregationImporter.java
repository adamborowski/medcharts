/*
 ma metody process, flush, 
 */
package pl.adamborowski.medcharts.data;

import java.io.IOException;
import java.io.ObjectOutputStream;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.CacheFileManager;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.ACT;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.AVG;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MAX;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MED;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MIN;
import pl.adamborowski.medcharts.data.aggregationimporter.MaxImporter;
import pl.adamborowski.medcharts.data.aggregationimporter.MinImporter;

/**
 *
 * @author adam
 */
public class AggregationImporter {

    protected final AggregationDescription ad;
    protected final CacheFileManager cacheFileManager;
    private ObjectOutputStream output;

    public AggregationImporter(AggregationDescription ad, CacheFileManager cacheFileManager) {
        this.ad = ad;
        this.cacheFileManager = cacheFileManager;
    }

    public static AggregationImporter create(AggregationDescription ad, CacheFileManager cacheFileManager) {
        switch (ad.type) {
            case MIN:
                return new MinImporter(ad, cacheFileManager);
            case MAX:
                return new MaxImporter(ad, cacheFileManager);
            case ACT:
                break;
            case AVG:
                break;
            case MED:
                break;
            default:
                throw new AssertionError(ad.type.name());

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

    //static so we can use infix without importer reference
    public static String getInfix(AggregationDescription ad) {
        return '.'+ad.name.toLowerCase() ;
    }

    void begin(DataSequence ds) throws IOException {
        ad.startTime = ds.getStart();
        output = cacheFileManager.createCacheFile(getInfix(ad));
        output.writeObject(ad);
    }
}
