/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter.Mapping;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;

/**
 *
 * @author test
 */
abstract class ViewRendererBase extends RendererBase<Serie>
{

    public ViewRendererBase(Serie binding, Mapping mapping)
    {
        super(binding, mapping);
    }


    private transient final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }
    //
    private float scaleX = 1;
    public static final String PROP_SCALEX = "scaleX";

    /**
     * Get the value of scaleX
     *
     * @return the value of scaleX
     */
    public float getScaleX()
    {
        return scaleX;
    }
public static final float MIN_SCALE_X=0.031118091f;
    /**
     * Set the value of scaleX
     *
     * @param scaleX new value of scaleX
     */
    public void setScaleX(float scaleX)
    {
        if (scaleX < MIN_SCALE_X)
        {
            scaleX=MIN_SCALE_X;
        }
        if(Float.isNaN(scaleX))
        {
            scaleX=1;
        }
        float oldScaleX = this.scaleX;
        this.scaleX = scaleX;
        firePropertyChange(PROP_SCALEX, oldScaleX, scaleX);
        invalidate();
    }
    private float scaleY = 1;
    public static final String PROP_SCALEY = "scaleY";

    /**
     * Get the value of scaleY
     *
     * @return the value of scaleY
     */
    public float getScaleY()
    {
        return scaleY;
    }
    private boolean autoScaleY=true;
    public static final String PROP_AUTOSCALEY = "autoScaleY";

    /**
     * Get the value of autoScaleY
     *
     * @return the value of autoScaleY
     */
    public boolean isAutoScaleY()
    {
        return autoScaleY;
    }

    /**
     * Set the value of autoScaleY
     *
     * @param autoScaleY new value of autoScaleY
     */
    public void setAutoScaleY(boolean autoScaleY)
    {
        boolean oldAutoScaleY = this.autoScaleY;
        this.autoScaleY = autoScaleY;
        firePropertyChange(PROP_AUTOSCALEY, oldAutoScaleY, autoScaleY);
        invalidate();
    }

    /**
     * Set the value of scaleY
     *
     * @param scaleY new value of scaleY
     */
    public void setScaleY(float scaleY)
    {
        if(Float.isNaN(scaleY))
        {
            scaleY=1;
        }
        float oldScaleY = this.scaleY;
        this.scaleY = scaleY;
        firePropertyChange(PROP_SCALEY, oldScaleY, scaleY);
        invalidate();
    }
    private long x = 0;
    public static final String PROP_X = "dataX";

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public long getDataX()
    {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setDataX(long x)
    {
        long oldX = this.x;
        this.x = x;
        firePropertyChange(PROP_X, oldX, x);
        invalidate();
    }
    private float y = 0;
    public static final String PROP_Y = "dataY";

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public float getDataY()
    {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setDataY(float y)
    {
        float oldY = this.y;
        this.y = y;
        firePropertyChange(PROP_Y, oldY, y);
        invalidate();
    }

    protected void firePropertyChange(String propName, Object oldValue, Object newValue)
    {
        pcs.firePropertyChange(propName, oldValue, newValue);
    }

    public abstract void invalidate();
}
