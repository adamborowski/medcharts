/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import pl.adamborowski.utils.GradientFactory;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import pl.adamborowski.utils.PaintUtil;

/**
 *
 * @author test
 */
public class SelectionItemPaint
{

    private static final float hoverAlpha = 0.8f;
    public static final GradientFactory GHOST = new GradientFactory(new float[]
    {
        0, 1
    }, new Color[]
    {
        new Color(0xaaf1e767, true),
        new Color(0xaafeb645, true)
    });
    public static final GradientFactory NORMAL = new GradientFactory(new float[]
    {
        0, 0.3f,  1
    }, new Color[]
    {
        PaintUtil.createColor(0x95BA2F, hoverAlpha),
        PaintUtil.createColor(0x8AC710, hoverAlpha),
        PaintUtil.createColor(0x97DE2C, hoverAlpha)
    });
    public static final GradientFactory HOVERED = new GradientFactory(new float[]
    {
        0, 0.3f,  1
    }, new Color[]
    {
        PaintUtil.createColor(0xdbf043, hoverAlpha),
        PaintUtil.createColor(0xc3d825, hoverAlpha),
        PaintUtil.createColor(0xe6f0a3, hoverAlpha)
    });
    public static final GradientFactory DOWN = new GradientFactory(new float[]
    {
        0, 0.3f,  1
    }, new Color[]
    {
        PaintUtil.createColor(0xdbf043, hoverAlpha),
        PaintUtil.createColor(0xb4c722, hoverAlpha),
        PaintUtil.createColor(0xcade3e, hoverAlpha)
    });
    public static final GradientFactory SELECTED = new GradientFactory(new float[]
    {
        0, 0.3f,  1
    }, new Color[]
    {
        PaintUtil.createColor(0xf07146, hoverAlpha),
        PaintUtil.createColor(0xe55b2b, hoverAlpha),
        PaintUtil.createColor(0xf2825b, hoverAlpha)
    });
}
