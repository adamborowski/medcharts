/*
 KLasa przeznaczona do renderowania konkretnej AggregationReader na cały czas życia aplikaci
 */
package pl.adamborowski.medcharts.data;

import java.awt.Color;
import java.awt.Graphics2D;
import pl.adamborowski.medcharts.MedChartPreferences;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.renderers.DataRendererBase;
import pl.adamborowski.medcharts.renderers.LabelRenderer;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.utils.DateUtil;
import pl.adamborowski.utils.PaintUtil;

/**
 * Class for rendering concrete aggregation, i.e. renderer of 'min10s'
 *
 * @author adam
 */
public abstract class AggregationRenderer {

    protected final SpaceManager sp;
    protected int numSamplesRendered;
    protected int range;

    public int getRange() {
        return range;
    }

    public int getNumSamplesRendered() {
        return numSamplesRendered;
    }

    public AggregationRenderer(SpaceManager sp) {
        this.sp = sp;
        hintRenderer.setBackgroundColor(PaintUtil.createColor(0xfcfcfc, 0.94f));
        hintRenderer.setRadius(6);
    }

    public void render(Graphics2D g, boolean drawBalls) {

    }

    public void calculateExtremum(DataRendererBase.Extremum extremum) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void renderHintImpl(Graphics2D g, int mouseDisplayX, int mouseDisplayY, AggregationReader reader, int positionPreference, boolean isUnderMouse, Color color) {
        try {
            int bottomPosition = sp.getHeight() - 25;
            int preferredY = bottomPosition;
            int preferredX = mouseDisplayX;

            long mouseDataX = sp.fixExceedingTime(sp.toDataX((float) mouseDisplayX));
            float mouseDataY = reader.getSample(mouseDataX);
            mouseDataX = sp.fixExceedingTime(sp.getSequence().fitTime(mouseDataX));
            int bestX = (int) sp.toDisplayX(mouseDataX);
            int bestY = (int) sp.toDisplayY(mouseDataY);
            if (sp.getHeight() > 90) {
                switch (positionPreference) {
                    case MedChartPreferences.HINT_POSITION_BOTTOM:
                        preferredY = bottomPosition;
                        break;
                    case MedChartPreferences.HINT_POSITION_TOP:
                        preferredY = 20;
                        break;
                    case MedChartPreferences.HINT_POSITION_MOUSE:
                        if (isUnderMouse) {
                            preferredY = mouseDisplayY + 15;
                            preferredX += 10;
                        }
                        break;
                    default: // (value)
                        preferredY = bestY + 5;
                        preferredX = bestX + 5;
                        break;
                }
            }
            g.setPaint(Color.red);
            final int r = 4;
            final int r2 = r * 2;
            g.drawArc(bestX - r, bestY - r, r2, r2, 0, 360);
            final int margin = 3;
            int xx = preferredX;

            if (xx < margin) {
                xx = margin;
            }
            if (xx > sp.getVisibleWidth() - 200 - margin) {
                xx = sp.getVisibleWidth() - 200 - margin;
            }
            hintRenderer.setForegroundColor(color);
            hintRenderer.setForegroundColor(color);
        hintRenderer.setBorderColor(color);
            hintRenderer.setX(xx);
            hintRenderer.setPadding(3);
            hintRenderer.setText(reader.ad.type.name() + " " + String.format("%s = %g", DateUtil.stringify(mouseDataX), mouseDataY));
            if (preferredY <= margin) {
                preferredY = margin;
            } else if (preferredY > bottomPosition) {
                preferredY = bottomPosition;
            }
            hintRenderer.setY(preferredY);
            hintRenderer.render(g);
        } catch (Exception e) {
            //TODO NAPRAWIĆ!
        }

    }
    private LabelRenderer hintRenderer = new LabelRenderer("", 0, 0);

    public abstract void renderHint(Graphics2D g, int mouseDisplayX, int mouseDisplayY, int positionPreference, boolean isUnderMouse, Color color);

}
