/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;

/**
 *
 * @author test
 */
public class NewSelectionBehaviour extends AbstractBehaviour
{

    private final SelectionController sc;
    private SelectionItem ghost;

    public NewSelectionBehaviour(SelectionController sc)
    {
        this.sc = sc;
    }

    @Override
    public boolean canBehaveNow(StateVars vars)
    {
        return sc.getItemContainingData(vars.mouse.data.x) == null;
    }

    @Override
    protected void init()
    {
        ghost = sc.createGhost(view.sp().fitToSequence(init.mouse.data.x), view.sp().fitToSequence(init.mouse.data.x));
        
        view.invalidate();
//        medChart.repaint();
    }

    @Override
    protected void update()
    {
        ghost.tryResize(true, view.sp().fitToSequence(current.mouse.data.x));
        view.invalidate();
//        medChart.repaint();
    }

    @Override
    protected void finish()
    {
        update();
        sc.addGhostToItems();
        
    }

    @Override
    protected void cancel()
    {
        sc.cancelGhost();
        view.invalidate();
//        medChart.repaint();
    }
}
