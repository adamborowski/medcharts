/*
 KLasa przeznaczona do renderowania konkretnej AggregationReader na cały czas życia aplikaci
 */
package pl.adamborowski.medcharts.data;

import java.awt.Graphics2D;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 * Class for rendering concrete aggregation, i.e. renderer of 'min10s'
 *
 * @author adam
 */
public class AggregationRenderer {

    protected final AggregationReader reader;
    protected final SpaceManager sp;

    public AggregationRenderer(AggregationReader reader, SpaceManager sp) {
        this.reader = reader;
        this.sp = sp;
    }

    public void render(Graphics2D g) {
        DataRange newRange = new DataRange(sp.fitToSequence(sp.visibleData.firstAccessible), Math.min(sp.fitToSequence(reader.getEnd()), sp.getSequence().moveTime(sp.visibleData.lastAccessible, 2)), (int) (float) Math.ceil(sp.getScaleX()));//todo zmieniono +interval na nexttime
        long timeIncrement = reader.getRange();
        long time = reader.getStart();
        float tx = sp.toDisplayX(newRange.getStart());
        float ty = sp.toDisplayY(reader.getSample(newRange.getStart()));
        float nx = 0, ny = 0;
        int i = 0;
        try {
            for (AggregationReader.Point point : reader.getData(newRange.getStart(), newRange.getEnd())) {
                i++;
                time += timeIncrement;
                nx = sp.toDisplayX(point.time);
                ny = sp.toDisplayY(point.value);
                g.drawLine((int) tx, (int) ty, (int) nx, (int) ny);
                tx = nx;
                ty = ny;
            }
        } catch (Exception e) {
            System.out.println("exception");
        }
        System.out.println("i:" + i + " - " + sp.toDisplayX(newRange.getEnd()));
    }

}
