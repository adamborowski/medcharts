/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.data;

/**
 *
 * @author test
 */
public interface IDataCollection<TValue, TIter> extends  Iterable<TIter>
{
    IDataCollection<TValue, TIter> getSubCollection(DataRange range);
    long getLastX();
    long getFirstX();
    /**
     * Narprawia x tak, żeby pasował dokładnie do którejś próbki
     * @param x
     * @return 
     */
    long adjustX(long x);
    int getSize();
    TValue getY(long x);
    TValue[] getYData();
    long[] getXData();
    IDataCollection<TValue, TIter> create(TValue[] yData, long[] xData, DataRange sourceRange);
    TValue[] create(int size);
}
