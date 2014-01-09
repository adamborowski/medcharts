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
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.assembly.reading.OverlayReader;
import pl.adamborowski.utils.BinaryRangeSearchUtil;
import pl.adamborowski.utils.PaintUtil;

/**
 *
 * @author test
 */
public class OverlayRenderer extends DataRendererBase<OverlayLinesCollection.Multipoint, Assembly.Serie.Overlay, OverlayLinesCollection.Iteration>
{

    private Line2D.Float lines[];
    private LabelRenderer titles[];

    public OverlayRenderer(IDataReader reader, RendererBase parent, Assembly.Serie.Overlay binding)
    {
        super(reader, parent, binding);
        initTitles();
    }

    @Override
    public void render(Graphics2D g)
    {
        if (currentScope.willRender() == false)
        {
            return;
        }

        renderMasks(g);
        renderGraphs(g);
    }

    private void initTitles()
    {
        int numEntry = binding.getLine().size() + binding.getMask().size();
        titles = new LabelRenderer[numEntry];

        int iColumn = 0;

        for (Assembly.Serie.Overlay.Line line : binding.getLine())
        {
            LabelRenderer title = new LabelRenderer(line.getName(), 10, iColumn * 30 + 40);
            titles[iColumn] = title;
            title.setForegroundColor(Color.decode(line.getColor()));
//            addChild(title);
            iColumn++;
        }

        for (Assembly.Serie.Overlay.Mask mask : binding.getMask())
        {
            LabelRenderer title = new LabelRenderer(mask.getName(), 10, iColumn * 30 + 40);
            titles[iColumn] = title;
            title.setForegroundColor(Color.decode(mask.getColor()));
//            addChild(title);
            iColumn++;
        }
    }

    @Override
    protected OverlayLinesCollection.Multipoint[] createArrayInstance(int size)
    {
        return new OverlayLinesCollection.Multipoint[size];
    }

    @Override
    protected Extremum calculateExtremum()
    {
        extremum.max = Float.MIN_VALUE;
        extremum.min = Float.MAX_VALUE;
        if (currentScope.data != null)
        {
            for (OverlayLinesCollection.Iteration i : currentScope.data)
            {
                if (i == null || i.y == null)
                {
                    break;
                }
                for (float linePoint : i.y.linePoints)
                {
                    extremum.max = Math.max(extremum.max, linePoint);
                    extremum.min = Math.min(extremum.min, linePoint);
                }
            }
        }
        return extremum;
    }

