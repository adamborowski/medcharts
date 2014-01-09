/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

/**
 *
 * @author test
 */
public class GradientFactory
{
    private float[] fractions;
    private Color[] colors;

    public GradientFactory(float[] fractions, Color[] colors)
    {
        this.fractions = fractions;
        this.colors = colors;
    }

    public Paint getGradient(int x1, int y1, int x2, int y2)
    {
        return new LinearGradientPaint(x1, y1, x2, y2, fractions, colors);
    }
    
}
