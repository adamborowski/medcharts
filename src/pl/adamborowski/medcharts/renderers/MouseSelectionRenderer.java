/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import pl.adamborowski.utils.PaintUtil;

/**
 *
 * @author test
 */
public class MouseSelectionRenderer extends RendererBase
{

    public MouseSelectionRenderer(RendererBase parent)
    {
        super(parent);
    }

    private float y1;
    private float y2;
    private long x1;
    private long x2;

    public void setY1(float y1)
    {
        this.y1 = y1;
    }

    public void setY2(float y2)
    {
        this.y2 = y2;
    }

    public void setX1(long x1)
    {
        this.x1 = x1;
    }

    public void setX2(long x2)
    {
        this.x2 = x2;
    }

    @Override
    public void render(Graphics2D g)
    {
        int tmp;
        int displayX1 = (int) sp().toDisplayX(x1);
        int displayX2 = (int) sp().toDisplayX(x2);
        int displayY1 = (int) sp().toDisplayY(y1);
        int displayY2 = (int) sp().toDisplayY(y2);

        if(displayX1>displayX2)
        {
            tmp=displayX1;
            displayX1=displayX2;
            displayX2=tmp;
        }
        if(displayY1>displayY2)
        {
            tmp=displayY1;
            displayY1=displayY2;
            displayY2=tmp;
        }
        
        
        int width = displayX2 - displayX1;
        int height = displayY2 - displayY1;
        // 
       Rectangle rect = new Rectangle(displayX1, displayY1, width, height);
        g.setPaint(PaintUtil.createColor(0x000000,0.5f));
        g.fill(rect);
        g.setPaint(PaintUtil.createColor(0xffffff,0.5f));
        g.draw(rect);


    }
}
