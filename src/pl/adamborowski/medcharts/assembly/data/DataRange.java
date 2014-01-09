/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.data;

/**
 * Zakres danych wyrażany w jednostkach danych (tutaj milisekundy)
 * @author test
 */
public class DataRange
{
    //x start

    private long start;
    //co ile skakać
    private int resolution;

    public int getScale()
    {
        return resolution;
    }
    //zwraca długość zakresu jako różnica startu i końca (nie jest to żadna jakaś liczba elementów
    //ta długość jest wyrażana w jednostce rozdzielczości
    //długość przedziału wyrażana w krotności interwału aktualnego
    public long getLengthInIntervals(int interval)
    {
        //TODO REMOVE
        return (long) ((end - start) / resolution/interval);
    }
    public long getLengthInTime(){
        if(end==Long.MAX_VALUE||start==Long.MIN_VALUE){
            return Long.MAX_VALUE;
        }
        return (end-start);
    }
    //x end
    private long end;

    public long getStart()
    {
        return start;
    }

    public long getEnd()
    {
        return end;
    }
    public DataRange(long start, long end){
        this.start=start;
        this.end=end;
        this.resolution=1;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
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
        final DataRange other = (DataRange) obj;
        if (this.start != other.start)
        {
            return false;
        }
        if (this.resolution != other.resolution)
        {
            return false;
        }
        if (this.end != other.end)
        {
            return false;
        }
        return true;
    }
    public DataRange(long start, long end, int resolution)
    {
        this.start = start;
        this.end = end;
        
        this.resolution = (int) resolution;
    }
    public DataRange newBounded(long start, long end)
    {
        return new DataRange(start, end, resolution);
    }
}
