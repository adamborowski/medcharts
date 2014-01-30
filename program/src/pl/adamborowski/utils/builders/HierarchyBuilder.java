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
import java.util.List;
import pl.adamborowski.medcharts.assembly.jaxb.Aggregations;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.AggregationImporter;
import pl.adamborowski.medcharts.data.AggregationReader;
import pl.adamborowski.medcharts.data.CacheFileManager;
import pl.adamborowski.medcharts.data.SerieImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.data.SerieRenderer;
import pl.adamborowski.medcharts.data.aggregationimporter.ActImporter;
import pl.adamborowski.medcharts.data.aggregationreader.ActReader;
import pl.adamborowski.medcharts.data.aggregationrenderer.LineRenderer;
import pl.adamborowski.medcharts.data.aggregationrenderer.MinMaxRenderer;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.utils.ParseUtil;

/**
 *
 * @author adam
 */
public class HierarchyBuilder {

    public static SerieImporter buildSerieImporterFromJaxb(CacheFileManager cacheFileManager, Serie serie) {
        SerieImporter serieImporter = new SerieImporter(cacheFileManager);
        if (serie.getAggregations() != null) {
            for (Aggregations.Aggregation aggregation : serie.getAggregations().getAggregation()) {
                List<AggregationDescription.Type> types = ParseUtil.parseTypes(aggregation.getType());
                for (AggregationDescription.Type type : types) {
                    serieImporter.addAggregation(new AggregationDescription(type.name() + aggregation.getRange(), type, ParseUtil.parseRange(aggregation.getRange())));
                }
            }
        }
        final AggregationDescription actAggregationImporter = new AggregationDescription("act", AggregationDescription.Type.ACT, 0);
        serieImporter.addAggregation(actAggregationImporter);
        return serieImporter;
    }

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

    public static SerieRenderer buildRendererHierarchyFromReaderHierarchy(SerieReader reader, SpaceManager sp) {
        Collection<AggregationReader> aggregations = reader.getAggregationReaders();
        SerieRenderer sr = new SerieRenderer(reader, sp);
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
        System.out.println("num aggegation renderrers; "+minReaders.size());
        sr.initAutomation();
        return sr;

    }
}
