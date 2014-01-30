/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts;

import com.sun.java.swing.plaf.motif.MotifBorders;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.renderers.DataSelectionRenderer;
import pl.adamborowski.medcharts.renderers.SpaceManager;
import pl.adamborowski.medcharts.renderers.RootRenderer;
import pl.adamborowski.medcharts.usercontrol.BehaviourController;

/**
 * .
 * Viewport wyświetla pojedynczego MedCharta Jest komponentem wizualnym. Posiada
 * mouseControllera który ustawia głównego MedCharta!
 *
 * @author test
 */
public class Viewport extends JPanel
{

    public MedChart getMedChart()
    {
        return medChart;
    }

    public final Point getMouse()
    {
        return getMousePosition(true);
    }

    public final boolean isUnderMouse()
    {
        return getMousePosition(true) != null;
    }
    /**
     * Zwraca pozycję myszki, ale niezależną od tego, czy komponent jest wskazywany
     * @return 
     */
    public final Point getMouseScreen(){
        Point screenPos = MouseInfo.getPointerInfo().getLocation();
        Point viewportPos=this.getLocationOnScreen();
        screenPos.x-=viewportPos.x;
        screenPos.y-=viewportPos.y;
        return screenPos;
    }
    
    private RootRenderer viewRenderer;
    private BehaviourController mouseController;
    private final MedChart medChart;
    private SelectionController selectionController;
    private final Serie binding;

    @SuppressWarnings("LeakingThisInConstructor")
    public Viewport(MedChart medChart, Serie binding, AssemblyImporter.Mapping mapping)
    {
        this.medChart = medChart;
        this.binding = binding;
        setBorder(new EtchedBorder());
        SpaceManager spaceManager = new SpaceManager();

        if (binding != null && binding.getSelection() != null)
        {
            selectionController = SelectionController.getControllerForSelection(binding.getSelection(), spaceManager, this);
        }

        viewRenderer = new RootRenderer(binding, mapping, this, spaceManager, selectionController);
        spaceManager.setViewRenderer(viewRenderer);

        mouseController = new BehaviourController(this, viewRenderer, medChart);
    }

    public Viewport(MedChart medChart)
    {
        this.medChart = medChart;
        this.binding = null;
        SpaceManager spaceManager = new SpaceManager();
        viewRenderer = new RootRenderer(this, spaceManager);
        spaceManager.setViewRenderer(viewRenderer);

    }

    void init()
    {
        //TODO ODKOMENTOWAĆ TO!!
//        if (binding.getSelection() != null)
//        {
//            sc = SelectionController.getControllerForSelection(binding.getSelection(), this);
//            dsr = new DataSelectionRenderer(sc);
//            addChildVirtual(dsr);
//            //nie dodawaj go jako dziecka: addChild(dsr);
//            dsr.initialize();
//        }
    }

    public BehaviourController getBehaviourController()
    {
        return mouseController;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        viewRenderer.render((Graphics2D) g);
    }

    public RootRenderer getViewRenderer()
    {
        return viewRenderer;
    }

    public SelectionController getSelectionController()
    {
        return selectionController;
    }
}
