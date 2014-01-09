/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

/**
 *
 * @author test
 */
public class ScrollDragBehaviour extends AbstractBehaviour
{
    private int mouseOffsetX;
    private int mouseOffsetY;

    @Override
    protected void init()
    {
        mouseOffsetX=init.mouse.display.x-init.center.display.x;
        mouseOffsetY=init.mouse.display.y-init.center.display.y;
    }

    @Override
    protected void update()
    {
        medChart.setDataX(init.center.data.x-view.sp().toDataWidth(current.mouse.display.x-current.center.display.x-mouseOffsetX));
        view.setDataY(init.center.data.y+view.sp().toDataHeight(current.mouse.display.y-current.center.display.y-mouseOffsetY));
    }

    @Override
    protected void finish()
    {
        update();
    }

    @Override
    protected void cancel()
    {
        medChart.setDataX(init.center.data.x);
        view.setDataY(init.center.data.y);
    }
    
}
