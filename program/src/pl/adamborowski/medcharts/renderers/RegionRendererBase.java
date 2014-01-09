/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Paint;

/**
 * klasa bazowa do różnych regionów  ( prostokąty, koła)
 * może mieć położenie, rozmiar, kolory
 * @author test
 */
public abstract class RegionRendererBase extends RendererBase<Void>
{

    private int x;
    private int y;
    private int width;
    private int height;
    private Paint foregroundPaint = Color.GRAY;
    private Paint backgroundPaint = new Color(0x99ffffff, true);
    private Paint borderPaint = new Color(0xaa777777, true);

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }
    
    public void setY2(int y2)
    {
        height=y2-y;
    }
    
    public void setX2(int x2)
    {
        width=x2-x;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public Paint getForegroundPaint()
    {
        return foregroundPaint;
    }

    public void setForegroundPaint(Paint foregroundPaint)
    {
        this.foregroundPaint = foregroundPaint;
    }

    public Paint getBackgroundPaint()
    {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint)
    {
        this.backgroundPaint = backgroundPaint;
    }

    public Paint getBorderPaint()
    {
        return borderPaint;
    }

    public void setBorderPaint(Paint borderPaint)
    {
        this.borderPaint = borderPaint;
    }
}
