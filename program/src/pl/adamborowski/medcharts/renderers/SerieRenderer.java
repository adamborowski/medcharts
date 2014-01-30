/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import pl.adamborowski.medcharts.MedChartPreferences;
import pl.adamborowski.medcharts.assembly.data.DataCollection;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Overlay;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import pl.adamborowski.medcharts.assembly.reading.OverlayReader;
import pl.adamborowski.utils.DateUtil;
import pl.adamborowski.utils.PaintUtil;
import pl.adamborowski.utils.builders.HierarchyBuilder;

/**
 *
 * @author test
 */
public class SerieRenderer extends DataRendererBase<Float, Serie, DataCollection.DataItem> {

    private int y;
    private int height;
    private Line2D.Float line;
    private OverlayRenderer overlay;
    private LabelRenderer title;
    //new-style serie renderer
    private final pl.adamborowski.medcharts.data.SerieRenderer serieRenderer;

    /**
     * W konstruktorze należy utworzyć sub renderery na postawie bindingu
     *
     * @param reader
     * @param parent
     * @param binding
     */
    public SerieRenderer(IDataReader reader, RendererBase parent, Serie binding) {
        super(reader, parent, binding);
        title = new LabelRenderer(binding.getTitle(), 10, 10);
        title.setForegroundColor(Color.decode(binding.getColor()));

        if (binding.getOverlay().isEmpty() == false) {
            Overlay ov = binding.getOverlay().get(0);
            overlay = new OverlayRenderer((OverlayReader) mapping.readerFor(ov), this, ov);
        }
        hintRenderer.setBackgroundColor(PaintUtil.createColor(0xfcfcfc, 0.94f));
        hintRenderer.setForegroundColor(title.getForegroundColor());
        hintRenderer.setBorderColor(title.getBorderColor());
        hintRenderer.setRadius(6);
        serieRenderer = HierarchyBuilder.buildRendererHierarchyFromReaderHierarchy(((MainReader) this.reader).serieReader, sp());
//        this.aggregationRenderer=

    }
    private LabelRenderer hintRenderer = new LabelRenderer("", 0, 0);
    private static DecimalFormat df = new DecimalFormat("#.#");

    static {
    }
    int bestDistance = Integer.MAX_VALUE;
    long bestProbe = 0;
    float bestValue = 0;
    int bestY = 0;
    int bestX = 0;
    boolean showHint = false;
    int mouseX = 0;
    int distance;

