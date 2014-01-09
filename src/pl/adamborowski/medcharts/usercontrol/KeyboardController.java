/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import pl.adamborowski.medcharts.MedChart;
import pl.adamborowski.medcharts.Viewport;

/**
 *
 * @author test
 */
public class KeyboardController extends DefaultListener
{

    private final MedChart medChart;

    @SuppressWarnings("LeakingThisInConstructor")
    public KeyboardController(MedChart medChart)
    {
        this.medChart = medChart;
        medChart.addKeyListener(this);
        medChart.addMouseListener(this);
        medChart.setFocusable(true);
        medChart.setRequestFocusEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        medChart.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            for (Viewport viewport : medChart.getViewports())
            {
                viewport.getBehaviourController().cancelCurrentBehaviour();
            }
        }
//        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
//        {
//            for (Viewport viewport : medChart.getViewports())
//            {
//                viewport.getViewRenderer().setAutoScaleY(true);
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }
}
