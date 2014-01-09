/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Graphics2D;
import pl.adamborowski.utils.PaintUtil;

/**
 *
 * @author test
 */
public class DataSelectionMaskRenderer extends RectangleRenderer
{

    @Override
    public void render(Graphics2D g)
    {
       
        setBackgroundPaint(PaintUtil.createColor(0xffc2d3, 0.5f));
        setBorderPaint(PaintUtil.createColor(0xff3377, 0.9f));
        super.render(g);
    }
    
}
