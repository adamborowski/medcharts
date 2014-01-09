/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import java.awt.Cursor;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;

/**
 * klasa podświetlająca zaznaczenia (hover)
 *
 * @author test
 */
public class HoverSelectionBehaviour extends AbstractBehaviour
{

    private final SelectionController sc;

    public HoverSelectionBehaviour(SelectionController sc)
    {
        this.sc = sc;
    }
    private SelectionItem hoveredItem = null;

    /**
     * Update wołany mouseMove, niezależnie od niczego
     */
    @Override
    protected void update()
    {
        if(hoveredItem!=null&&hoveredItem.getState()!= SelectionController.ItemState.HOVERED)
        {
            hoveredItem.setState(SelectionController.ItemState.HOVERED);
        }
        SelectionItem guess = sc.getItemContainingData(current.mouse.data.x);
        if (guess != hoveredItem)
        {
            if (hoveredItem != null)
            {
                hoveredItem.setState(SelectionController.ItemState.NORMAL);
            }
            hoveredItem = guess;
            if (hoveredItem != null)
            {
                hoveredItem.setState(SelectionController.ItemState.HOVERED);
                medChart.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
            else
            {
                medChart.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            view.invalidate();
            medChart.repaint();
        }
    }

    @Override
    protected void finish()
    {
        //wyłączamy hover
        if (hoveredItem != null)
        {
            hoveredItem.setState(SelectionController.ItemState.NORMAL);
            hoveredItem = null;
            view.invalidate();
            medChart.repaint();
        }
        medChart.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
