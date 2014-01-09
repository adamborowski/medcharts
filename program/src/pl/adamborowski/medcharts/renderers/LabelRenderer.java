/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import pl.adamborowski.utils.FontUtil;
import sun.java2d.BackBufferCapsProvider;

/**
 *
 * @author test
 */
public class LabelRenderer extends RendererBase<Void>
{

    public LabelRenderer(String text, int x, int y)
    {
        this.y = y;
        this.x = x;
        this.text = text;
    }

    private int padding = 6;
    private Color foregroundColor = Color.GRAY;
    private int radius = 10;
    private Color backgroundColor =new Color(0x99ffffff, true);
    private Color borderColor = new Color(0xaa777777, true);
    private int y;
    private int x;
    private String text;

    public int getPadding()
    {
        return padding;
    }

    public void setPadding(int padding)
    {
        this.padding = padding;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public void render(Graphics2D g)
    {
        final Font font = g.getFont();
        final FontMetrics fm = g.getFontMetrics(font);
        final Rectangle2D textBounds = FontUtil.getStringBounds(text, g);
        final int textWidth = (int) textBounds.getWidth();
        final int textHeight = (int) textBounds.getHeight();
        final int borderWidth = textWidth + 2 * padding;
        final int borderHeight = textHeight + 2 * padding;
        final int borderX = getX();
        final int borderY = getY();
        final int textX = borderX + padding;
        
        final int textY = borderY + padding+fm.getAscent();

        final RoundRectangle2D.Float border = new RoundRectangle2D.Float(borderX, borderY, borderWidth, borderHeight, radius, radius);

        g.setPaint(backgroundColor);
        g.fill(border);
        g.setPaint(borderColor);
        g.draw(border);
        g.setPaint(foregroundColor);
        g.drawString(text, textX, textY);
    }
}
