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
public class LineRenderer extends AggregationRenderer {

    private AggregationReader reader;
    private DataRange newRange;

    public LineRenderer(SpaceManager sp, AggregationReader reader) {
        super(sp);
        this.reader = reader;
    }

    @Override
    public void render(Graphics2D g) {

        float tx = sp.toDisplayX(newRange.getStart());
        float ty = sp.toDisplayY(reader.getSample(newRange.getStart()));
        float nx = 0, ny = 0;
        int i = 0;
        for (AggregationReader.Point point : reader.getData(newRange.getStart(), newRange.getEnd())) {
            i++;
            nx = sp.toDisplayX(point.time);
            ny = sp.toDisplayY(point.value);
            g.drawLine((int) tx, (int) ty, (int) nx, (int) ny);
            tx = nx;
            ty = ny;
        }
    }

    @Override
    public void calculateExtremum(DataRendererBase.Extremum extremum) {
        newRange = new DataRange(sp.fitToSequence(sp.visibleData.firstAccessible), Math.min(sp.fitToSequence(reader.getEnd()), sp.getSequence().moveTime(sp.visibleData.lastAccessible, 2)), (int) (float) Math.ceil(sp.getScaleX()));//todo zmieniono +interval na nexttime
        for (AggregationReader.Point point : reader.getData(newRange.getStart(), newRange.getEnd())) {
            if (point.value > extremum.max) {
                extremum.max = point.value;
            } else if (point.value < extremum.min) {
                extremum.min = point.value;
            }
        }
    }

}
