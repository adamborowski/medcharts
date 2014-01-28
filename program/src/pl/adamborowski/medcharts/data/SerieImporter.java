/**
 * podczas imoprtowania konkretnego pliku zodziea pracę pomiędzy aggregation
 * importerów czyli np 3 agregacje: act min|max 10s min|max 1m
 *
 * robi z tego 5 aggregation importerów: min10s, max10s, min1m, max1m, act
 */
package pl.adamborowski.medcharts.data;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.imporing.CacheFileManager;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;

/**
 *
 * @author adam
 */
public class SerieImporter {

    private final CacheFileManager cacheFileManager;
    private final ArrayList<AggregationImporter> aggregations;

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
            if (!cacheFileManager.isCacheFileValid(AggregationImporter.getInfix(a.ad))) {
                return false;
            }
        }
        return true;
    }

    public void begin(DataSequence ds) throws IOException {
        for (AggregationImporter a : aggregations) {
            a.begin(ds);
        }
    }

    public void process(float currentValue) {
        for(AggregationImporter a:aggregations)
        {
            a.process(currentValue);
        }
    }

}
