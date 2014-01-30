/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Selection;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 * .
 * kontroler zaznaczenia - odpowienik xmlowego "selection"
 *
 * jeśli kilka serii w assembly chce kontrolować to samo zaznaczenie (wymagane
 * na razie: albo tylko w pierwszym, albo we wszystkich naraz, ale nie wiadomo,
 * czy nie będą chcieli zaznaczenia wydziwnionego
 *
 * @author test
 */
public class SelectionController
{

    private static Map<pl.adamborowski.medcharts.assembly.jaxb.Selection, SelectionController> controllersForSelections = new HashMap<>();

    public static SelectionController getControllerForSelection(pl.adamborowski.medcharts.assembly.jaxb.Selection selection, SpaceManager spaceManager, Viewport viewport)
    {
        if (controllersForSelections.containsKey(selection))
        {
            return controllersForSelections.get(selection);
        }
        SelectionController controller = new SelectionController(selection, spaceManager, viewport, viewport.getMedChart().getImporter());
        controllersForSelections.put(selection, controller);
        return controller;
    }
    File itemsFile;

    public SpaceManager getSpaceManager()
    {
        return spaceManager;
    }

    public static void clearControllers()
    {
        controllersForSelections.clear();
    }
    //------------------------------------------------------------------------------------------
    final private pl.adamborowski.medcharts.assembly.jaxb.Selection jaxbSelection;

    public pl.adamborowski.medcharts.assembly.jaxb.Selection getBinding()
    {
        return jaxbSelection;
    }
    private final SpaceManager spaceManager;
    private final Viewport viewport;

    public SelectionController(pl.adamborowski.medcharts.assembly.jaxb.Selection selection, SpaceManager spaceManager, Viewport viewport, AssemblyImporter importer)
    {
        this.jaxbSelection = selection;
        this.spaceManager = spaceManager;
        this.viewport = viewport;
        items = new ArrayList<>();
        itemsFile = getSelectionFile(importer, selection);
        if (itemsFile.exists())
        {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(itemsFile)))
            {
                items = (ArrayList<SelectionItem>) ois.readObject();
                for (SelectionItem i : items)
                {
                    i.__deserialize__(this);
                }
            } catch (Exception ex)
            {
                Logger.getLogger(SelectionController.class.getName()).log(Level.SEVERE, null, ex);
                items = new ArrayList<>();
                itemsFile.delete();
            }
        } else
        {
            items = new ArrayList<>();
        }
    }

    public void serialize()
    {
        try
        {
            File tmpFile = File.createTempFile(itemsFile.getName(), ".sel~", itemsFile.getParentFile());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile)))
            {
                oos.writeObject(items);
            } catch (Exception ex)
            {
                Logger.getLogger(SelectionController.class.getName()).log(Level.SEVERE, null, ex);
                tmpFile.delete();
            }
            itemsFile.delete();
            tmpFile.renameTo(itemsFile);
        } catch (IOException ex)
        {
            Logger.getLogger(SelectionController.class.getName()).log(Level.SEVERE, null, ex);

        }


    }

    public static void serialize(List<SelectionItem> items, AssemblyImporter importer, pl.adamborowski.medcharts.assembly.jaxb.Selection selection)
    {
        File myItemsFile = getSelectionFile(importer, selection);

        try
        {
            File tmpFile = File.createTempFile(myItemsFile.getName(), ".sel~", myItemsFile.getParentFile());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile)))
            {
                oos.writeObject(items);
            } catch (Exception ex)
            {
                Logger.getLogger(SelectionController.class.getName()).log(Level.SEVERE, null, ex);
                tmpFile.delete();
            }
            myItemsFile.delete();
            tmpFile.renameTo(myItemsFile);
        } catch (IOException ex)
        {
            Logger.getLogger(SelectionController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    //--------------------------------------------------------------------------

    public void removeItem(SelectionItem item)
    {
        items.remove(item);
    }

    public void clear()
    {
        items.clear();
    }

    void addItem(SelectionItem newItem)
    {
        items.add(newItem);
    }

    public void cancelGhost()
    {
        ghost = null;
    }

    void joinSelected()
    {
        if (selection.items.size() < 2)
        {
            return;
        }
        Collections.sort(selection.items);
        SelectionItem newItem = new SelectionItem(this);
        newItem.setStart(selection.items.get(0).getStart());
        newItem.setEnd(selection.items.get(selection.items.size() - 1).getEnd());
        newItem.setState(SelectionController.ItemState.NORMAL);
        ArrayList<SelectionItem> joinedItems = getItemsInRange(newItem.getStart(), newItem.getEnd());

        for (SelectionItem i : joinedItems)
        {
            removeItem(i);
        }
        addItem(newItem);
        selection.setMainItem(newItem);
        //updateSelections();
    }

    public static File getSelectionFile(AssemblyImporter importer, pl.adamborowski.medcharts.assembly.jaxb.Selection selection)
    {
        return importer.getBinPath().resolve(selection.getId() + ".sel").toFile();
    }

    public enum ItemState
    {

        GHOST, NORMAL, HOVERED, DOWN
    };
    private SelectionItem ghost;

    public SelectionItem getGhost()
    {
        return ghost;
    }

    public SelectionItem createGhost(long start, long end)
    {
        selection.deselectAll();
        ghost = new SelectionItem(this);
        ghost.start = start;
        ghost.end = end;
        return ghost;
    }

    public void addGhostToItems()
    {
        items.add(ghost);
        ghost.setState(ItemState.NORMAL);
        selection.setMainItem(ghost);
        ghost = null;
    }

    public ArrayList<SelectionItem> getItems()
    {
        return items;
    }
    private ArrayList<SelectionItem> items;

    public SelectionItem getItemContainingData(long xData)
    {
        //przeszukiwanie binarne kolekcji
        for (SelectionItem i : items)
        {
            if (i.getStart() <= xData && i.getEnd() >= xData)
            {
                return i;
            }
        }
        return null;
    }

    public DataRange getRangeOfFreeSpaceUnderPoint(long xPoint, SelectionItem exclude)
    {
        //idź w pętli po posortowanych obszarach i pamiętaj prawą krawędź
        //itemu jeśli jest przed punktem. jesli po, to zwróć zakres o ile się nie zachaczają, wtedy null
        Collections.sort(items);
        long lastEnd = Integer.MIN_VALUE;
        for (SelectionItem i : items)
        {
            if (i == exclude)
            {
                continue;
            }
            if (i.contains(xPoint))
            {
                //nie ma co, obszar pokrywa xPoint
                return null;
            }
            if (i.getEnd() <= xPoint)
            {
                //ten obszar jest przed xPoint, dlatego lastEnd to jego prawa krawędź
                lastEnd = spaceManager.getSequence().nextTime(i.getEnd());
            } else if (i.getEnd() >= xPoint)
            {
                //ten obszar jest już za, zatem jego lewa to nasza prawa krawędź
                return new DataRange(lastEnd, spaceManager.getSequence().prevTime(i.getStart()));
            }
        }
        return new DataRange(lastEnd, Integer.MAX_VALUE);
    }

    public ArrayList<SelectionItem> getItemsInRange(long xStart, long xEnd)
    {
        return getItemsInRange(xStart, xEnd, null);
    }

    public ArrayList<SelectionItem> getItemsInRange(long xStart, long xEnd, SelectionItem excludeThis)
    {
        Collections.sort(items);
        ArrayList<SelectionItem> toReturn = new ArrayList<>();
        Iterator<SelectionItem> iter = items.iterator();
        while (iter.hasNext())
        {
            SelectionItem i = iter.next();
            if (i == excludeThis)
            {
                continue;
            }
            //patrzymy czy jest jakiś który zawiera lewą krawędź
            if (i.getStart() > xEnd)
            {
                //jesli ten jest poza to reszta i tak nie będzie
                return toReturn;
            }
            if (i.getEnd() >= xStart)
            {
                toReturn.add(i);
                break;
                //właśnie dodaliśmy pierwszego

            }
        }
        //teraz dodawajmy kolejne, chyba, że któryś już zawiera xEnd, to przerwij
        while (iter.hasNext())
        {
            SelectionItem i = iter.next();
            if (i == excludeThis)
            {
                continue;
            }
            if (i.getStart() > xEnd)
            {
                break;
            }
            toReturn.add(i);
            //
        }
        return toReturn;
    }
    //------------------- SELECTED ITEMS
    final public Selection selection = new Selection();

    public class Selection
    {

        private SelectionItem mainItem;
        private List<SelectionItem> items = new ArrayList<>(100);

        public void setMainItem(SelectionItem item)
        {
            deselectAll();
            mainItem = item;
            if (mainItem != null)
            {
                item.setSelected(true);
                items.add(item);
            }
            update();
        }

        public void setItems(List<SelectionItem> items)
        {
            for (SelectionItem item : this.items)
            {
                item.setSelected(false);
            }
            this.items.clear();

            if (items != null)
            {
                this.items.addAll(items);
            }
            for (SelectionItem item : this.items)
            {
                item.setSelected(true);
            }
            update();
        }

        public void addItem(SelectionItem item)
        {
            item.setSelected(true);
            items.add(item);
            update();
        }

        public void toggleItem(SelectionItem item)
        {
            if (!item.isSelected())
            {
                addItem(item);
            } else
            {
                removeItem(item);
            }
        }

        public void removeItem(SelectionItem item)
        {
            item.setSelected(false);
            items.remove(item);
            update();
        }

        public List<SelectionItem> getItems()
        {
            return items;
        }

        public void deselectAll()
        {
            if (mainItem != null)
            {
                mainItem.setSelected(false);
            }
            mainItem = null;
            for (SelectionItem item : items)
            {
                item.setSelected(false);
            }

            items.clear();

            update();
        }

        public void selectAll()
        {
            setItems(SelectionController.this.items);
        }

        private void update()
        {
            viewport.invalidate();
        }
    }
}
