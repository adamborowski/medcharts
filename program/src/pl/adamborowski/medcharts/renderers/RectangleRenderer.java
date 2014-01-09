/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Klasa do wyświetlania obszarów prostokątnych z kolorkami
 *
 * @author test
 */
public class RectangleRenderer extends RegionRendererBase
{

    private int radius = 10;

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    RoundRectangle2D.Float rectangle = new RoundRectangle2D.Float();

    public RoundRectangle2D.Float getRectangle()
    {
        return rectangle;
    }

    @Override
    public void render(Graphics2D g)
    {
        rectangle.x = getWidth() >= 0 ? getX() : getX() + getWidth();
        rectangle.y = getHeight() >= 0 ? getY() : getY() + getHeight();
        rectangle.archeight = radius;
        rectangle.arcwidth = radius;
        rectangle.width = Math.abs(getWidth());
        rectangle.height = Math.abs(getHeight());
        //background
        if (getBackgroundPaint() != null)
        {
            g.setPaint(getBackgroundPaint());
            g.fill(rectangle);
        }
        //border
        if (getBorderPaint() != null)
        {
            g.setPaint(getBorderPaint());
            g.draw(rectangle);
        }
    }
}
