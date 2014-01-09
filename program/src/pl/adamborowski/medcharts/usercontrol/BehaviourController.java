/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol;

import pl.adamborowski.medcharts.usercontrol.behaviour.AbstractBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.ZoomSelectedBehaviour;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JComponent;
import pl.adamborowski.medcharts.MedChart;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.Viewport;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.medcharts.renderers.RootRenderer;
import pl.adamborowski.medcharts.usercontrol.behaviour.DrawSelectionMaskBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.NewSelectionBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.ScrollDragBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.HoverSelectionBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.MoveSelectionBehaviour;
import pl.adamborowski.medcharts.usercontrol.behaviour.ResizeSelectionBehaviour;

/**
 *
 * @author test
 */
public class BehaviourController extends DefaultListener
{

    public static boolean IS_CONTROL = false;
    public static boolean IS_SHIFT = false;
    private RootRenderer viewRenderer;
    private MedChart medChart;
    //
    private ZoomSelectedBehaviour zoomSelectedBehaviour = new ZoomSelectedBehaviour();
    private ScrollDragBehaviour scrollDragBehaviour = new ScrollDragBehaviour();
    private NewSelectionBehaviour newSelectionBehaviour;
    private HoverSelectionBehaviour hoverSelectionBehaviour;
    private MoveSelectionBehaviour moveSelectionBehaviour;
    private ResizeSelectionBehaviour resizeSelectionBehaviour;
    private DrawSelectionMaskBehaviour drawSelectionMaskBehaviour;
    //
    private AbstractBehaviour currentBehaviour = null;
    private Viewport viewport;
    private SelectionController selectionController;

    @SuppressWarnings("LeakingThisInConstructor")
    public BehaviourController(Viewport viewport, RootRenderer viewRenderer, MedChart medChart)
    {
        initController(viewRenderer, medChart, viewport);
        initBehaviours(viewRenderer);

    }

    public AbstractBehaviour getCurrentBehaviour()
    {
        return currentBehaviour;
    }

    public boolean isWorking()
    {
        return currentBehaviour != null;
    }

    public JComponent getEventSource()
    {
        return viewport;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        medChart.dispatchEvent(e);// łatanie SWINGA
        createVars(e);
        if (currentBehaviour == null)
        {
            switch (e.getButton())
            {
                case MouseEvent.BUTTON3:
                    initBehaviour(zoomSelectedBehaviour, e);
                    break;
                case MouseEvent.BUTTON2:
                    initBehaviour(scrollDragBehaviour, e);
                    break;
                case MouseEvent.BUTTON1:
                    if (newSelectionBehaviour != null)
                    {

                        if (drawSelectionMaskBehaviour.canBehaveNow(currentVars))
                        {
                            initBehaviour(drawSelectionMaskBehaviour, e);
                        } else if (newSelectionBehaviour.canBehaveNow(currentVars))
                        {
                            initBehaviour(newSelectionBehaviour, e);
                        } else if (moveSelectionBehaviour.canBehaveNow(currentVars))
                        {
                            initBehaviour(moveSelectionBehaviour, e);
                        } else
                        {
                            initBehaviour(resizeSelectionBehaviour, e);
                        }
                    }
                    break;
            }
        }
    }
    AbstractBehaviour.StateVars currentVars;

    void createVars(MouseEvent e)
    {
        IS_CONTROL = e.isControlDown();
        IS_SHIFT = e.isShiftDown();
        currentVars = new AbstractBehaviour.StateVars();
        currentVars.mouse.display.x = e.getX();
        currentVars.mouse.display.y = e.getY();
        currentVars.center.display.x = viewRenderer.getWidth() / 2;
        currentVars.center.display.y = viewRenderer.getHeight() / 2;
        SpaceManager s = viewRenderer.sp();
        currentVars.mouse.data.x = s.toDataX(currentVars.mouse.display.x);
        currentVars.mouse.data.y = s.toDataY(currentVars.mouse.display.y);
        currentVars.center.data.x = s.toDataX(currentVars.center.display.x);
        currentVars.center.data.y = s.toDataY(currentVars.center.display.y);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        medChart.setHoveredViewport(viewport);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        finishCurrentBehaviour(e);
        mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        updateCurrentBehaviour(e);
    }

    private void initBehaviour(AbstractBehaviour behaviour, MouseEvent e)
    {
        createVars(e);
        currentBehaviour = behaviour;
        currentBehaviour.initBehaviour(currentVars);
    }

