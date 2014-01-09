/*
 * Klasa która trzyma wycięte dane, pozwala w efektywy a zarazem wygodny sposób
 * iterować poprzez dane
 */
package pl.adamborowski.medcharts.assembly.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import pl.adamborowski.utils.BinarySearchClosestUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author test
 */
public class DataCollection implements IDataCollection<Float, DataCollection.DataItem>
{

    private final DataRange sourceRange;
    private final long start;
    private final long end;
    private final Float[] yData;
    private final long[] xData;

    public DataCollection(Float[] yData, long[] xData, DataRange sourceRange)
    {
        start = xData[0];
        this.end = xData[xData.length - 1];
        this.yData = yData;
        this.xData = xData;
        this.sourceRange = sourceRange;
    }

    /**
     * zwraca podkolekcję, ale podany range.last obcina tak, aby pasowało do
     * interwału
     *
     * @param range
     * @return
     */
    @Override
    public DataCollection getSubCollection(DataRange range)
    {
        throw new NotImplementedException();
//        if (range.getScale() < sourceRange.getScale())
//        {
//            throw new SubCollectionException("Cannot get subcollection with better resolution than source.");
//        }
//        if (range.getScale() % sourceRange.getScale() != 0)
//        {
//            throw new SubCollectionException("Cannot get subcollection with resolution that is not integer multiplication of source resolution.");
//        }
//        if (range.getStart() < sourceRange.getStart() || range.getEnd() > sourceRange.getEnd())
//        {
//            throw new SubCollectionException("Given range is not included in collection range at all.");
//        }
//        //offset źródłowy startu jako odległość startów podzielonych przez interval oraz przez rozdzielczość źródła
//        final int sourceStartIndex = (int) Math.round(getXPositionFloat(range.getStart()));
//        final int sourceEndIndex = (int) Math.round(getXPositionFloat(range.getEnd()));
//        //rozdzielczość nowego względem starego
//        final int relativeResolution = range.getScale() / sourceRange.getScale();
//        final int sourceLength = sourceEndIndex - sourceStartIndex + 1;
//        final int newSize = sourceLength / relativeResolution;
//
//        Float[] newYData = new Float[newSize];
//
//        int sourceI = sourceStartIndex;
//        for (int i = 0; i < newSize; i++)
//        {
//            newYData[i] = yData[sourceI];
//            //
//            sourceI += relativeResolution;
//        }
//        final long newFirst = getXAt(sourceStartIndex);
//        
//        return new DataCollection(newFirst, newYData, range);
    }

    @Override
    public long adjustX(long x)
    {
        int xF = (int) Math.round(getXPositionFloat(x));
        // nie walimy wyjątku, bo ktoś chce tylko wyrównać niekoniecznie w tym zakresie
//        if(xF<0||xF>=yData.length)
//        {
//            throw new IllegalArgumentException("X is out of range");
//        }

        return getXAt(xF);
    }

    @Override
    public int getSize()
    {
        return yData.length;
    }

    @Override
    public IDataCollection<Float, DataItem> create( Float[] yData, long[] xData, DataRange sourceRange)
    {
        return new DataCollection(yData, xData, sourceRange);
    }

    @Override
    public Float[] create(int size)
    {
        return new Float[size];
    }

    public DataRange getSourceRange()
    {
        return sourceRange;
    }

    public int getLength()
    {
        return yData.length;
    }

    @Override
    public Float[] getYData()
    {
        return yData;
    }

    @Override
    public long getFirstX()
    {
        return start;
    }

    @Override
    public long getLastX()
    {
        return end;
    }

    public final long getXAt(int index)
    {
        return xData[index];
    }

    public void debugTrace()
    {
        System.out.println("Debug trace: " + start + " - " + getLastX());
        for (DataItem i : this)
        {
            System.out.println(i.i + ". x:" + i.x + "\ty:" + i.y);
        }
    }

    /**
     * Czas logarytmiczny, nie zaleca się używać ponieważ próbki nie leżą
     * równomiernie, ponieważ ciężko obliczyć uwzględniając sekwencje i skalę
     * jednocześnie
     *
     * @param x
     * @return
     */
    public int getXPosition(long x)
    {
        int index = BinarySearchClosestUtil.search(xData, 0, xData.length - 1, x);
        if (index < 0 || index > xData.length)
        {
            throw new IllegalArgumentException();
        }
        return index;
    }

    /**
     * Nie zaleca się używać, czas logarytmiczny
     *
     * @param x
     * @return
     */
    public double getXPositionFloat(long x)
    {
        int index = BinarySearchClosestUtil.search(xData, 0, xData.length - 1, x);
        if (index < 0 || index > xData.length)
        {
            throw new IllegalArgumentException();
        }
        long a = xData[index];
        long b = xData[index + 1];
        float midpoint = (b - a) / (float) (x - a);
        return a + midpoint;
    }

    /**
     * Nie zaleca się używać, czas logarytmiczny
     *
     * @param x
     * @return
     */
    @Override
    public Float getY(long x)
    {
        return yData[getXPosition(x)];
    }

    @Override
    public Iterator<DataItem> iterator()
    {
        return new DataIterator();
    }

    @Override
    public long[] getXData()
    {
        return xData;
    }

    public final static class DataItem
    {

        @Override
        public String toString()
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            return "[ @" + i + " (" + sdf.format(new Date(x)) + " -> " + y + " ]";
        }
       public long x;
        public float y;
        public int i;

    }

    private class DataIterator implements Iterator<DataItem>
    {

        private int current = 0;
        DataItem point = new DataItem();

        public DataIterator()
        {
            point.i = 0;
        }

        @Override
        public boolean hasNext()
        {
            return current + 1 < yData.length;
        }

        @Override
        public DataItem next()
        {
            point.x = xData[current];
            point.y = yData[current]==null?Float.NaN:yData[current];
            point.i = current;
            //
            current++;
            return point;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
