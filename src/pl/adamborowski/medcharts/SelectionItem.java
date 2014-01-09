/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts;

import java.io.Serializable;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import java.util.ArrayList;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.utils.interfaces.IRange;

/**
 *
 * @author test
 */
public class SelectionItem implements Comparable<SelectionItem>, IRange<Long>, Serializable
{

    private static int itemIdFactory = 0;
    transient public static DataRange freeRange;
    transient private boolean collisionDetected = false;
//getter jednorazowego użytku
    transient private SelectionController selectionController;
    private int id = itemIdFactory++;
    transient SelectionController.ItemState state = SelectionController.ItemState.GHOST;
    transient private boolean selected;
    long start;
    long end;
    final boolean ENABLE_TRACE = false;

    public SelectionItem(SelectionController selectionController)
    {
        this.selectionController = selectionController;
    }
/**
 * konstruktor tylko dla overlayImprtera, który chce ten obiekt zserializować do pliku
 * @param start
 * @param end 
 */
    public SelectionItem(long start, long end)
    {
        this.selectionController = null;
        this.start = start;
        this.end = end;
    }

    public void __deserialize__(SelectionController selectionController)
    {
        this.selectionController = selectionController;
        state = SelectionController.ItemState.NORMAL;
    }

    public boolean checkCollision()
    {
        boolean cd = collisionDetected;
        collisionDetected = false;
        return cd;
    }

    @Override
    public Object clone()
    {
        SelectionItem cl = new SelectionItem(selectionController);
        cl.start = start;
        cl.end = end;
        cl.selected = selected;
        cl.state = state;
        return cl;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public SelectionController.ItemState getState()
    {
        return state;
    }

    public int getId()
    {
        return id;
    }

    public void setState(SelectionController.ItemState state)
    {
        this.state = state;
    }

    public void fixBounds()
    {
        long tmp = start;
        if (start > end)
        {
            start = end;
            end = tmp;
        }
    }

    public Long getStart()
    {
        return Math.min(start, end);
    }

    public void setStart(long start)
    {
        this.start = start;
    }

    public boolean contains(long xData)
    {
        return xData > getStart() && xData < getEnd();
    }

    public Long getEnd()
    {
        return Math.max(start, end);
    }

    public void setEnd(long end)
    {
        this.end = end;
    }

    public void setPosition(long newStart)
    {
        long width = getEnd() - getStart();
        start = newStart;
        end = newStart + width;
    }

    public void setBound(boolean rightBound, long value)
    {
        if (rightBound)
        {
            end = value;
        } else
        {
            start = value;
        }
    }

    final void trace(String str)
    {
        if (ENABLE_TRACE)
        {
            System.out.println("move: " + str);
        }
    }

    public void tryMove(long newStart, long mousePos)
    {
        final long deltaX = newStart - getStart();
        final boolean rightDirection = deltaX > 0;
        ArrayList<SelectionItem> items;
        final DataSequence ds = selectionController.getSpaceManager().getSequence();
        //najpierw sprawdź, czy tam gdzie chcę się przesunąć, jest cokolwiek!!
        items = selectionController.getItemsInRange(newStart, newStart + getWidth(), this);
        if (items.isEmpty())
        {
            setPosition(newStart);
            collisionDetected = false;
            trace("obszar wolny, mozna przemieścić");
            return;
        }



        if (rightDirection)
        {
            items = selectionController.getItemsInRange(getStart(), getEnd() + deltaX, this);
        } else
        {
            items = selectionController.getItemsInRange(getStart() + deltaX, getEnd(), this);
        }
        //pierwszy element to będzie ten item, następny to ten co nam przeszkadza
        if (items.size() > 0)
        {
            long newStartGood;
            //jeśli ktoś jest w tym przedziale, to zobacz jakie jest wolne pole pod myszką
            freeRange = selectionController.getRangeOfFreeSpaceUnderPoint(mousePos, this);
            if (freeRange != null && freeRange.getLengthInTime() >= getWidth())
            {
                trace("obszar zajęty, ale przesuwam do najbliższego złączonego wolnego");
                //można włożyć
                //musisz przykleic niezależnie od kierunku,tylko czy mysz jest w pierwszej połowie przedziału
                newStartGood = selectionController.getSpaceManager().fitToSequence(newStart);
                if (newStartGood < freeRange.getStart())
                {
                    newStartGood = freeRange.getStart();
                } else if (newStartGood + getWidth() > freeRange.getEnd())
                {
                    newStartGood = freeRange.getEnd() - getWidth();
                }
            } else
            {
                trace("obszar zajęty, brak połączonego wolnego, zmuszam do pozostania");
                if (rightDirection)
                {

                    newStartGood = ds.prevTime(items.get(0).getStart() - getWidth());
                } else
                {
                    //w lewo
                    newStartGood = ds.nextTime(items.get(items.size() - 1).getEnd());
                }

            }
            setPosition(newStartGood);
            collisionDetected = true;
        } else
        {
            setPosition(newStart);
            collisionDetected = false;
        }

    }

    public void tryResize(boolean rightEdge, long newBound)
    {
        DataSequence ds = selectionController.getSpaceManager().getSequence();
        final long edgeX = rightEdge ? getEnd() : getStart();
        final long fixedEdgeX = rightEdge ? getStart() : getEnd();
        final long deltaX = newBound - edgeX;
        final boolean rightDirection = deltaX > 0;
        //
        final long rangeStart = rightDirection ? fixedEdgeX : newBound;
        final long rangeEnd = !rightDirection ? fixedEdgeX : newBound;

        ArrayList<SelectionItem> items = selectionController.getItemsInRange(rangeStart, rangeEnd);
        final int numIgnoring = this == selectionController.getGhost() ? 0 : 1;
        if (items.size() > numIgnoring)
        {
            long newBoundGood;
            if (rightDirection)
            {
                newBoundGood = ds.prevTime(items.get(numIgnoring).getStart());
            } else
            {
                //w lewo
                newBoundGood = ds.nextTime(items.get(items.size() - numIgnoring - 1).getEnd());
            }
            setBound(start < end ? rightDirection : !rightDirection, newBoundGood);
            collisionDetected = true;
        } else
        {
            setBound(rightEdge, newBound);
            collisionDetected = false;
        }
    }

    @Override
    public int compareTo(SelectionItem o)
    {
        // liczba pokazuje kto zostaje pokonany (1 to prawy, -1 to lewy - jak na osi)
        return Long.compare(getStart(), o.getStart());
    }
    //funkcja porównuje, czy zaznaczenie jest z jego lewej strony czy prawej

    public int compareTo(Long o)
    {
        if (getStart() <= o && getEnd() >= o)
        {
            return 0;
        }
        if (getStart() < o)
        {
            return -1;
        }
        return 1;
    }

    long getWidth()
    {
        return Math.abs(start - end);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 11 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SelectionItem other = (SelectionItem) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }
}
