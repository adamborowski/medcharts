/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;

/**
 *
 * @author test
 */
public class PaintUtil
{

    public static Color createColor(String rgb, float a)
    {
        if (rgb.startsWith("#"))
        {
            rgb = rgb.substring(1);
        }
        return createColor(Integer.parseInt(rgb, 16), a);
    }

    public static Color createColor(int rgb, float a)
    {
        final int al = (int) (a * 256);
        rgb = rgb | al << 24;
        return new Color(rgb, true);
    }

    private abstract class PaintItem
    {

        Paint paint;

        abstract void paintOn(Graphics2D g);
    }
    ArrayList<PaintItem> items = new ArrayList<>();

    public void paintOn(Graphics2D g)
    {
        for (PaintItem i : items)
        {
            i.paintOn(g);
        }
    }

    public void addShape(Shape shape, Paint paint, Paint strokePaint)
    {
        PaintShapeItem sh = new PaintShapeItem();
        sh.shape = shape;
        sh.paint = paint;
        sh.strokePaint = strokePaint;
        items.add(sh);
    }

    public void addStrokedShape(Shape shape, Paint strokePaint)
    {
        addShape(shape, null, strokePaint);
    }

    public void addFilledShape(Shape shape, Paint fillPaint)
    {
        addShape(shape, fillPaint, null);
    }

    public void addText(String text, Paint paint, int x, int y)
    {
        PaintStringItem it = new PaintStringItem();
        it.paint = paint;
        it.x = x;
        it.y = y;
        it.string = text;
        items.add(it);
    }

    private class PaintShapeItem extends PaintItem
    {

        Shape shape;
        Paint strokePaint;

        @Override
        void paintOn(Graphics2D g)
        {
            if (paint != null)
            {
                g.setPaint(paint);
                g.fill(shape);
            }
            if (strokePaint != null)
            {
                g.setPaint(strokePaint);
                g.draw(shape);
            }
        }
    }

    private class PaintStringItem extends PaintItem
    {

        String string;
        int x;
        int y;

        @Override
        void paintOn(Graphics2D g)
        {
            g.setPaint(paint);
            g.drawString(string, x, y);
        }
    }
}
