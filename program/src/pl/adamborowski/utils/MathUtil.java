/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

/**
 *
 * @author test
 */
public class MathUtil
{

    public static long floor(long number, int multiplier)
    {
        return (number / multiplier) * multiplier;
    }

    public static double floor(float number, float multiplier)
    {
        return Math.floor(number / multiplier) * multiplier;
    }

    public static double ceil(float number, float multiplier)
    {
        return Math.ceil((float) number / (float) multiplier) * multiplier;
    }
}
