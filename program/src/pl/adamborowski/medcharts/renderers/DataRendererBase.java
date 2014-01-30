/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.adamborowski.medcharts.assembly.data.DataRange;
import pl.adamborowski.medcharts.assembly.data.IDataCollection;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.assembly.reading.MainReader;

/**
 * klasa datarendererbase to rendererbase z proxy
 *
 * @author test
 */
public abstract class DataRendererBase<TValue, TBinding, TItem> extends RendererBase<TBinding> {

    protected final IDataReader reader;
    protected IDataCollection<TValue, TItem> data;
    private DataRange range = new DataRange(0, 0, 0);

    public DataRendererBase(IDataReader reader, RendererBase parent, TBinding binding) {
        super(parent, binding);
        this.reader = reader;
    }

    @Override
    public void render(Graphics2D g) {
    }

    protected class Pixel {

        int displayX;
        long dataX;
        TValue value;
        int i;
    }

    /**
     * Klasa która przechowuje render, aktualizując ich kontekst na ekranie.
     * Najpierw próbuje osadzić stare rendery na ekranie i patrzy, czy można z
     * nich skorzystać i jeśli tak to próbuje luki wypełnić od readera
     *
     * TODO OPTYMALIZACJĘ - buforowanie zrobić na dataCollection - metoda JOIN
     */
    protected class RenderData implements Iterable<Pixel> {

        IDataCollection<TValue, TItem> data;
        final long lastPixelTime;
        final long firstPixelTime;
        final int firstPixel; // od którego piksela zaczęto malować (czasami jest pusto po lewej)
        final int lastPixel; //na którym pikselu skończono malować (czasami jest pusto po prawej)
        int numPixels;
        final float pixelTimeInterval; //ile jednostek danych przypada na jeden piksel
        final float scaleX;
        final long dataX; //jaki był dataX w momencie renderowania
        final RenderData oldRender;

        /**
         * Pytanie, czy ten render wymaga kreślenia na navas
         *
         * @return
         */
        public boolean willRender() {
            return data != null && data.getSize() > 0;
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public RenderData(RenderData previous) {
            oldRender = previous;
            //inicjalizuj obecne dane
            dataX = sp().getDataX();
            scaleX = sp().getScaleX();

            if (previous != null && previous.canRecoverFrom(this)) {
                long timeDiff = -(dataX - previous.dataX); // minus bo jset odwrotny kontekst (przesuwamy piksele po czasie a nie czas po pikselach)
                int pixelShift = (int) sp().toDisplayWidth(timeDiff);// o ile pixeli w prawo przesuwamy całość, żeby pasowała do zmiany
                firstPixel = oldRender.firstPixel + pixelShift;
                firstPixelTime = oldRender.firstPixelTime + timeDiff;
                lastPixel = oldRender.lastPixel + pixelShift;
                lastPixelTime = oldRender.lastPixelTime + timeDiff;
                numPixels = lastPixel - firstPixel + 1;
                //dzięki temu nasze piksele są przesunięte (ale nie zmienił się ich czas)   
            } else {
                firstPixelTime = sp().visibleData.firstAccessible;
                firstPixel = sp().visibleDisplay.firstAccessible;
                lastPixelTime = sp().visibleData.lastAccessible;
                lastPixel = sp().visibleDisplay.lastAccessible;
                //dzięki czemu pixele będą ustawione na sp().visilble.*
                numPixels = sp().visibleDisplay.lengthAccessible;
            }

            if (numPixels < 0) {
                numPixels = 0;
            }
            //pixels = createArrayInstance(numPixels);
            pixelTimeInterval = sp().toDataWidth(1);
        }

       
        public boolean canRecoverFrom(RenderData oldRender) {
            return false;//jeszcze to jest kulejące!! 
            //FIXME nie działa caching
//            return scaleX == oldRender.scaleX;
        }

        protected class PixelIterator implements Iterator<Pixel> {

            Pixel pixel = new Pixel();

            public PixelIterator() {
                pixel.i = 0;
            }

            @Override
            public boolean hasNext() {
                return pixel.i + 1 < numPixels;
            }

            @Override
            public Pixel next() {

                pixel.dataX = sp().toDataX(pixel.displayX);
                pixel.displayX = pixel.i + firstPixel;
//                pixel.value = pixels[pixel.i];
                pixel.i++;
                return pixel;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public Iterator<Pixel> iterator() {
            return new PixelIterator();
        }
    }

    public static class Extremum {

        public float max = Float.MAX_VALUE;
        public float min = -Float.MAX_VALUE;
    }
    protected Extremum extremum = new Extremum();

    public abstract void calculateExtremum(DataRendererBase.Extremum extremum);

    protected abstract TValue[] createArrayInstance(int size);

    public void gatherData() {

    }
}
