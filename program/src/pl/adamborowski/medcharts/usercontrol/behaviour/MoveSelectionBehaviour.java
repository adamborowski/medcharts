/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import java.awt.geom.Area;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.renderers.DataSelectionItemRenderer;
import pl.adamborowski.medcharts.usercontrol.BehaviourController;

/**
 *
 * @author test
 */
public class MoveSelectionBehaviour extends AbstractBehaviour
{

    final SelectionController controller;
    long initStart;
    long initEnd;
    private long itemOffsetX;

    public MoveSelectionBehaviour(SelectionController controller)
    {
        this.controller = controller;
    }
    private SelectionItem movingItem;

    @Override
    public boolean canBehaveNow(StateVars vars)
    {
        movingItem = controller.getItemContainingData(vars.mouse.data.x);
        if (movingItem != null)
        {
            int disX1 = (int) view.sp().toDisplayX(movingItem.getStart());
            int disX2 = (int) view.sp().toDisplayX(movingItem.getEnd());
            Area resizeArea = DataSelectionItemRenderer.getResizeArea(disX1, 0, disX2 - disX1, view.sp().getHeight());
            return !resizeArea.contains(vars.mouse.display.x, vars.mouse.display.y);
        }
        return false;
    }

    @Override
    protected void init()
    {
        if (BehaviourController.IS_SHIFT)
        {
            controller.selection.toggleItem(movingItem);
        } else
        {
            controller.selection.setMainItem(movingItem);
        }
        view.invalidate();
        movingItem.setState(SelectionController.ItemState.DOWN);
        initStart = movingItem.getStart();
        initEnd = movingItem.getEnd();
        //
        itemOffsetX = init.mouse.data.x - initStart;
    }

    @Override
    protected void update()
    {
        movingItem.tryMove(view.sp().fitToSequence(current.mouse.data.x - itemOffsetX), current.mouse.data.x);
        view.invalidate();
//        medChart.repaint();
    }

    @Override
    protected void finish()
    {
        update();
        movingItem.setState(SelectionController.ItemState.NORMAL);
    }

    @Override
    protected void cancel()
    {
        movingItem.setStart(initStart);
        movingItem.setEnd(initEnd);
        view.invalidate();
//        medChart.repaint();
        movingItem.setState(SelectionController.ItemState.NORMAL);
    }
}
