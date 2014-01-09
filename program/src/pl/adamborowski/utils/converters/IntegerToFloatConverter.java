/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils.converters;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author test
 */
public class IntegerToFloatConverter extends Converter<Float, Integer>
{
    float factor;
    public IntegerToFloatConverter(float factor)
    {
        this.factor=factor;
    }

    @Override
    public Integer convertForward(Float value)
    {
        return (int)(value.intValue()*factor);
    }

    @Override
    public Float convertReverse(Integer value)
    {
        return value.floatValue()/factor;
    }
    
}
