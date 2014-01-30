/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import pl.adamborowski.medcharts.assembly.data.Mask;
import pl.adamborowski.medcharts.assembly.data.OverlayLinesCollection;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Overlay;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import pl.adamborowski.medcharts.assembly.reading.OverlayReader;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.medcharts.data.SerieRenderer;
import pl.adamborowski.utils.BinaryRangeSearchUtil;
import pl.adamborowski.utils.PaintUtil;
import pl.adamborowski.utils.builders.HierarchyBuilder;

/**
 *
 * @author test
 */
public class OverlayRenderer extends DataRendererBase<OverlayLinesCollection.Multipoint, Overlay, OverlayLinesCollection.Iteration> {

    private Line2D.Float lines[];
    private LabelRenderer titles[];
    private ArrayList<pl.adamborowski.medcharts.data.SerieRenderer> serieRenderers = new ArrayList<pl.adamborowski.medcharts.data.SerieRenderer>();

    public OverlayRenderer(OverlayReader reader, RendererBase parent, Overlay binding) {
        super(reader, parent, binding);
        initTitles();
        for (SerieReader s : reader.serieReaders) {
            serieRenderers.add(HierarchyBuilder.buildRendererHierarchyFromReaderHierarchy(s, sp()));
        }
    }

    @Override
    public void render(Graphics2D g) {
        renderMasks(g);
        renderGraphs(g);
    }

    private void initTitles() {
        int numEntry = binding.getLine().size() + binding.getMask().size();
        titles = new LabelRenderer[numEntry];

        int iColumn = 0;

        for (Overlay.Line line : binding.getLine()) {
            LabelRenderer title = new LabelRenderer(line.getName(), 10, iColumn * 30 + 40);
            titles[iColumn] = title;
            title.setForegroundColor(Color.decode(line.getColor()));
//            addChild(title);
            iColumn++;
        }

        for (Overlay.Mask mask : binding.getMask()) {
            LabelRenderer title = new LabelRenderer(mask.getName(), 10, iColumn * 30 + 40);
            titles[iColumn] = title;
            title.setForegroundColor(Color.decode(mask.getColor()));
//            addChild(title);
            iColumn++;
        }
    }

    @Override
    protected OverlayLinesCollection.Multipoint[] createArrayInstance(int size) {
        return new OverlayLinesCollection.Multipoint[size];
    }

    @Override
    public void calculateExtremum(DataRendererBase.Extremum extremum) {
        for (SerieRenderer s : serieRenderers) {
            s.calculateExtremum(extremum);
        }

    }

    private void renderGraphs(Graphics2D g) throws NumberFormatException {

        int i=0;
        OverlayReader reader2=(OverlayReader) reader;
        for (SerieRenderer s : serieRenderers) {
            
            
            g.setPaint(Color.decode(reader2.getBinding().getLine().get(i).getColor()));
            s.render(g);
            
            i++;
        }
        for (LabelRenderer title : titles) {
            title.render(g);
        }
    }
    private RectangleRenderer rectangleRenderer = new RectangleRenderer();
    private BinaryRangeSearchUtil<Mask.Item, Long> finder = new BinaryRangeSearchUtil<>();

    private void renderMasks(Graphics2D g) {
        OverlayReader overlayReader = ((OverlayReader) reader);
        ArrayList<Mask> masks = overlayReader.getMasks();
        int maskIndex = 0;//po liniach zaczynaja się maski
        for (Mask mask : masks) {
            if (binding.getMask().get(maskIndex).getImportToSelection() != null) {
                continue; // nie wyświetlamy, gdy jest zaimportowany do selection
            }
            final Overlay.Mask column = binding.getMask().get(maskIndex);
            final Color maskColor = PaintUtil.createColor(column.getColor(), column.getAlpha());
            rectangleRenderer.setBackgroundPaint(maskColor);
            rectangleRenderer.setBorderPaint(null);
            rectangleRenderer.setRadius(7);
            rectangleRenderer.setY((int) sp().toDisplayY(extremum.max));
            rectangleRenderer.setY2((int) sp().toDisplayY(extremum.min));
            //teraz znajdź indeks maski która będzie widzialna
            ArrayList<Mask.Item> maskItems = finder.search(mask.getItems(), sp().visibleData.first, sp().visibleData.last);
            for (Mask.Item item : maskItems) {
                rectangleRenderer.setX((int) sp().toDisplayX(item.getStart()));
                rectangleRenderer.setWidth((int) sp().toDisplayX(item.getEnd()) - rectangleRenderer.getX());
                rectangleRenderer.render(g);
            }
            maskIndex++;
        }

    }
}