    @Override
    public void render(Graphics2D g) {

//        g.setPaint(Color.MAGENTA);
//        System.out.println("circ x:" + (int) sp().toDisplayX(sp().getDataX()));
//        g.fillArc((int) sp().toDisplayX(sp().getDataX()), (int) sp().toDisplayY(sp().getDataY()), 20, 20, 0, 360);
        g.setPaint(Color.decode(binding.getColor()));

//        line = new Line2D.Float();
        int x1, y1, x2, y2;
        x2 = 0; //(int) sp().toDisplayX(currentScope.data.getXData()[0]);
        y2 = 0; //(int) sp().toDisplayY(currentScope.data.getYData()[0]);
        final int p = 2, p2 = 2 * p;
        final int pointsSpace = 2;
        final boolean drawPoitns = sp().getScaleX() < sp().getSequence().getA() / (float) (p2 + pointsSpace);
        // wyznacz najbliższą próbkę
        bestDistance = Integer.MAX_VALUE;
        bestProbe = 0;
        bestValue = 0;
        bestY = 0;
        bestX = 0;
//        showHint = false;
        switch (getPreferenceI(MedChartPreferences.SHOW_HINT)) {
            case MedChartPreferences.SHOW_HINT_ALL:
                showHint = true;
                break;
            case MedChartPreferences.SHOW_HINT_CURRENT:
                showHint = isUnderMouse();
                break;
            default:
                showHint = false;
        }
        mouseX = getMouseScreen().x;
//        Point mouse = getMouse();
//        if (mouse != null)
//        {
//            showHint = true;
//            mouseX = getMouse().x;
//        } else
//        {
//            mouseX = 0;
//        }
        //
        serieRenderer.render(g);
//                                                                                                for (DataCollection.DataItem i : currentScope.data) {
//                                                                                                    newX = (int) sp().toDisplayX(i.x);
//                                                                                                    if (newX == x2 && i.i != 0) {
//                                                                                                        //metoda odczytu: czytaj dla każdego piksela 
//                                                                                                        //ma jedną nieistotną cechę: przy dużym powiększeniu renderuje
//                                                                                                        //kilka próbek dla tego samego x (bo jak zrobi fitToSequence 
//                                                                                                        //to moze sie okazac ten sam indeks próbki)
//
//                                                                                                        continue;
//                                                                                                    }
//                                                                                                    x1 = x2;
//                                                                                                    y1 = y2;
//                                                                                                    x2 = newX;
//                                                                                                    y2 = (int) sp().toDisplayY(i.y);
//                                                                                                    g.drawLine(x1, y1, x2, y2);
//                                                                                                    if (drawPoitns) {
//                                                                                                        //rysuj punkciek
//                                                                                                        g.drawArc(x2 - p, y2 - p, p2, p2, 0, 360);
//                                                                                                    }
//                                                                                                    if (showHint) {
//                                                                                                        distance = Math.abs(x2 - mouseX);
//                                                                                                        if (distance < bestDistance) {
//                                                                                                            bestDistance = distance;
//                                                                                                            bestProbe = i.x;
//                                                                                                            bestValue = i.y;
//                                                                                                            bestX = x2;
//                                                                                                            bestY = y2;
//                                                                                                        }
//                                                                                                    }
//                                                                                                }
        // <editor-fold defaultstate="collapsed" desc="inne sposoby renderowania (stare)">

//        int counter = 0;
//        for (Pixel pixel : currentScope)
//        {
//            if (pixel.value == null)
//            {
//                continue;
//            }
//            line.x1 = line.x2;
//            line.y1 = line.y2;
//            
//            line.x2 = pixel.displayX;
//            line.y2 = sp().toDisplayY(pixel.value);
//            if (line.y2 > sp().getHeight())
//            {
//                line.y2 = sp().getHeight();
//            } else if (line.y2 < 0)
//            {
//                line.y2 = 0;
//            }
//            
//            g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
//            counter++;
//        }
//        TimeUtil.start("metoda po tablicy data");
//        
//        line.x2 = currentScope.firstPixel;
//        line.y2 = sp().toDisplayY(currentScope.pixels[0]);
//        Float[] values = data.getYData();
//        long xPos = data.getFirstX();
//        for (int i = 0; i < values.length; i++)
//        {
//            int displayX = (int) sp().toDisplayX(xPos);
//            int displayY = (int) sp().toDisplayY(values[i]);
//
//
//            line.x1 = line.x2;
//            line.y1 = line.y2;
//            line.x2 = displayX;
//            line.y2 = displayY;
//
//            g.draw(line);
//
//            xPos += data.getInterval();
//        }
//        TimeUtil.stop();
//        TimeUtil.start("metoda po currentScope");
//        g.setPaint(Color.red);
//        line.x2 = currentScope.firstPixel;
//        line.y2 = sp().toDisplayY(currentScope.pixels[0]);
//        
//        
//        
//        
        /*
         //        TimeUtil.stop();
         //
         //
         //
         //        float incrementor = 1.0f;
         //        if (sp().getIntervalsPerPixel() < 1)
         //        {
         //            incrementor = 1 / sp().getIntervalsPerPixel();
         //        }
         //        line = new Line2D.Float();
         //        //od jakiego x zaczynamy
         //        int startingPixel = (int) Math.ceil(sp().toDisplayX(sp().visible.firstAccessible)); // ceil bo wczesniejszego pixela nie ma
         //        int endingPixel = (int) sp().toDisplayX(sp().visible.lastAccessible);
         //        line.x2 = startingPixel;
         //        line.y2 = sp().toDisplayY(data.getY(sp().toDataX(startingPixel)));
         //        for (float i = startingPixel + incrementor - 1; i < endingPixel + incrementor - 1; i += incrementor)
         //        {
         //            long iDataX = sp().toDataX(i);
         //            line.x1 = line.x2;
         //            line.y1 = line.y2;
         //            line.x2 = i;
         //            line.y2 = sp().toDisplayY(data.getY(iDataX));
         //            g.draw(line);
         //            iDataX += sp().getDataPerPixel();
         //        }
         */
        // </editor-fold>
        if (overlay != null) {
            overlay.render(g);
        }
        title.render(g);
    }
//    @Override
//    public void render(Graphics2D g)
//    {
//        if (currentScope.willRender() == false)
//        {
//            return;
//        }
//
//
////        g.setPaint(Color.MAGENTA);
////        System.out.println("circ x:" + (int) sp().toDisplayX(sp().getDataX()));
////        g.fillArc((int) sp().toDisplayX(sp().getDataX()), (int) sp().toDisplayY(sp().getDataY()), 20, 20, 0, 360);
//        g.setPaint(Color.decode(binding.getColor()));
//
////        line = new Line2D.Float();
//        int x1, y1, x2, y2;
//        x2 = (int) sp().toDisplayX(currentScope.data.getXData()[0]);
//        y2 = (int) sp().toDisplayY(currentScope.data.getYData()[0]);
//        final int p = 2, p2 = 2 * p;
//        final int pointsSpace = 2;
//        final boolean drawPoitns = sp().getScaleX() < sp().getSequence().getA() / (float) (p2 + pointsSpace);
//        // wyznacz najbliższą próbkę
//        bestDistance = Integer.MAX_VALUE;
//        bestProbe = 0;
//        bestValue = 0;
//        bestY = 0;
//        bestX = 0;
////        showHint = false;
//        switch (getPreferenceI(MedChartPreferences.SHOW_HINT))
//        {
//            case MedChartPreferences.SHOW_HINT_ALL:
//                showHint = true;
//                break;
//            case MedChartPreferences.SHOW_HINT_CURRENT:
//                showHint = isUnderMouse();
//                break;
//            default:
//                showHint = false;
//        }
//        mouseX = getMouseScreen().x;
////        Point mouse = getMouse();
////        if (mouse != null)
////        {
////            showHint = true;
////            mouseX = getMouse().x;
////        } else
////        {
////            mouseX = 0;
////        }
//        int newX;
//        //
//        for (DataCollection.DataItem i : currentScope.data)
//        {
//            newX = (int) sp().toDisplayX(i.x);
//            if (newX == x2 && i.i != 0)
//            {
//                //metoda odczytu: czytaj dla każdego piksela 
//                //ma jedną nieistotną cechę: przy dużym powiększeniu renderuje
//                //kilka próbek dla tego samego x (bo jak zrobi fitToSequence 
//                //to moze sie okazac ten sam indeks próbki)
//
//                continue;
//            }
//            x1 = x2;
//            y1 = y2;
//            x2 = newX;
//            y2 = (int) sp().toDisplayY(i.y);
////            if (true)//rysuj blokami
////            {
////                int ax=Math.min(x1, x2);
////                int ay=Math.min(y1, y2);
////                int aw=Math.abs(x1-x2)+1;
////                int ah=Math.abs(y1-y2)+1;
////                g.fillRect(ax, ay, aw, ah);
////            } else
////            {
//                g.drawLine(x1, y1, x2, y2);
////            }
//            if (drawPoitns)
//            {
//                //rysuj punkciek
//                g.drawArc(x2 - p, y2 - p, p2, p2, 0, 360);
//            }
//            if (showHint)
//            {
//                distance = Math.abs(x2 - mouseX);
//                if (distance < bestDistance)
//                {
//                    bestDistance = distance;
//                    bestProbe = i.x;
//                    bestValue = i.y;
//                    bestX = x2;
//                    bestY = y2;
//                }
//            }
//        }
//        // <editor-fold defaultstate="collapsed" desc="inne sposoby renderowania (stare)">
//
//
////        int counter = 0;
////        for (Pixel pixel : currentScope)
////        {
////            if (pixel.value == null)
////            {
////                continue;
////            }
////            line.x1 = line.x2;
////            line.y1 = line.y2;
////            
////            line.x2 = pixel.displayX;
////            line.y2 = sp().toDisplayY(pixel.value);
////            if (line.y2 > sp().getHeight())
////            {
////                line.y2 = sp().getHeight();
////            } else if (line.y2 < 0)
////            {
////                line.y2 = 0;
////            }
////            
////            g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
////            counter++;
////        }
//
//
////        TimeUtil.start("metoda po tablicy data");
////        
////        line.x2 = currentScope.firstPixel;
////        line.y2 = sp().toDisplayY(currentScope.pixels[0]);
////        Float[] values = data.getYData();
////        long xPos = data.getFirstX();
////        for (int i = 0; i < values.length; i++)
////        {
////            int displayX = (int) sp().toDisplayX(xPos);
////            int displayY = (int) sp().toDisplayY(values[i]);
////
////
////            line.x1 = line.x2;
////            line.y1 = line.y2;
////            line.x2 = displayX;
////            line.y2 = displayY;
////
////            g.draw(line);
////
////            xPos += data.getInterval();
////        }
////        TimeUtil.stop();
////        TimeUtil.start("metoda po currentScope");
////        g.setPaint(Color.red);
////        line.x2 = currentScope.firstPixel;
////        line.y2 = sp().toDisplayY(currentScope.pixels[0]);
////        
////        
////        
////        
//        /*
//         //        TimeUtil.stop();
//         //
//         //
//         //
//         //        float incrementor = 1.0f;
//         //        if (sp().getIntervalsPerPixel() < 1)
//         //        {
//         //            incrementor = 1 / sp().getIntervalsPerPixel();
//         //        }
//         //        line = new Line2D.Float();
//         //        //od jakiego x zaczynamy
//         //        int startingPixel = (int) Math.ceil(sp().toDisplayX(sp().visible.firstAccessible)); // ceil bo wczesniejszego pixela nie ma
//         //        int endingPixel = (int) sp().toDisplayX(sp().visible.lastAccessible);
//         //        line.x2 = startingPixel;
//         //        line.y2 = sp().toDisplayY(data.getY(sp().toDataX(startingPixel)));
//         //        for (float i = startingPixel + incrementor - 1; i < endingPixel + incrementor - 1; i += incrementor)
//         //        {
//         //            long iDataX = sp().toDataX(i);
//         //            line.x1 = line.x2;
//         //            line.y1 = line.y2;
//         //            line.x2 = i;
//         //            line.y2 = sp().toDisplayY(data.getY(iDataX));
//         //            g.draw(line);
//         //            iDataX += sp().getDataPerPixel();
//         //        }
//         */
//        // </editor-fold>
//        if (overlay != null)
//        {
//            overlay.render(g);
//        }
//        title.render(g);
//    }

