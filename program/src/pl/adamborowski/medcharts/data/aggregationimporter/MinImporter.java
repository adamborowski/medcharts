/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.adamborowski.medcharts.data.aggregationimporter;

import pl.adamborowski.medcharts.assembly.imporing.CacheFileManager;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.AggregationImporter;

/**
 *
 * @author adam
 */
public class MinImporter extends AggregationImporter
{

    public MinImporter(AggregationDescription ad, CacheFileManager cacheFileManager) {
        super(ad, cacheFileManager);
    }
    
}
