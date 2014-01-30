/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.data.aggregationrenderer;

import java.awt.Graphics2D;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.data.AggregationReader;
import pl.adamborowski.medcharts.data.AggregationRenderer;
import pl.adamborowski.medcharts.renderers.DataRendererBase;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 *
 * @author adam
 */
public class MinMaxRenderer extends AggregationRenderer {

    private AggregationReader maxReader;
    private AggregationReader minReader;
    private DataRange newRange;

    public MinMaxRenderer(SpaceManager sp, AggregationReader maxReader, AggregationReader minReader) {
        super(sp);
        this.maxReader = maxReader;
        this.minReader = minReader;
    }

    final int maxSamples = 10000;
    int xPoints[] = new int[maxSamples * 2];
    int yPoints[] = new int[maxSamples * 2];

    int lastNumPoints = 0;

    @Override
    public void render(Graphics2D g) {
//        newRange = new DataRange(sp.fitToSequence(sp.visibleData.firstAccessible), Math.min(sp.fitToSequence(minReader.getEnd()), sp.getSequence().moveTime(sp.visibleData.lastAccessible, 2)), (int) (float) Math.ceil(sp.getScaleX()));//todo zmieniono +interval na nexttime
        int numMaxPoints = 0;
        for (AggregationReader.Point point : maxReader.getData(newRange.getStart(), newRange.getEnd())) {
            xPoints[numMaxPoints] = (int) sp.toDisplayX(point.time);
            yPoints[numMaxPoints] = (int) sp.toDisplayY(point.value);
            numMaxPoints++;
        }
        int lastSampleIndex = numMaxPoints * 2 - 1;
        int numMinPoints = 0;
        for (AggregationReader.Point point : minReader.getData(newRange.getStart(), newRange.getEnd())) {
            xPoints[lastSampleIndex - numMinPoints] = (int) sp.toDisplayX(point.time);
            yPoints[lastSampleIndex - numMinPoints] = (int) sp.toDisplayY(point.value);
            numMinPoints++;
        }
        int totalNumPoints = numMaxPoints + numMinPoints;
        g.fillPolygon(xPoints, yPoints, totalNumPoints);
        g.drawPolygon(xPoints, yPoints, totalNumPoints);
        if (lastNumPoints != totalNumPoints) {
            lastNumPoints = totalNumPoints;
//            System.out.println("minmax: " + minReader.getType().name() + " total points: " + (totalNumPoints));
        }
    }

    @Override
    public void calculateExtremum(DataRendererBase.Extremum extremum) {
        newRange = new DataRange(sp.fitToSequence(sp.visibleData.firstAccessible), Math.min(sp.fitToSequence(maxReader.getEnd()), sp.getSequence().moveTime(sp.visibleData.lastAccessible, 2)), (int) (float) Math.ceil(sp.getScaleX()));//todo zmieniono +interval na nexttime
        for (AggregationReader.Point point : maxReader.getData(newRange.getStart(), newRange.getEnd())) {
            if (point.value > extremum.max) {
                extremum.max = point.value;
            }
        }
        for (AggregationReader.Point point : minReader.getData(newRange.getStart(), newRange.getEnd())) {
            if (point.value < extremum.min) {
                extremum.min = point.value;
            }
        }
    }
}
