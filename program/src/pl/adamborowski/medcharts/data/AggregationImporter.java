/*
 ma metody process, flush, 
 */
package pl.adamborowski.medcharts.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.ACT;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.AVG;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MAX;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MED;
import static pl.adamborowski.medcharts.data.AggregationDescription.Type.MIN;
import pl.adamborowski.medcharts.data.aggregationimporter.ActImporter;
import pl.adamborowski.medcharts.data.aggregationimporter.MaxImporter;
import pl.adamborowski.medcharts.data.aggregationimporter.MinImporter;

/**
 *
 * @author adam
 */
public abstract class AggregationImporter {

    public static AggregationImporter create(AggregationDescription ad, CacheFileManager cacheFileManager) {
        switch (ad.type) {
            case MIN:
                return new MinImporter(ad, cacheFileManager);
            case MAX:
                return new MaxImporter(ad, cacheFileManager);
            case ACT:
                return new ActImporter(ad, cacheFileManager);
            case AVG:
                break;
            case MED:
                break;
            default:
                throw new AssertionError(ad.type.name());

        }
        return null;
    }

    //static so we can use infix without importer reference
    public static String getInfix(AggregationDescription ad) {
        return '.' + ad.name.toLowerCase();
    }

    public static String getConfigInfix(AggregationDescription ad) {
        return getInfix(ad) + ".config";
    }
    private ObjectOutputStream configOutput;

    public final AggregationDescription ad;
    public final CacheFileManager cacheFileManager;
    protected ObjectOutputStream output;

    private int aggregationSize;
    private int currentAggregationSize = 0;
    protected final AggregationConfig config = new AggregationConfig();

    public AggregationImporter(AggregationDescription ad, CacheFileManager cacheFileManager) {
        this.ad = ad;
        config.ad=ad;
        this.cacheFileManager = cacheFileManager;
    }

    public AggregationReader createReader() throws IOException {
        return new AggregationReader(cacheFileManager.openCacheFile(getInfix(ad)), cacheFileManager.openCacheFile(getConfigInfix(ad)));
    }

    public void process(float value) throws IOException {
        processSample(value);
        currentAggregationSize++;
        if (currentAggregationSize == aggregationSize) {
            //flushing
            flush();
            currentAggregationSize = 0;
            config.numSamples++;
            //end of flushing
        }
    }

    protected abstract void processSample(float value);

    protected abstract void flush() throws IOException;

    public final void save() throws IOException {
        //flushing
        flush();
        currentAggregationSize = 0;
        config.numSamples++;
        //end of flushing
        output.close();
        configOutput.writeObject(config);//last int is total number of samples
        configOutput.close();
        System.out.println("Aggregation "+ad.name+": "+config.numSamples+" samples.");
    }

    public final void begin(DataSequence ds) throws IOException {
        ad.startTime = ds.getStart();
        output = cacheFileManager.createCacheFile(getInfix(ad));
        configOutput = cacheFileManager.createCacheFile(getConfigInfix(ad));
        if (ad.range > 0)//ad.range=0 where ACT
        {
            aggregationSize = (int) ((float) ad.range / ds.getSequenceLength() * ds.getSequenceCount());
            if (aggregationSize < 1) {
                aggregationSize = 1;
            }
        }
        config.sequence=ds;
    }

    boolean isCacheValid() {
        return cacheFileManager.isCacheFileValid(getInfix(ad))
                && cacheFileManager.isCacheFileValid(getConfigInfix(ad));
    }

    public static class AggregationConfig implements Serializable {

        public AggregationDescription ad;
        public int numSamples = 0;
        public DataSequence sequence;
    }
    
    public ObjectInputStream openCacheFile() throws IOException{
        return cacheFileManager.openCacheFile(getInfix(ad));
    }
    
    public ObjectInputStream openCacheConfigFile() throws IOException{
        return cacheFileManager.openCacheFile(getConfigInfix(ad));
    }
}
