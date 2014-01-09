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
public abstract class DataRendererBase<TValue, TBinding, TItem> extends RendererBase<TBinding>
{

    protected final IDataReader reader;
    protected IDataCollection<TValue, TItem> data;
    private DataRange range = new DataRange(0, 0, 0);

    public DataRendererBase(IDataReader reader, RendererBase parent, TBinding binding)
    {
        super(parent, binding);
        this.reader = reader;
    }

    @Override
    public void render(Graphics2D g)
    {
    }

    protected class Pixel
    {

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
    protected class RenderData implements Iterable<Pixel>
    {

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
        public boolean willRender()
        {
            return data != null && data.getSize() > 0;
        }

        @SuppressWarnings("LeakingThisInConstructor")
        public RenderData(RenderData previous)
        {
            oldRender = previous;
            //inicjalizuj obecne dane
            dataX = sp().getDataX();
            scaleX = sp().getScaleX();

            if (previous != null && previous.canRecoverFrom(this))
            {
                long timeDiff = -(dataX - previous.dataX); // minus bo jset odwrotny kontekst (przesuwamy piksele po czasie a nie czas po pikselach)
                int pixelShift = (int) sp().toDisplayWidth(timeDiff);// o ile pixeli w prawo przesuwamy całość, żeby pasowała do zmiany
                firstPixel = oldRender.firstPixel + pixelShift;
                firstPixelTime = oldRender.firstPixelTime + timeDiff;
                lastPixel = oldRender.lastPixel + pixelShift;
                lastPixelTime = oldRender.lastPixelTime + timeDiff;
                numPixels = lastPixel - firstPixel + 1;
                //dzięki temu nasze piksele są przesunięte (ale nie zmienił się ich czas)   
            } else
            {
                firstPixelTime = sp().visibleData.firstAccessible;
                firstPixel = sp().visibleDisplay.firstAccessible;
                lastPixelTime = sp().visibleData.lastAccessible;
                lastPixel = sp().visibleDisplay.lastAccessible;
                //dzięki czemu pixele będą ustawione na sp().visilble.*
                numPixels = sp().visibleDisplay.lengthAccessible;
            }

            if (numPixels < 0)
            {
                numPixels = 0;
            }
            //pixels = createArrayInstance(numPixels);
            pixelTimeInterval = sp().toDataWidth(1);
        }

        void fillWithNewData(IDataCollection<TValue, TItem> data, int fromPixel, int toPixel)
        {
            this.data = data;
            //jeśli fromPixel i toPixel ==-1 to licz sam te wartości
            //od jakiego x zaczynamy
//            long lastDataX = Long.MAX_VALUE;
//            for (int i = fromPixel; i <= toPixel; i++)
//            {
//                long iDataX = sp().toDataX(i);//tutaj może dojść poza zakres, gdyż
//                long fixedIDataX = sp().fitToSequence(iDataX);
//                if (fixedIDataX == lastDataX && (i + 1 != toPixel))//ostatnie zapewnia dociągnięcie pędzlem do krawędzi ekranu
//                {
//                    //nulle są tam, gdzie jest przy dużym powiększeniu ta sama próka
//                    pixels[i - firstPixel] = null;
//                } else
//                {
//
//                    pixels[i - firstPixel] = data.getY(iDataX);
//                }
//                lastDataX = fixedIDataX;
////                if (iDataX > sp().visibleData.lastAccessible)
////                {
////                    iDataX = sp().visibleData.lastAccessible;
////                }
////                    System.out.println("i-firstPixel: "+(i-firstPixel));
//
//            }
        }

        @Deprecated
        void recoverPixels(int fromPixel, int toPixel, int toIndex)
        {
//            final int fromPixel = fromPixel - oldRender.firstPixel;
//            int length = toPixel - fromPixel + 1;
////            System.out.println("Chcę odzyskać piksele od "+fromPixel+" do "+toPixel+", wszystkich: "+numPixels);
////            System.out.printf("recover: old pixels length: %s, lastExclusive: %s\t new pixels length: %s, lastExclusive: %s", oldRender.pixels.length
////                    , fromPixel+length, pixels.length, toPixel+length);
//            if (fromPixel + length >= oldRender.pixels.length)
//            {
//                length = oldRender.pixels.length - fromPixel - 1;
//            }
//            System.arraycopy(oldRender.pixels, fromPixel, pixels, toPixel, length);
        }

        public boolean canRecoverFrom(RenderData oldRender)
        {
            return false;//jeszcze to jest kulejące!! 
            //FIXME nie działa caching
//            return scaleX == oldRender.scaleX;
        }

        protected class PixelIterator implements Iterator<Pixel>
        {

            Pixel pixel = new Pixel();

            public PixelIterator()
            {
                pixel.i = 0;
            }

            @Override
            public boolean hasNext()
            {
                return pixel.i + 1 < numPixels;
            }

            @Override
            public Pixel next()
            {

                pixel.dataX = sp().toDataX(pixel.displayX);
                pixel.displayX = pixel.i + firstPixel;
//                pixel.value = pixels[pixel.i];
                pixel.i++;
                return pixel;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public Iterator<Pixel> iterator()
        {
            return new PixelIterator();
        }
    }
    protected RenderData oldScope;
    protected RenderData currentScope;

    public class Extremum
    {

        public float max;
        public float min;
    }
    protected Extremum extremum = new Extremum();

