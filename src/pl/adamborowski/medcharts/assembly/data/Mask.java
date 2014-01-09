/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.data;

import java.util.Arrays;
import java.util.Iterator;
import pl.adamborowski.medcharts.assembly.data.Mask.Item;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.utils.interfaces.IRange;

/**
 *
 * @author test
 */
public class Mask implements Iterable<Item> 
{
    
//    private Assembly.Serie.Overlay.Column binding;
//
//    /**
//     * Get the value of binding
//     *
//     * @return the value of binding
//     */
//    public Assembly.Serie.Overlay.Column getBinding()
//    {
//        return binding;
//    }
//
//    /**
//     * Set the value of binding
//     *
//     * @param binding new value of binding
//     */
//    public void setBinding(Assembly.Serie.Overlay.Column binding)
//    {
//        this.binding = binding;
//    }

    public Mask(int numItems)
    {
        items = new Item[numItems];
    }

    @Override
    public Iterator<Item> iterator()
    {
        return new Iter();
    }

    public class Item implements Comparable<Item>, IRange<Long>
    {

        private long start;
        private long end;

        @Override
        public int compareTo(Item o)
        {
            return Long.compare(start, o.start);
        }

        @Override
        public Long getStart()
        {
            return start;
        }

        @Override
        public Long getEnd()
        {
            return end;
        }
    }
    private final Item[] items;

    public Item[] getItems()
    {
        return items;
    }
    private int newInsertIndex = 0;

    public void addItem(long start, long end)
    {
        Item item = new Item();
        item.start = start;
        item.end = end;
        items[newInsertIndex] = item;
        newInsertIndex++;
    }

    public void sort()
    {
        Arrays.sort(items);
    }

    class Iter implements Iterator<Item>
    {

        int i = 0;
        int length = items.length;

        @Override
        public boolean hasNext()
        {
            return i + 1 < length;
        }

        @Override
        public Item next()
        {
            return items[i++];
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    // dana typu maska, można pobrać początek, koniec etc
}