    private void updateCurrentBehaviour(MouseEvent e)
    {
        if (currentBehaviour != null)
        {
            createVars(e);
            currentBehaviour.updateBehaviour(currentVars);
        }
    }

    private void finishCurrentBehaviour(MouseEvent e)
    {
        if (currentBehaviour != null)
        {
            createVars(e);
            currentBehaviour.finishBehaviour(currentVars);
            currentBehaviour = null;
        }
    }

    public void cancelCurrentBehaviour()
    {
        if (currentBehaviour != null)
        {
            currentBehaviour.cancelBehaviour();
            currentBehaviour = null;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.isControlDown() || e.isShiftDown())
        {
            //tutaj dodaj zooma i doprowadź, żeby punkt pod myszą był nadal pod myszą!!
            float scaleX = medChart.getScaleX();
            float scaleY = viewRenderer.getScaleY();
            SpaceManager s = viewRenderer.sp();
            long xDataUnderCursor = s.toDataX(e.getX());
            float yDataUnderCursor = s.toDataY(e.getY());
            final float fraction = 1.5f;
            try
            {
                if (e.isShiftDown())
                {
                    medChart.setScaleX(scaleX * (e.getWheelRotation() > 0 ? fraction : 1 / fraction));
                }
                if (e.isControlDown() && !viewRenderer.isAutoScaleY())
                {
                    viewRenderer.setScaleY(scaleY * (e.getWheelRotation() > 0 ? fraction : 1 / fraction));
                }
                viewRenderer.invalidate();
                long newXDataUnderCursor = s.toDataX(e.getX());
                float newYDataUnderCursor = s.toDataY(e.getY());
                long xDiff = newXDataUnderCursor - xDataUnderCursor;
                float yDiff = newYDataUnderCursor - yDataUnderCursor;
                medChart.setDataX(medChart.getDataX() - xDiff);
                viewRenderer.setDataY(viewRenderer.getDataY() - yDiff);
            } catch (IllegalArgumentException error)
            {
                medChart.setScaleX(scaleX);
                viewRenderer.setScaleY(scaleY);
            }
        } else
        {
            medChart.setDataX(medChart.getDataX() + viewRenderer.sp().toDataWidth(70 * (e.getWheelRotation() < 0 ? -1 : 1)));
        }
        mouseDragged(e);
        mouseMoved(e);
    }

    private void configureBehaviour(AbstractBehaviour behaviour)
    {
        behaviour.setMedChart(medChart);
        behaviour.setView(viewRenderer);
    }

    private void initController(RootRenderer viewRenderer, MedChart medChart, Viewport viewport)
    {
        this.viewRenderer = viewRenderer;
        this.medChart = medChart;
        viewport.addMouseListener(this);
        viewport.addMouseMotionListener(this);
        viewport.addMouseWheelListener(this);
        this.viewport = viewport;
        selectionController = viewport.getSelectionController();
    }

    private void initBehaviours(RootRenderer viewRenderer)
    {
        //
        configureBehaviour(zoomSelectedBehaviour);
        configureBehaviour(scrollDragBehaviour);
        //
        if (selectionController != null)
        {
            newSelectionBehaviour = new NewSelectionBehaviour(selectionController);
            configureBehaviour(newSelectionBehaviour);

            hoverSelectionBehaviour = new HoverSelectionBehaviour(selectionController);
            configureBehaviour(hoverSelectionBehaviour);

            moveSelectionBehaviour = new MoveSelectionBehaviour(selectionController);
            configureBehaviour(moveSelectionBehaviour);

            resizeSelectionBehaviour = new ResizeSelectionBehaviour(selectionController);
            configureBehaviour(resizeSelectionBehaviour);
            
            drawSelectionMaskBehaviour = new DrawSelectionMaskBehaviour(selectionController);
            configureBehaviour(drawSelectionMaskBehaviour);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (hoverSelectionBehaviour != null)
        {
            createVars(e);
            hoverSelectionBehaviour.updateBehaviour(currentVars);
        }
       // viewRenderer.invalidate();
        medChart.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (hoverSelectionBehaviour != null)
        {
            createVars(e);
            hoverSelectionBehaviour.finishBehaviour(currentVars);
        }
        viewRenderer.invalidate();
    }

    public AbstractBehaviour.StateVars getStateVars()
    {
        return currentVars;
    }
}
