/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.AggregationImporter;
import pl.adamborowski.medcharts.data.AggregationReader;
import pl.adamborowski.medcharts.data.AggregationRenderer;
import pl.adamborowski.medcharts.data.SerieImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.data.SerieRenderer;
import pl.adamborowski.medcharts.data.aggregationimporter.ActImporter;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 *
 * @author adam
 */
public class HierarchyBuilder {

    public static SerieReader buildReaderHierarchyFromImporterHierarchy(SerieImporter importer) throws IOException {
        ArrayList<AggregationImporter> aggregations = importer.getAggregations();
        SerieReader sr = new SerieReader(importer.openCacheConfigFile());
        for (AggregationImporter a : aggregations) {

            if (a instanceof ActImporter) {
                AggregationReader reader = sr.addActAggregationReader(a.openCacheFile(), a.openCacheConfigFile());
                sr.actAggregationReader = reader;
            } else {
                AggregationReader reader = sr.addAggregationReader(a.openCacheFile(), a.openCacheConfigFile());
            }
        }
        return sr;
    }

    public static SerieRenderer buildRendererHierarchyFromReaderHierarchy(SerieReader reader, SpaceManager sp, Assembly.Serie jaxb) {
        Collection<AggregationReader> aggregations = reader.getAggregationReaders();
        SerieRenderer sr = new SerieRenderer(reader, jaxb, sp);
        for (AggregationReader a : aggregations) {
            AggregationRenderer aggregationRenderer = new AggregationRenderer(a, sp);
            sr.addAggregationRenderer(aggregationRenderer);
        }
        sr.initAutomation();
        return sr;

    }
}