    public void renderHint(Graphics2D g) {
        if (getViewport().getBehaviourController().isWorking()) {
            return;
        }

        int preferredX = bestX;
        int bottomPosition = sp().getHeight() - 25;
        int preferredY = bottomPosition;
        int upPosition = 3;
        if (sp().getHeight() > 90) {
            switch (getPreferenceI(MedChartPreferences.HINT_POSITION)) {
                case MedChartPreferences.HINT_POSITION_BOTTOM:
                    preferredY = bottomPosition;
                    break;
                case MedChartPreferences.HINT_POSITION_MOUSE:
                    if (isUnderMouse()) {
                        preferredY = getMouseScreen().y + 15;
                        preferredX += 10;
                    }
                    break;
                default: // (value)
                    preferredY = bestY + 5;
                    preferredX = bestX + 5;
                    break;
            }
        }
        if (showHint) {
            g.setPaint(Color.red);
            final int r = 4;
            final int r2 = r * 2;
            g.drawArc(bestX - r, bestY - r, r2, r2, 0, 360);
            final int margin = 3;
            int xx = preferredX;

            if (xx < margin) {
                xx = margin;
            }
            if (xx > sp().getVisibleWidth() - 200 - margin) {
                xx = sp().getVisibleWidth() - 200 - margin;
            }

            hintRenderer.setX(xx);
            hintRenderer.setPadding(3);
            hintRenderer.setText(String.format("%s = %g", DateUtil.stringify(bestProbe), bestValue));
            if (preferredY <= margin) {
                preferredY = margin;
            } else if (preferredY > bottomPosition) {
                preferredY = bottomPosition;
            }
            hintRenderer.setY(preferredY);
            hintRenderer.render(g);

        }
    }

