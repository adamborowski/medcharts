/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

import pl.adamborowski.medcharts.assembly.data.DataSequence;

/**
 * obiekt pomocniczy przy MainImporterze. ustala min i max dla trybu oglądania >
 * 1 faza na pixel
 *
 * @author test
 */
public final class MainImportHelper
{

    private float[] maxValues;
    private float[] minValues;

    public void init(long start, long end, int phaseLength, DataSequence sequence)
    {
    }

    public float[] getMaxValues()
    {
        return maxValues;
    }

    public float[] getMinValues()
    {
        return minValues;
    }

    /**
     * W każdym odczycie danych powinna być wywołana ta metoda. która przesuwa
     * czas po sekwencji i jak doliczy czas treshold to dodaje do tablic max i
     * min i "zeruje" wartości min i max
     *
     * @param value
     */
    public void proceed(float value)
    {
    }
}
