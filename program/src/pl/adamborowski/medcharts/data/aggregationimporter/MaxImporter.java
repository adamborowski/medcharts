/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.data.aggregationimporter;

import java.io.IOException;
import pl.adamborowski.medcharts.data.CacheFileManager;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.AggregationImporter;

/**
 *
 * @author adam
 */
public class MaxImporter extends AggregationImporter {

    public MaxImporter(AggregationDescription ad, CacheFileManager cacheFileManager) {
        super(ad, cacheFileManager);
    }

    private float maxValue = -Float.MAX_VALUE;
    int a=0;
    @Override
    protected void processSample(float value) {
        if (value > maxValue) {
            maxValue = value;
        }
        a++;
    }

    @Override
    protected void flush() throws IOException {
        output.writeFloat(maxValue);
        maxValue=-Float.MAX_VALUE;
    }

}
