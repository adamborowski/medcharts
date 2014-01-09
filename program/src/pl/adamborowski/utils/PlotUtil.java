/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

/**
 *
 * @author test
 */
public class PlotUtil
{

    public static class MajorMinorTick
    {

        public float major;
        public float minor;
        public int diffDigitPosition;
    }

    /**
     * ]
     *
     * @param containerHeight wysokość wykresu w jednostce danych
     * @param majorTickMinHeight wysokość labela w jednostce danych
     * @return
     */
    public static MajorMinorTick getVerticalMajorMinorTick(float containerHeight, float majorTickMinHeight)
    {
        //pozycja różniącego się znaku liczona od 0
        final float diffPosition = (float) Math.floor(Math.log10(majorTickMinHeight));
        final float diffMultiplier = (float) Math.pow(10, diffPosition);
        MajorMinorTick mmt = new MajorMinorTick();
        mmt.diffDigitPosition = (int) diffPosition;//(int) (majorTickMinHeight * diffPosition) % 10;
        if (majorTickMinHeight <= diffMultiplier)
        {
            mmt.major = diffMultiplier;
            mmt.minor = diffMultiplier * 0.5f;
        } else if (majorTickMinHeight <= 2 * diffMultiplier)
        {
            mmt.major = 2 * diffMultiplier;
            mmt.minor = diffMultiplier;
        } else if (majorTickMinHeight <= 5 * diffMultiplier)
        {
            mmt.major = 5 * diffMultiplier;
            mmt.minor = 2 * diffMultiplier;
        } else
        {
            mmt.major = 10 * diffMultiplier;
            mmt.minor = 5 * diffMultiplier;
            mmt.diffDigitPosition++;
        }
        return mmt;
    }
}
