/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.data;

import java.util.Iterator;
import pl.adamborowski.medcharts.assembly.data.OverlayLinesCollection.Iteration;
import pl.adamborowski.utils.BinarySearchClosestUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author test
 */
public class OverlayLinesCollection implements Iterable<Iteration>, IDataCollection<OverlayLinesCollection.Multipoint, OverlayLinesCollection.Iteration>
{

    private final DataRange sourceRange;
    private Multipoint[] yData;
    final int numLines;
    long startX;
    private final long[] xData;
    private long endX;

        public OverlayLinesCollection(Multipoint[] yData, long[] xData, DataRange sourceRange)
    {
        this.startX = xData[0];
        this.endX=xData[xData.length-1];
        this.yData = yData;
        this.xData=xData;
        this.sourceRange = sourceRange;
        if (yData.length == 0)
        {
            this.numLines = 0;
        } else
        {
            this.numLines = yData[0].linePoints.length;
        }
    }

    @Override
    public IDataCollection<Multipoint, OverlayLinesCollection.Iteration> getSubCollection(DataRange range)
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
//        Multipoint[] newYData = new Multipoint[newSize];
//
//        int sourceI = sourceStartIndex;
//        for (int i = 0; i < newSize; i++)
//        {
//            newYData[i] = yData[sourceI];
//            //
//            sourceI += relativeResolution;
//        }
//        final int newInterval = interval * relativeResolution;
//        final long newFirst = getXAt(sourceStartIndex);
//
//        return new OverlayLinesCollection(newFirst, newInterval, newYData, range);
    }

    @Override
    public long adjustX(long x)
    {
        int xF = (int) Math.round(getXPositionFloat(x));
        return getXAt(xF);
    }

    @Override
    public int getSize()
    {
        return yData.length;
    }

    @Override
    public IDataCollection<Multipoint, OverlayLinesCollection.Iteration> create(Multipoint[] yData, long[] xData, DataRange sourceRange)
    {
        return new OverlayLinesCollection(yData, xData, sourceRange);
    }

    
    
    @Override
    public Multipoint[] create(int size)
    {
        return new Multipoint[size];
    }

    @Override
    public Multipoint getY(long x)
    {
        return yData[getXPosition(x)];
    }

    @Override
    public long getLastX()
    {
        return endX;
    }

    @Override
    public long getFirstX()
    {
        return startX;
    }

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

        public int getXPosition(long x)
    {
        int index = BinarySearchClosestUtil.search(xData, 0, xData.length - 1, x);
        if (index < 0 || index > xData.length)
        {
            throw new IllegalArgumentException();
        }
        return index;
    }


    
    @Override
    public Multipoint[] getYData()
    {
        return yData;
    }

    public final long getXAt(int index)
    {
        return xData[index];
    }

    public int getNumLines()
    {
        return numLines;
    }

    @Override
    public Iterator<Iteration> iterator()
    {
        return new OverlayCollectionIterator();
    }

    @Override
    public long[] getXData()
    {
        return xData;
    }

    public static class Multipoint
    {

        public float[] linePoints;

        public Multipoint(int numPoints)
        {
            linePoints = new float[numPoints];
        }

        public static Multipoint[] create(int numPoints, int numLines)
        {
            Multipoint points[] = new Multipoint[numPoints];
            for (int i = 0; i < numPoints; i++)
            {
                points[i] = new Multipoint(numLines);
            }
            return points;
        }
    }

    public static class Iteration
    {
        public int i;
        public long x;
        public Multipoint y;
    }

    public class OverlayCollectionIterator implements Iterator<Iteration>
    {

        public OverlayCollectionIterator()
        {
            item = new Iteration();
            current = 0;
        }
        int current; // licznik iteracji
        int length = yData.length;
        Iteration item;

        @Override
        public boolean hasNext()
        {
            return current + 1 < yData.length;
        }

        @Override
        public Iteration next()
        {
            item.x = xData[current];
            item.y = yData[current];
            item.i=current;
            current++;
            return item;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
