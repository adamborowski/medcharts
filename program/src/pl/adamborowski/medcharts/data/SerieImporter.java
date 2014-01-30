/**
 * podczas imoprtowania konkretnego pliku zodziea pracę pomiędzy aggregation
 * importerów czyli np 3 agregacje: act min|max 10s min|max 1m
 *
 * robi z tego 5 aggregation importerów: min10s, max10s, min1m, max1m, act
 */
package pl.adamborowski.medcharts.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import pl.adamborowski.medcharts.assembly.data.DataSequence;

/**
 *
 * @author adam
 */
public class SerieImporter {

    public final CacheFileManager cacheFileManager;
    private final ArrayList<AggregationImporter> aggregations;
    private ObjectOutputStream configFile;

    public SerieImporter(CacheFileManager cacheFileManager) {
        this.aggregations = new ArrayList<>(10);
        this.cacheFileManager = cacheFileManager;
    }

    public void addAggregation(AggregationDescription ad) {
        this.aggregations.add(AggregationImporter.create(ad, cacheFileManager));
    }

    public boolean isCacheValid() {
        if (!cacheFileManager.isCacheFileValid("")) {
            return false;
        }
        for (AggregationImporter a : aggregations) {
            if (!a.isCacheValid()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<AggregationImporter> getAggregations() {
        return aggregations;
    }

    public void begin(DataSequence ds) throws IOException {
        config.dataSequence = ds;
        for (AggregationImporter a : aggregations) {
            a.begin(ds);
        }
    }

    public void process(long time, float currentValue) throws IOException {
        for (AggregationImporter a : aggregations) {
            a.process(time, currentValue);
        }
        if (currentValue > config.maxValue) {
            config.maxValue = currentValue;
        }
        if (currentValue < config.minValue) {
            config.minValue = currentValue;
        }

    }

    public final void save() throws IOException {
        for (AggregationImporter a : aggregations) {
            a.save();
        }
        this.configFile = cacheFileManager.createCacheFile(".config");
        configFile.writeObject(config);
        configFile.close();
    }

    public static class SerieConfig implements Serializable {

        public DataSequence dataSequence;
        public float maxValue = -Float.MAX_VALUE;//defines boundaries of data
        public float minValue = Float.MAX_VALUE;
    }
    protected final SerieConfig config = new SerieConfig();

    public ObjectInputStream openCacheConfigFile() throws IOException {
        return cacheFileManager.openCacheFile(".config");
    }

}
