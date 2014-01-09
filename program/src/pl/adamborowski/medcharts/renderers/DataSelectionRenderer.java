/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.usercontrol.BehaviourController;

/**
 *
 * @author test
 */
public class DataSelectionRenderer extends RendererBase<Void>
{
    
    private final SelectionController selectionController;

    public DataSelectionRenderer(SelectionController selectionController, RootRenderer viewRenderer)
    {
        super(viewRenderer);
        this.selectionController = selectionController;
    }


    DataSelectionItemRenderer dsir = new DataSelectionItemRenderer(this);

    @Override
    public void render(Graphics2D g)
    {
        
    }
    
    private SelectionItem hoveredItem=null;
    
    public void renderUnderSerie(Graphics2D g)
    {
        hoveredItem=null;
        if (selectionController.getItems().size() > 0)
        {
            ArrayList<SelectionItem> items = selectionController.getItemsInRange(sp().visibleData.first, sp().visibleData.last);
            for (SelectionItem item : items)
            {
                if(item.getState()!= SelectionController.ItemState.NORMAL)
                {
                    hoveredItem= item;
                }
                dsir.setCurrentItem(item);
                dsir.render(g);
            }
        }
        //przechodź przez itemy i wkłądaj do selectionItemrenderera nowy item, na podsatwie jego stanu wyświetli strzałki, zmieni kolor itd
    }
    public void renderOverSerie(Graphics2D g)
    {
        if (selectionController.getGhost() != null)
        {
            dsir.setCurrentItem(selectionController.getGhost());
            dsir.render(g);
        }
        if(hoveredItem!=null)
        {
            dsir.setCurrentItem(hoveredItem);
            dsir.render(g);
        }
    }
}
