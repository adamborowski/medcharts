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
public class ActImporter extends AggregationImporter {

    public ActImporter(AggregationDescription ad, CacheFileManager cacheFileManager) {
        super(ad, cacheFileManager);
    }

    @Override
    public void process(float value) throws IOException {
        output.writeFloat(value);//we not need to process-and-flush step, write all vaues into array
        config.numSamples++;
    }

    @Override
    protected void flush() {
        //we not need flushing
        config.numSamples--;//fix because aggregationImporter.save calls flush and increases numSaples
    }

    @Override
    protected void processSample(float value) {
        throw new UnsupportedOperationException("Not supported for act type."); //To change body of generated methods, choose Tools | Templates.
    }

}
