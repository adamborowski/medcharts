/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import pl.adamborowski.medcharts.renderers.RootRenderer;

/**
 * Ważna zmiana- od tej pory skala oznacza ilość milisekund na piksel
 * @author test
 */
final public class SpaceManager
{

    private  RootRenderer viewRenderer;
    private float yMultiplier;
    private DataSequence sequence;
    private int phaseXShift;
    private long firstTime;
    private long lastTime;
    private float millisecondsPerPixel;
//    private float intervalsPerPixel;
    private long dataX;
    private float dataY;
    private float scaleX;
    private float scaleY;
    private int width;
    private int height;
    private float localYOffset;
    private int zeroYPosition; //potrzebne
    private int resolution; //potrzebne
    private float dataMaxModule;
    private int zeroXPosition;
    public final DataVisibilityBounds visibleData = new DataVisibilityBounds();
    public final DisplayVisibilityBounds visibleDisplay = new DisplayVisibilityBounds();

    public SpaceManager()
    {
    }

    public void setViewRenderer(RootRenderer viewRenderer)
    {
        this.viewRenderer = viewRenderer;
    }

    final void configure(MainReader mr)
    {
        sequence = mr.getSequence();
        firstTime = mr.getStart();
        lastTime = mr.getEnd();
        phaseXShift = (int) (firstTime % sequence.getSequenceLength());
        dataMaxModule = mr.getMaxModule();
//        System.out.println(dataMaxModule);
    }

    final void update()
    {
        //
        dataX = viewRenderer.getDataX();
        dataY = viewRenderer.getDataY();
        scaleX = viewRenderer.getScaleX();
        scaleY = viewRenderer.getScaleY();
        width = viewRenderer.getWidth();
        height = viewRenderer.getHeight();
        if (height == 0 || width == 0)
        {
            return;
        }
//        System.out.println("dataMaxModule: "+dataMaxModule);
        //o ile trzeba przemnożyć dataY żeby pokazać na ekranie - przy dużym powiększeniu będzie to np 1000
        yMultiplier = height / 2.0f / dataMaxModule / scaleY;
//        System.out.println("yMultiplier: "+yMultiplier);
        localYOffset = (dataY) * yMultiplier + height / 2;
        zeroYPosition = height / 2;
        zeroXPosition = width / 2;
        //
        millisecondsPerPixel = scaleX;//z definicji skali x: ile  próbek na piksel

        //visibleDisplay od teraz zmienia się wyznaczenie dataVisibilitybounds! - dany piksel dokładnie wskazuje data 
        //a nie tak, że my zadajemy komponentowi wyświetlić coś w pół piksela
        //teraz jest tak że próbujemy zrobić tak jak każą (dataX) ale jak nie trafimy równo w całkowity piksel, to poprawiamy
        //datavisibility bounds, bo inaczej powstaje przekłamanie.

        visibleDisplay.first = 0;
        visibleDisplay.last = width - 1;
        visibleDisplay.firstAccessible = (int) Math.ceil(Math.max(visibleDisplay.first, toDisplayX(firstTime)));
        visibleDisplay.lastAccessible = (int) Math.min(visibleDisplay.last, toDisplayX(lastTime));
        visibleDisplay.length = visibleDisplay.last - visibleDisplay.first + 1;
        visibleDisplay.lengthAccessible = visibleDisplay.lastAccessible - visibleDisplay.firstAccessible + 1;
        //teraz już ustaliłeś już piksele, teraz wyznacz ich czasy!

        visibleData.first = toDataX(visibleDisplay.first);
        visibleData.last = toDataX(visibleDisplay.last);//rekurencja : żeby wyznaczyć korzystamy z czegoś co już tego potrzebuje
        visibleData.firstAccessible = Math.max(firstTime, toDataX(visibleDisplay.firstAccessible));//FIXME możliwe błedy z powodu wymuszenia accessible żeby nie był mniejszy niż początek pliku
        visibleData.lastAccessible = Math.min(lastTime, toDataX(visibleDisplay.lastAccessible)); //FIXME to samo
        visibleData.length = visibleData.last - visibleData.first + 1; // ta jedynka to tradycyjnie, bo wchodzi w skład last

        /*
         //teraz sprawdź, jak długi przedział będzie widoczny przy zadanej skali
         visibleData.length = (int) (width * scaleX);
         //zobacz, czy jest przesunięcie w fazie interwału (ms: 10 60 110 czy ms: 0 50 100 150)

         visibleData.first = dataX - visibleData.length / 2;

         visibleData.first = Math.round((double) visibleData.first / (double) dataInterval) * dataInterval + phaseXShift;
         visibleData.firstAccessible = Math.max(visibleData.first, firstTime);
         visibleData.last = dataX + visibleData.length / 2;
         //pierwszym widocznym nie może być wartość w środku fazy interwału (10, 60, 110, a nie 45, 24, 266)
         visibleData.last = Math.round((double) visibleData.last / (double) dataInterval) * dataInterval + phaseXShift;
         visibleData.lastAccessible = Math.min(visibleData.last, lastTime);
         //jaka minimalna rozdzielczość jest potrzebna?
         //kalkuluj długość przedziału na jeden pixel
         */

//        intervalsPerPixel = dataPerPixel / (float) sequence;
        //o ile trzeba przesunąć dziedzinę, aby zobaczyć xData na środku canvasu?
        resolution = (int) millisecondsPerPixel; // TODO ZASTĄPIĆ jakimś sequence

    }

