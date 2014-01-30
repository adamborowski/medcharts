/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.AggregationImporter;
import pl.adamborowski.medcharts.data.AggregationReader;
import pl.adamborowski.medcharts.data.AggregationRenderer;
import pl.adamborowski.medcharts.data.SerieImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.data.SerieRenderer;
import pl.adamborowski.medcharts.data.aggregationimporter.ActImporter;
import pl.adamborowski.medcharts.data.aggregationreader.ActReader;
import pl.adamborowski.medcharts.data.aggregationrenderer.LineRenderer;
import pl.adamborowski.medcharts.data.aggregationrenderer.MinMaxRenderer;
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
        //collect min readers, max readers, and act reader to group by range
        HashMap<Integer, AggregationReader> minReaders = new HashMap<>();
        HashMap<Integer, AggregationReader> maxReaders = new HashMap<>();
        ActReader actReader = null;
        for (AggregationReader a : aggregations) {
            if (a instanceof ActReader) {
                actReader = (ActReader) a;
            } else if (a.getType() == AggregationDescription.Type.MIN) {
                minReaders.put(a.getRange(), a);
            } else if (a.getType() == AggregationDescription.Type.MAX) {
                maxReaders.put(a.getRange(), a);
            }
//            AggregationRenderer aggregationRenderer = new AggregationRenderer(a, sp);
//            sr.addAggregationRenderer(aggregationRenderer);c
        }

        sr.addAggregationRenderer(actReader.getRange(), new LineRenderer(sp, actReader));

        for (AggregationReader minReader : minReaders.values()) {
            AggregationReader maxReader = maxReaders.get(minReader.getRange());
            sr.addAggregationRenderer(minReader.getRange(), new MinMaxRenderer(sp, maxReader, minReader));
        }

        sr.initAutomation();
        return sr;

    }
}
