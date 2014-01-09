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
public class IntegerToLongConverter extends Converter<Long, Integer>
{

    public IntegerToLongConverter()
    {
    }

    @Override
    public Integer convertForward(Long value)
    {
        return value.intValue();
    }

    @Override
    public Long convertReverse(Integer value)
    {
        return value.longValue();
    }
    
}
