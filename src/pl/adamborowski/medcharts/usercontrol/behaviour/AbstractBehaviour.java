/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.usercontrol.behaviour;

import pl.adamborowski.medcharts.MedChart;
import pl.adamborowski.medcharts.renderers.RootRenderer;

/**
 * klasa bazowa dla "Zachowania", czyli przemieszczania obiektu, rozciągania,
 * rysowania, przesuwania, skalowania kontrolerowi można wtedy przypisać
 * konkretne zachowania w konkretnych momentach: LPM: zoomowanie prostokątnym
 * zaznaczeniem lub praca z obszarami (nowe, przesun, przeciagnij) RPM:
 * przesuwanie wykresu MPM: zoomowanie prostokątnym zaznaczeniem
 *
 * @author test
 */
public abstract class AbstractBehaviour
{
    public boolean canBehaveNow(StateVars vars)
    {
        return true;
    }

    protected MedChart medChart;
    protected RootRenderer view;

    public final void setView(RootRenderer view)
    {
        this.view = view;
    }

    public final void setMedChart(MedChart medChart)
    {
        this.medChart = medChart;
    }
    private boolean lock;

    final public void setLock(boolean lock)
    {
        this.lock = lock;
    }

    final public void initBehaviour(StateVars initVars)
    {
        this.init = initVars;
        if (!lock)
        {
            init();
        }
    }

    final public void updateBehaviour(StateVars currentVars)
    {
        this.current = currentVars;
        if (!lock)
        {
            update();
        }
    }

    final public void finishBehaviour(StateVars currentVars)
    {
        this.current = currentVars;
        if (!lock)
        {
            finish();
        }
    }

    final public void cancelBehaviour()
    {
        if (!lock)
        {
            cancel();
        }
    }

    protected void init(){}

    protected void update(){}

    protected void finish(){}

    protected void cancel(){}
    protected StateVars init;
    protected StateVars current;

    public static class DataVars
    {

        public long x;
        public float y;
    }

    public static class DisplayVars
    {

        public int x;
        public int y;
    }

    public static class SpaceVars
    {

        public final DataVars data = new DataVars();
        public final DisplayVars display = new DisplayVars();
    }

    public static class StateVars
    {

        public final SpaceVars mouse = new SpaceVars();
        public final SpaceVars center = new SpaceVars();
    }
}
