/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.Viewport;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter.Mapping;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import pl.adamborowski.utils.FontUtil;

/**
 * klasa która trzyma:axis renderer, grid renderer, serie renderer
 *
 * @author test
 */
public final class RootRenderer extends ViewRendererBase
{

    private MouseSelectionRenderer msr;
    private DataSelectionMaskRenderer dsmr;
    private final HelpRenderer helpRenderer;

    public void setDsmr(DataSelectionMaskRenderer dsmr)
    {
        this.dsmr = dsmr;
    }

    public void setMsr(MouseSelectionRenderer msr)
    {
        this.msr = msr;
    }
    private DataSelectionRenderer dsr;
//    @Override
//    public void setBinding(Assembly.Serie binding)
//    {
//        super.setBinding(binding);
//        serieRenderer.setBinding(binding);
//    }
    private final MainReader reader;
    private final SerieRenderer serieRenderer;
    Viewport viewport;
    Path2D.Double busy;
    AffineTransform transform;
    final SpaceManager spaceManager;
    private boolean repaintFlag = true;
    private final boolean renderGrid;
//    private OverlayRenderer overlayRenderer;

    /**
     * Konstruktor ViewRenderera jest odpowiedzialny za utworzenie sub
     * rendererów na podstawie bindingów
     *
     * @param serie
     * @param mapping
     */
    public RootRenderer(Serie serie, Mapping mapping, Viewport viewport, SpaceManager spaceManager, SelectionController sc)
    {
        super(serie, mapping);
        renderGrid = true;
        this.spaceManager = spaceManager;
        this.viewport = viewport;
        reader = (MainReader) mapping.readerFor(serie);
        this.helpRenderer = new HelpRenderer(this);
        serieRenderer = new SerieRenderer(reader, this, serie);
        busy = new Path2D.Double();
        final int dx = -20;
        final int dy = -10;
        busy.moveTo(dx + 0, dy + 0);
        busy.lineTo(dx + 20, dy + 10);
        busy.lineTo(dx + 40, dy + 0);
        busy.lineTo(dx + 40, dy + 20);
        busy.lineTo(dx + 20, dy + 10);
        busy.lineTo(dx + 0, dy + 20);
        busy.closePath();
        busy.transform(AffineTransform.getScaleInstance(0.4, 0.4));
        transform = AffineTransform.getRotateInstance(Math.PI / 180.0 * 10);

        if (binding.getSelection() != null)
        {
            dsr = new DataSelectionRenderer(sc, this);
        }
        configure();
    }

    public RootRenderer(Viewport viewport, SpaceManager spaceManager)
    {
        super(null, null);
        this.viewport = viewport;
        this.helpRenderer = new HelpRenderer(this);
        reader = null;
        serieRenderer = null;
        this.spaceManager = spaceManager;
        renderGrid = false;
    }

    public Viewport getRenderTarget()
    {
        return viewport;
    }


    @Override
    public SpaceManager getSpaceManager()
    {
        return spaceManager;
    }

    public int getWidth()
    {
        return viewport.getWidth();
    }

    public int getHeight()
    {
        return viewport.getHeight();
    }

    public void setRepaintOnInvalidateFlag(boolean repaintFlag)
    {
        this.repaintFlag = repaintFlag;
    }

    @Override
    public final void invalidate()
    {
        sp().update();
        if (repaintFlag)
        {
            viewport.repaint();
        }
    }

    private void configure()
    {
        //main reader zostaje dodany metodą addChild
        // a tutaj chodzi o konfigurację interval+firstTime
        sp().configure(reader);
    }

    @Override
    public void render(Graphics2D g)
    {
        if (getHeight() == 0)
        {
            return;

        }
//        TimeUtil.start("view render - cały");
        //XXX ważne, że chart renderer updatuje tylko i wyłącznie on sam
        g.setFont(FontUtil.getFont("Consolas", Font.PLAIN, 12));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, sp().getVisibleWidth(), sp().getHeight());
        //pierwszy update sp(), dla gather data itd
        sp().update();
//        TimeUtil.start("gather data");
        if (serieRenderer != null)
        {
//            serieRenderer.gatherData();
            serieRenderer.calculateExtremum(new DataRendererBase.Extremum());
            if (isAutoScaleY())
            {
                SerieRenderer.CalculateAutoResult newParams = serieRenderer.calculateAutoParams();
                setRepaintOnInvalidateFlag(false);
                setScaleY(newParams.scaleY);
                setDataY(newParams.dataY);
                setRepaintOnInvalidateFlag(true);
            }
        }
//        TimeUtil.stop();
        //drugi update, ponieważ może zmieniono skalę Y
        sp().update();
//        TimeUtil.start("helpRender 1");
        if (helpRenderer != null)
        {
            if (renderGrid)
            {
                helpRenderer.renderUnderSerie(g);
                helpRenderer.renderGrid(g);
            } else
            {
                helpRenderer.renderLabels(g);
            }
        }
//        TimeUtil.stop();


        if (dsr != null)
        {
            dsr.renderUnderSerie(g);
        }

//        TimeUtil.start("render serie");
        if (serieRenderer != null)
        {
            serieRenderer.render(g);
        }
//        TimeUtil.stop();
//        TimeUtil.start("helpRender 2");
        if (helpRenderer != null)
        {
            if (renderGrid)
            {
                helpRenderer.renderVerticalAxe(g);
            } else
            {
            }
        }

//        TimeUtil.stop();
        if (dsr != null)
        {
            dsr.renderOverSerie(g);
        }


        if (busy != null)
        {
            //
            //nic nie trzeba dodatkowo, chyba że jakiś wait state można pokazać czy
            //evaluation mask etc
            busy.transform(transform);
            g.setPaint(Color.GRAY);
            final int busyX = getWidth() - 90;
            final int busyY = getHeight() - 10;
            g.translate(busyX, busyY);
            g.fill(busy);
            g.translate(-busyX, -busyY);


        }


        if (msr != null)
        {
            msr.render(g);
        }

        if (dsmr != null)
        {
            dsmr.render(g);
        }
        if (serieRenderer != null)
        {
            serieRenderer.renderHint(g);
        }
//        TimeUtil.stop();
    }


    public HelpRenderer getHelpRenderer()
    {
        return helpRenderer;
    }

    public SerieRenderer getSerieRenderer()
    {
        return serieRenderer;
    }
}