    private void renderGraphs(Graphics2D g) throws NumberFormatException
    {

        final OverlayLinesCollection.Multipoint[] multipoints = currentScope.data.getYData();
        final int numLines = multipoints[0].linePoints.length;
        int x1[] = new int[numLines];
        int x2[] = new int[numLines];
        int y1[] = new int[numLines];
        int y2[] = new int[numLines];
        Color colors[] = new Color[numLines];
//        lines = new Line2D.Float[numLines];
        for (int i = 0; i < numLines; i++)
        {
            x2[i] = currentScope.firstPixel;
            y2[i] = (int) sp().toDisplayY(multipoints[0].linePoints[i]);
            colors[i] = Color.decode(binding.getLine().get(i).getColor());
        }
        int j;
        final int p = 2, p2 = 2 * p;
        final int pointsSpace = 2;
        final boolean drawPoitns = sp().getScaleX() < sp().getSequence().getA() / (float) (p2 + pointsSpace);
        float value;
        int newX;
        for (OverlayLinesCollection.Iteration i : currentScope.data)
        {
            for (j = 0; j < numLines; j++)
            {
                value = i.y.linePoints[j];
                if (Float.isNaN(value))
                {
                    continue;
                }
                newX = (int) sp().toDisplayX(i.x);
                if (newX == x2[j] && i.i != 0)
                {
                    continue;
                }
                x1[j] = x2[j];
                y1[j] = y2[j];
                x2[j] = newX;

                y2[j] = (int) sp().toDisplayY(i.y.linePoints[j]);
                g.setPaint(colors[j]);
                g.drawLine(x1[j], y1[j], x2[j], y2[j]);
                if (drawPoitns)
                {
                    //rysuj punkciek

                    g.drawArc(x2[j] - p, y2[j] - p, p2, p2, 0, 360);
                }
            }
        }
////        //od jakiego x zaczynamy
//
//
//
//        for (Pixel pixel : currentScope)
//        {
//            int lineIndex = 0;
//            for (Line2D.Float line : lines)
//            {
//                line.x1 = line.x2;
//                line.y1 = line.y2;
//                if (pixel.value == null)
//                {
//                    break;
//                }
//                Float value = pixel.value.linePoints[lineIndex];
//                g.setPaint(Color.decode(binding.getLine().get(lineIndex).getColor()));
//                if (!value.isNaN())
//                {
//                    line.x2 = pixel.displayX;
//                    line.y2 = sp().toDisplayY(value);
//                    g.draw(line);
//                }
//                lineIndex++;
//            }
//        }
        //        int startingPixel = (int) Math.ceil(sp().toDisplayX(sp().visible.firstAccessible)); // ceil bo wczesniejszego pixela nie ma
//        int endingPixel = (int) sp().toDisplayX(sp().visible.lastAccessible);
//        OverlayLinesCollection.Multipoint overlayItem = data.getY(sp().toDataX(startingPixel));
//        int iLine = 0;
//        for (Line2D.Float line : lines)
//        {
//            line.x2 = startingPixel;
//            line.y2 = sp().toDisplayY(overlayItem.linePoints[iLine++]);
//        }
//
//        for (float i = startingPixel + incrementor - 1; i < endingPixel+incrementor-1; i += incrementor)
//        {
//            long iDataX = sp().toDataX(i);
//            try
//            {
//                overlayItem = data.getY(iDataX);
//            } catch (Throwable e)
//            {
//                for(int ss=0;ss<numLines;ss++)
//                {
//                    overlayItem.linePoints[ss]=0.5f;
//                }
//            }
//            iLine = 0;
//            for (Line2D.Float line : lines)
//            {
//                line.x1 = line.x2;
//                line.y1 = line.y2;
//                line.x2 = i;
//                line.y2 = sp().toDisplayY(overlayItem.linePoints[iLine]);
//                g.setPaint(Color.decode(binding.getColumn().get(iLine).getColor()));
//                iLine++;
//                g.draw(line);
//            }
//            iDataX += sp().getDataPerPixel();
//        }
//        g.drawString("overlay", 40, 20);

        for (LabelRenderer title : titles)
        {
            title.render(g);
        }
    }
    private RectangleRenderer rectangleRenderer = new RectangleRenderer();
    private BinaryRangeSearchUtil<Mask.Item, Long> finder = new BinaryRangeSearchUtil<>();

    private void renderMasks(Graphics2D g)
    {
        OverlayReader overlayReader = ((OverlayReader) reader);
        ArrayList<Mask> masks = overlayReader.getMasks();
        int maskIndex = 0;//po liniach zaczynaja się maski
        for (Mask mask : masks)
        {
            if (binding.getMask().get(maskIndex).getImportToSelection() != null)
            {
                continue; // nie wyświetlamy, gdy jest zaimportowany do selection
            }
            final Assembly.Serie.Overlay.Mask column = binding.getMask().get(maskIndex);
            final Color maskColor = PaintUtil.createColor(column.getColor(), column.getAlpha());
            rectangleRenderer.setBackgroundPaint(maskColor);
            rectangleRenderer.setBorderPaint(null);
            rectangleRenderer.setRadius(7);
            rectangleRenderer.setY((int) sp().toDisplayY(extremum.max));
            rectangleRenderer.setY2((int) sp().toDisplayY(extremum.min));
            //teraz znajdź indeks maski która będzie widzialna
            ArrayList<Mask.Item> maskItems = finder.search(mask.getItems(), sp().visibleData.first, sp().visibleData.last);
            for (Mask.Item item : maskItems)
            {
                rectangleRenderer.setX((int) sp().toDisplayX(item.getStart()));
                rectangleRenderer.setWidth((int) sp().toDisplayX(item.getEnd()) - rectangleRenderer.getX());
                rectangleRenderer.render(g);
            }
            maskIndex++;
        }

    }
}