    final public int getVisibleWidth()
    {
        return width;
    }

    final public int getHeight()
    {
        return height;
    }

    /**
     * .
     * lepiej zrobić to jako pomiar dataX + (displayX-center)*dataPerPixel
     *
     * @param displayX
     * @return
     */
    final public long toDataX(float displayX)
    {
        return (long) (dataX + (displayX - zeroXPosition) * millisecondsPerPixel);
        //return (long) (visibleData.first + (displayX) * dataPerPixel);
    }

    final public float toDisplayX(long dataXParam)
    {
        return (dataXParam - dataX) / millisecondsPerPixel + zeroXPosition;
//        return (float) (dataX - visibleData.first) / dataPerPixel;
    }

    final public float toDataY(float displayY)
    {
        return -(displayY - localYOffset) / yMultiplier;
    }

    final public float toDisplayY(float dataY)
    {
        return (-dataY) * yMultiplier + localYOffset;
    }

    final public float toDisplayHeight(float dataHeight)
    {
        return -dataHeight * yMultiplier;
    }

    final public long toDataWidth(float displayWidth)
    {
        return (long) (displayWidth * millisecondsPerPixel);
    }

    final public float toDataHeight(float displayHeight)
    {
        return (displayHeight) / yMultiplier;
    }

    final public float toDisplayWidth(long dataWidth)
    {
        return dataWidth / millisecondsPerPixel;
    }

    final public long fitToSequence(long time)
    {
        //TODO ZMIANA będzie
        return sequence.fitTime(time);
    }

    final public long toDataXAndFixToFitIntervalAndPhaseShift(float displayX)
    {
        return fitToSequence(toDataX(displayX));
    }

//    final public long ceilWidthToFitInterval(long xData)
//    {
//        //TODO PROBLEMATYCZNE gdy nie ma info, gdzie ten odcinek będzie (trzeba podać raczej x1 oraz x2 --> fitToSequence, i zwrócić różnicę
//        return (long) (Math.ceil(xData / sequence) * sequence);
//    }

    final public float getDataMaxModule()
    {
        return dataMaxModule;
    }

    final public int getZeroYPosition()
    {
        return zeroYPosition;
    }

    final public DataSequence getSequence()
    {
        return sequence;
    }

    final public int getPhaseXShift()
    {
        return phaseXShift;
    }

    final public long getFirstTime()
    {
        return firstTime;
    }

    final public long getLastTime()
    {
        return lastTime;
    }

    final public float getDataPerPixel()
    {
        return millisecondsPerPixel;
    }

//    final public float getIntervalsPerPixel()
//    {
//        return intervalsPerPixel;
//    }

    final public long getDataX()
    {
        return dataX;
    }

    final public float getDataY()
    {
        return dataY;
    }

    final public float getScaleX()
    {
        return scaleX;
    }

    final public float getScaleY()
    {
        return scaleY;
    }

    final public float getLocalYOffset()
    {
        return localYOffset;
    }

    final public int getResolution()
    {
        return resolution;
    }

    public class DataVisibilityBounds
    {

        public long first;
        public long last;
        public long firstAccessible;
        public long lastAccessible;
        public long length;
        public long lengthAccessible;
    }

    public class DisplayVisibilityBounds
    {

        public int first;
        public int last;
        public int firstAccessible;
        public int lastAccessible;
        public int length;
        public int lengthAccessible;
    }
}
