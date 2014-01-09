/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.data;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author test
 */
final public class DataSequence
{

    final private int x;//ile razy powtarzający się interwał
    final private int a;//powtarzający się interwał
    final private int b;//wyrównujący interwał
    final private int s;//ax+b

    public int getX()
    {
        return x;
    }

    public int getA()
    {
        return a;
    }

    public int getB()
    {
        return b;
    }

    /**
     * długość sekwencji w milisekundach
     *
     * @return
     */
    public int getSequenceLength()
    {
        return s;
    }

    /**
     * ilość próbek w sekwencji
     *
     * @return
     */
    public int getSequenceCount()
    {
        return n;
    }

    public long getStart()
    {
        return start;
    }
    final private int t;//a(x+1)
    final private int ax;//ax
    final private int n; //x+1 -- ilość próbek w sekwencji
    final private long start;
    //-------- zmienne pomocnicze --------
    private int sequenceIndex;
    private int sequenceRest;
    private int probeBaseIndex;
    private int probeRestIndex;

    public DataSequence(int x, int a, int b)
    {
        this(x, a, b, 0);
    }

    public DataSequence(int x, int a, int b, long start)
    {
        this.x = x;
        this.a = a;
        this.b = b;
        this.start = start;
        ax = a * x;
        s = ax + b;
        n = b == 0 ? 1 : x + 1;
        t = a * n;
    }

    public long nextTime(long time)
    {
        return toTime(toProbeIndex(time) + 1);
    }

    public long prevTime(long time)
    {
        return toTime(toProbeIndex(time) - 1);
    }

    public long moveTime(long time, int numProbes)
    {
        return toTime(toProbeIndex(time) + numProbes);
    }
    
    public long fitTime(long time)
    {
        return toTime(toProbeIndex(time));
    }

    public long toTime(int probeIndex)
    {
        sequenceIndex = probeIndex / n; // w jakiej sekwencji jset próbka
        probeRestIndex = probeIndex % n; // która to jest próbka w sekcwencji
        return start + sequenceIndex * s + a * probeRestIndex; //tutaj nie ma znaczenia,
        /* czy interwał ostatni jest inny, bo odcinki od pierwszego do ostatniego
         * indeksu w sekwencji są równe:
         * |_____|_____|_____|________|
         *  <-a-> <-a-> <-a->
         */
    }

    public int toProbeIndex(long time)
    {
        time -= start;
        sequenceIndex = (int) (time / s); // o jakiej sekwencji mówimy
        sequenceRest = (int) (time % s);  // czas lokalny sekwencji
        probeBaseIndex = sequenceIndex * n;  // numer próbki dla początku sekwencji
        probeRestIndex = sequenceRest / a;    // indeks żądanej próbki, ale lokalny, oraz zakładając, że interwały są równe
        /*
         * dlatego jeśli ostatni interwał jest dłuższy, to zrównując interwały wyjdzie, że lokalny indeks próbki
         * jest większy niż maksymalny w sekwencji:
         * |____|____|____|__|
         *   ^     ^   ^   ^
         *   |     |   |   |
         *   0     1   2   3
         * 
         * POWYŻSZE OK
         * 
         * 
         * |____|____|____|____!____!_|
         *   ^    ^     ^   ^     ^  ^
         *   |    |     |   |     |  |
         *   0    1     2   3     4  5
         * 
         * POWYŻSZE ŹLE!
         * powinno być:
         * 
         * | 0  | 1  |  2 | 3  !  3 !3|
         * \___/\___/\___/\__________/
         * 
         * 
         * Dlatego trzeba zawinąć do n
         */
        if (probeRestIndex > x)
        {
            probeRestIndex = x;// indeks lokalny w sekwencji, po uwzględnieniu różności interwałów
        }
        return probeBaseIndex + probeRestIndex;
    }
    /**
     *. Zwraca ilość próbek w przedziale domkniętym [a, b]
     * @param start
     * @param end
     * @return 
     */
    public int numProbesInRange(long start, long end)
    {
       return toProbeIndex(end)-toProbeIndex(start)+1;// dodajemy jedynkę, bo prawy indeks jest włączny    
    }

    /**
     * klasa pozwalająca iterować po sekwencjach
     */
    public final class Iterator
    {

        /**
         * Zmienna mówiąca, w której części sekwencji się znajdujemy
         */
        private int sequenceCounter;
        private long time;

        public Iterator(long time)
        {
            throw new NotImplementedException();
        }

        public Iterator(int probeIndex)
        {
            throw new NotImplementedException();
        }

        public long nextTime()
        {
            throw new NotImplementedException();
        }
        
    }
}