    protected abstract Extremum calculateExtremum();//każda podklasa ma zaimplemenotwać sprawdzanie max wartości

    protected abstract TValue[] createArrayInstance(int size);
    private long oldFirstData = -1;
    private long oldLastData = -1;

    public void gatherData()
    {
        SpaceManager s = sp();
        DataRange newRange = new DataRange(s.fitToSequence(s.visibleData.firstAccessible), Math.min(s.fitToSequence(reader.getEnd()), s.getSequence().moveTime(s.visibleData.lastAccessible, 2)), (int) (float) Math.ceil(s.getScaleX()));//todo zmieniono +interval na nexttime
//        System.out.println("last pixel x: "+sp().toDisplayX(s.visibleData.lastAccessible));
//        System.out.println("last pixel x moved: "+sp().toDisplayX(s.getSequence().nextTime(s.visibleData.lastAccessible)));
        if (s.visibleData.first == oldFirstData && s.visibleData.last == oldLastData)
        {
            //te same dane
            return;
        }
        oldFirstData = s.visibleData.first;
        oldLastData = s.visibleData.last;


        range = newRange;
        int numPixels = s.visibleDisplay.lengthAccessible;
        int newFirstPixel = s.visibleDisplay.firstAccessible;
        int newLastPixel = s.visibleDisplay.lastAccessible;
//        System.out.printf("Tworze nowe piksle: numPixels: %s, newFirstPixel: %s, newLastPixel: %s, szerokość: %s\n", numPixels, newFirstPixel, newLastPixel, sp().getVisibleWidth());
        oldScope = currentScope;
        currentScope = new RenderData(oldScope);

        if (s.visibleData.firstAccessible < s.visibleData.lastAccessible)
        {
            if (oldScope == null || !currentScope.canRecoverFrom(oldScope))// czy skala się różni? wtedy na pewno trzeba przemalować
            {

                try
                {

                    //zrób test polegający na sprawdzeniu, czy visibilitybounds mapują się bez błedów
                    //fa=first accessible, la=last accessible
//                    long faDisplayToData = s.toDataX(s.visibleDisplay.firstAccessible);
//                    float faDataToDisplay = s.toDisplayX(s.visibleData.firstAccessible);
//                    System.out.printf("\nfaData: native: %s converted: %s\n", s.visibleData.firstAccessible, faDisplayToData);
//                    System.out.printf("faDisplay: native: %s converted: %s\n", s.visibleDisplay.firstAccessible, faDataToDisplay);
                    data = (IDataCollection<TValue, TItem>) reader.getData(s, s.visibleDisplay.firstAccessible, (int) s.toDisplayX(reader.getSequence().nextTime(s.visibleData.lastAccessible)));


                } catch (IOException ex)
                {
                    Logger.getLogger(SerieRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                //przechodź po pikselach i maluj do tablicy
                currentScope.fillWithNewData(data, newFirstPixel, newLastPixel);

                //TODO dziwnie renderuje
            } else
            {
                //dostosuj stare wyrenderowanie, aby było przesunięte

                //teraz sprawdź, czy zakres pikseli nowych pokrywa się z zakresem zapamiętanych

                int gapStartPixel;
                int gapEndPixel;
                int receiveStartPixel;
                int receiveEndPixel;
                int receiveIndex;//indeks na który wkładamy do tempa odzyski
                if (currentScope.firstPixel > oldScope.firstPixel)
                {
                    System.out.println("Przewijanie w lewo, luka z lewej, odzysk z prawej");
                    //piksele poszły w prawo: luka z lewej, odzysk z prawej
                    gapStartPixel = oldScope.firstPixel;
                    gapEndPixel = currentScope.firstPixel - 1;//ten co się przesunał
                    receiveStartPixel = currentScope.firstPixel;
                    //TODO tutaj trzeba obciąć, żeby nie cachowały się próbki wraz z przesuwaniem... można spróbować numPixels przemnożyć, wtedy będzie cachował np 10000 pix z lewej i prawej
                    receiveEndPixel = Math.min(numPixels - 1, currentScope.lastPixel);
                    receiveIndex = 0; //odzyskujemy od pierwszego rendera
                } else
                {
                    System.out.println("Przewijanie w prawo, luka z z prawej, odzysk z lewej");
                    //piksele poszły w lewo: odzysk z lewej, luka z prawej
                    receiveStartPixel = Math.max(0, currentScope.firstPixel);
                    receiveEndPixel = currentScope.lastPixel;
                    gapStartPixel = currentScope.lastPixel + 1;
                    gapEndPixel = oldScope.lastPixel;
                    receiveIndex = oldScope.firstPixel;//odzyskujemy od pewnego rendera (bo część poszła za lewą krawędź ekranu)
                }
                //odzyskaj dane:

                currentScope.recoverPixels(receiveStartPixel, receiveEndPixel, receiveIndex);


                //wypełnij lukę!

//                try
//                {
//                    IDataCollection<TValue, TItem> gapData = (IDataCollection<TValue, TItem>) reader.getData(range.newBounded(s.toDataX(gapStartPixel), s.toDataX(gapEndPixel)));
//                    currentScope.fillWithNewData(gapData, gapStartPixel, gapEndPixel);
//
//                } catch (IOException ex)
//                {
//                    Logger.getLogger(DataRendererBase.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        } else
        {
            //currentScope = null;
        }

    }
}
