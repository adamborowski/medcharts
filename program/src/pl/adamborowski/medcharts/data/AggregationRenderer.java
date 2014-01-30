/*
 KLasa przeznaczona do renderowania konkretnej AggregationReader na cały czas życia aplikaci
 */
package pl.adamborowski.medcharts.data;

import java.awt.Graphics2D;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.renderers.DataRendererBase;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 * Class for rendering concrete aggregation, i.e. renderer of 'min10s'
 *
 * @author adam
 */
public class AggregationRenderer {

    protected final SpaceManager sp;

    public AggregationRenderer(SpaceManager sp) {
        this.sp = sp;
    }

    public void render(Graphics2D g) {

    }

    public void calculateExtremum(DataRendererBase.Extremum extremum) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
