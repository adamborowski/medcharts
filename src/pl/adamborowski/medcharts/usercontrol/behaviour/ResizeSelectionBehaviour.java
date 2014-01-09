/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.usercontrol.BehaviourController;

/**
 *
 * @author test
 */
public class ResizeSelectionBehaviour extends AbstractBehaviour
{
    
    final SelectionController controller;
    SelectionItem resizingItem = null;
    boolean rightEdge = false;
    long itemOffsetX;
    long initStart;
    long initEnd;
    
    public ResizeSelectionBehaviour(SelectionController controller)
    {
        this.controller = controller;
    }
    
    @Override
    public boolean canBehaveNow(StateVars vars)
    {
        resizingItem = controller.getItemContainingData(init.mouse.data.x);
        return resizingItem != null;
    }
    
    @Override
    protected void init()
    {
        resizingItem = controller.getItemContainingData(init.mouse.data.x);
        
        if (BehaviourController.IS_SHIFT)
        {
            controller.selection.toggleItem(resizingItem);
        } else
        {
            controller.selection.setMainItem(resizingItem);
        }
        view.invalidate();
        resizingItem.fixBounds();
        initStart = resizingItem.getStart();
        initEnd = resizingItem.getEnd();
        long resizingItemMidpoint = (resizingItem.getEnd() + resizingItem.getStart()) / 2;
        if (init.mouse.data.x <= resizingItemMidpoint)
        {
            rightEdge = false;
            itemOffsetX = init.mouse.data.x - resizingItem.getStart();
            //rozciąganie z lewej

        } else
        {
            rightEdge = true;
            itemOffsetX = init.mouse.data.x - resizingItem.getEnd();
        }
        resizingItem.setState(SelectionController.ItemState.DOWN);
    }
    
    @Override
    protected void update()
    {
        resizingItem.tryResize(rightEdge, view.sp().fitToSequence(current.mouse.data.x - itemOffsetX));
        view.invalidate();
//        medChart.repaint();
    }
    
    @Override
    protected void finish()
    {
        resizingItem.setState(SelectionController.ItemState.NORMAL);
        update();
    }
    
    @Override
    protected void cancel()
    {
        resizingItem.setStart(initStart);
        resizingItem.setEnd(initEnd);
        view.invalidate();
//        medChart.repaint();
    }
}
