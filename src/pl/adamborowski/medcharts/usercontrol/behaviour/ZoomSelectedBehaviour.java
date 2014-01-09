/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import pl.adamborowski.medcharts.MedChart;
import pl.adamborowski.medcharts.renderers.MouseSelectionRenderer;

/**
 * w tej klasie będziemy kontrolować zaznaczanie obszaru myszką i zoomowanie
 * init: dodaj
 *
 * @author test
 */
public class ZoomSelectedBehaviour extends AbstractBehaviour
{

    private MouseSelectionRenderer selectionRenderer;

    @Override
    protected void init()
    {
        selectionRenderer = new MouseSelectionRenderer(view);
        //
        selectionRenderer.setX1(init.mouse.data.x);
        selectionRenderer.setX2(init.mouse.data.x + view.sp().toDataWidth(1));
        //
        selectionRenderer.setY1(init.mouse.data.y);
        selectionRenderer.setY2(init.mouse.data.y + view.sp().toDataHeight(1));
        view.setMsr(selectionRenderer);
        view.invalidate();
    }

    @Override
    protected void update()
    {
        selectionRenderer.setX2(current.mouse.data.x);
        selectionRenderer.setY2(current.mouse.data.y);
        view.invalidate();
    }

    @Override
    protected void finish()
    {
        selectionRenderer = null;
        //obliczenia nowej skali i położenia całego medcharta
        int displayWidth = Math.abs(current.mouse.display.x - init.mouse.display.x);
        int displayHeight = Math.abs(current.mouse.display.y - init.mouse.display.y);
        float dataWidth = Math.abs(current.mouse.data.x - init.mouse.data.x);
        float scaleX = dataWidth / (float) view.getWidth();
        long dataX = (current.mouse.data.x + init.mouse.data.x) / 2;
        //
        float dataHeight = Math.abs(init.mouse.data.y - current.mouse.data.y);
        if (displayWidth > 2 && displayHeight > 2)
        {
            float scaleY = dataHeight / view.sp().getDataMaxModule() / 2.0f;
            float dataY = (current.mouse.data.y + init.mouse.data.y) / 2.0f;
            //
            view.setAutoScaleY(false);
            medChart.setDataX(dataX);
            view.setDataY(dataY);
            medChart.setScaleX(scaleX);
            view.setScaleY(scaleY);
        }
        view.setMsr(null);
        //
    }

    @Override
    protected void cancel()
    {
        selectionRenderer = null;
        view.setMsr(null);
        view.invalidate();
    }
}
