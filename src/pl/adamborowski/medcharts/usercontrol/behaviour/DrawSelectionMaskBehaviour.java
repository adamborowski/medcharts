/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import java.util.ArrayList;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.renderers.DataSelectionItemRenderer;
import pl.adamborowski.medcharts.renderers.DataSelectionMaskRenderer;
import pl.adamborowski.medcharts.usercontrol.BehaviourController;

/**
 *
 * @author test
 */
public class DrawSelectionMaskBehaviour extends AbstractBehaviour
{

    private final SelectionController sc;

    public DrawSelectionMaskBehaviour(SelectionController sc)
    {
        this.sc = sc;
    }

    @Override
    public boolean canBehaveNow(StateVars vars)
    {
        return BehaviourController.IS_CONTROL;
    }

    @Override
    protected void init()
    {
        dsmr.setX(init.mouse.display.x);
        dsmr.setWidth(1);
//        //
        dsmr.setY(init.mouse.display.y);
        dsmr.setHeight(1);
        view.setDsmr(dsmr);
        view.invalidate();

//        dsmr.setY2(init.mouse.data.y + view.sp().toDataHeight(1));
//        view.setDsmr(dsmr);
//        view.invalidate();
    }

    @Override
    protected void update()
    {
        dsmr.setX2(current.mouse.display.x);
        dsmr.setY2(current.mouse.display.y);
        view.invalidate();
    }

    @Override
    protected void finish()
    {
        view.setDsmr(null);
        view.invalidate();
        ArrayList<SelectionItem> items = sc.getItemsInRange(Math.min(init.mouse.data.x, current.mouse.data.x), Math.max(init.mouse.data.x, current.mouse.data.x));
        if (items.isEmpty() == false)
        {
            sc.selection.setMainItem(items.get(0));
        }
        sc.selection.setItems(items);
    }

    @Override
    protected void cancel()
    {
        view.setDsmr(null);
        view.invalidate();
    }
    final DataSelectionMaskRenderer dsmr = new DataSelectionMaskRenderer();
}