    @Override
    protected Float[] createArrayInstance(int size) {
        return new Float[size];
    }

    @Override
    public void calculateExtremum(DataRendererBase.Extremum extremum) {
        this.extremum=extremum;
        extremum.max = -Float.MAX_VALUE;
        extremum.min = Float.MAX_VALUE;
        serieRenderer.calculateExtremum(extremum);
        if (overlay != null) {
            overlay.calculateExtremum(extremum);
        }
    }

    public IDataReader getReader() {
        return reader;
    }

    public class CalculateAutoResult {

        public float scaleY;
        public float dataY;
    }
    private CalculateAutoResult car = new CalculateAutoResult();

    CalculateAutoResult calculateAutoParams() {
        final int margin = 10;
        Extremum e = extremum;
        float absModule = Math.max(Math.abs(e.max), Math.abs(e.min));
        car.scaleY = (e.max - e.min) / sp().getDataMaxModule() / 2 / (sp().getHeight() - 2 * margin) * sp().getHeight();
        if (car.scaleY == 0) {
            car.scaleY = 1;
        }
        car.dataY = (e.min + e.max) / 2f;
//        car.scaleY = (absModule + sp().toDataHeight(margin)) / sp().getDataMaxModule();// * (1 - 2 * margin / sp().getHeight());
        if (Float.isInfinite(car.scaleY)) {
            //TODO jak ktos bardzo powiekszy to moze sie zwalic bo bedzie infinity
            car.scaleY = 1;
        }
        return car;
    }

    @Override
    public void gatherData() {
//        super.gatherData(); //To change body of generated methods, choose Tools | Templates.
//        if (overlay != null) {
//
//            overlay.gatherData();
//        }
    }
}
