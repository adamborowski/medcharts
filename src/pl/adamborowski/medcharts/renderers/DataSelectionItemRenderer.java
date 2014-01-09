/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import pl.adamborowski.medcharts.MedChartPreferences;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.utils.GradientFactory;
import pl.adamborowski.utils.PaintUtil;

/**
 *
 * @author test
 */
public class DataSelectionItemRenderer extends RendererBase<Void>
{

    public DataSelectionItemRenderer(RendererBase parent)
    {
        super(parent);
    }
    public static Area lastArea = null;

    public static Area getResizeArea(int x, int y, int width, int height)
    {
        y += 2;
        height -= 5;
        Area area = new Area();
        area.add(new Area(new ArrowShape(x, y, 0)));
        area.add(new Area(new ArrowShape(x + width, y, 1)));
        area.add(new Area(new ArrowShape(x + width, y + height, 2)));
        area.add(new Area(new ArrowShape(x, y + height, 3)));
        lastArea = area;
        return area;

    }
    private SelectionItem currentItem;

    public void setCurrentItem(SelectionItem currentItem)
    {
        this.currentItem = currentItem;
    }
    private RectangleRenderer rectangleRenderer = new RectangleRenderer();

    @Override
    public void render(Graphics2D g)
    {
        GradientFactory gf;
        switch (currentItem.getState())
        {
            case GHOST:
                gf = SelectionItemPaint.GHOST;
                break;
            case NORMAL:
                gf = SelectionItemPaint.NORMAL;
                break;
            case HOVERED:
                gf = SelectionItemPaint.HOVERED;
                break;
            case DOWN:
                gf = SelectionItemPaint.DOWN;
                break;
            default:
                throw new AssertionError(currentItem.getState().name());
        }

        Paint p = gf.getGradient(0, sp().getHeight(), 0, 0);
        if (currentItem.getState() == SelectionController.ItemState.HOVERED && getPreferenceB(MedChartPreferences.HIDE_HOVERED_SELECTION))
        {
            p = PaintUtil.createColor(0x008800, .0f);
        }
        final int margin = 2;
        final int leftPos = (int) sp().toDisplayX(currentItem.getStart());
        final int rightPos = (int) sp().toDisplayX(currentItem.getEnd());
        final int topPos = margin;
        final int bottomPos = sp().getHeight() - margin - 2;
        final int width = rightPos - leftPos;
        final int height = bottomPos - topPos;
        rectangleRenderer.setX(leftPos);
        rectangleRenderer.setWidth(width);
        rectangleRenderer.setY(topPos);
        rectangleRenderer.setHeight(height);
        rectangleRenderer.setRadius(10);
        rectangleRenderer.setBackgroundPaint(p);
        rectangleRenderer.setBorderPaint(new Color(0x55aa55));
        rectangleRenderer.render(g);



        //
        if (currentItem.getState() != SelectionController.ItemState.NORMAL)
        {
            if (rightPos - leftPos > 30)
            {
                g.setPaint(PaintUtil.createColor(0xffffff, .8f));
                g.fill(getResizeArea(leftPos, 0, width + 1, sp().getHeight()));
            }
        }
        if (currentItem.isSelected())
        {
            rectangleRenderer.setBackgroundPaint(null);
            Stroke str = g.getStroke();
            g.setStroke(new BasicStroke(2f));
            rectangleRenderer.setBorderPaint(PaintUtil.createColor(0x333333, 0.6f));
            rectangleRenderer.render(g);
            g.setStroke(str);
        }
    }

    private final static class ArrowShape extends java.awt.geom.Path2D.Float
    {

        public static final int size = 20;
        public static final int round = 6;

        public ArrowShape(int x, int y, int quadrant)
        {
            this(quadrant);
            translate(x, y);
        }

        public ArrowShape(int quadrant)
        {
            super();

            moveTo(0, round);
            quadTo(0, 0, round, 0);
            lineTo(size, 0);
            lineTo(0, size);
            closePath();
            translate(1, 1);
            transform(AffineTransform.getQuadrantRotateInstance(quadrant));
        }

        public void translate(int dx, int dy)
        {
            AffineTransform at = new AffineTransform();
            at.translate(dx, dy);
            transform(at);
        }
    }
}
