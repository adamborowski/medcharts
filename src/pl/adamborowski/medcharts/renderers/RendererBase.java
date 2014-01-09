/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Graphics2D;
import java.awt.Point;
import pl.adamborowski.medcharts.Viewport;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter.Mapping;

/**
 *
 * @author test
 */
public abstract class RendererBase<TBinding>
{

    protected final RootRenderer root;
    protected final RendererBase parent;
    protected final TBinding binding;
    protected final Mapping mapping;

    public final Point getMouse()
    {

        return root.getViewport().getMouse();
    }

    public final boolean isUnderMouse()
    {
        return root.getViewport().isUnderMouse();
    }

    public final Point getMouseScreen()
    {
        return root.getViewport().getMouseScreen();
    }

    public final Viewport getViewport()
    {
        return root.viewport;
    }

    public final boolean getPreferenceB(String name)
    {
        return (boolean) getViewport().getMedChart().getPreferences().get(name);
    }

    public final int getPreferenceI(String name)
    {
        return (int) getViewport().getMedChart().getPreferences().get(name);
    }

    Mapping getMapping()
    {
        return mapping;
    }

    public RendererBase(RendererBase parent, TBinding binding)
    {
        this.parent = parent;
        root = parent.getRoot();
        this.binding = binding;
        this.mapping = root.getMapping();
    }

    /**
     * Konstruktor dla viewRendererra, on podaje mapę oraz nie podaje parenta
     * tym bardziej roota
     *
     * @param binding
     * @param map
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public RendererBase(TBinding binding, Mapping mapping)
    {

        this.parent = null;
        root = (RootRenderer) this;
        this.binding = binding;
        this.mapping = mapping;
    }

    /**
     * Dla tych rendererów, które nie są w ogóle powiązane z jaxb (np
     * RectangleRenderer)
     *
     * @param parent
     */
    public RendererBase(RendererBase parent)
    {
        this(parent, null);
    }

    /**
     * niebezpieczy, nie ma między innymi możliwości korzystania z SpaceManagera
     */
    public RendererBase()
    {
        parent = null;
        root = null;
        binding = null;
        mapping = null;
    }

    public TBinding getBinding()
    {
        return binding;
    }

    public RootRenderer getRoot()
    {
        return root;
    }

    public RendererBase getParent()
    {
        return parent;
    }

    public final SpaceManager sp()
    {
        return root.getSpaceManager();
    }

    /**
     * .
     * domyślnie każdy renderer renderuje swoje dzieci. jeśli jakiś renderer
     * jest liściem, to niech nie woła super.render()
     *
     * @param g
     */
    public void render(Graphics2D g)
    {
    }

    protected SpaceManager getSpaceManager()
    {
        return root.getSpaceManager();
    }

    public void invalidate()
    {
        root.invalidate();
    }
}
