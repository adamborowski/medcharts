/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import pl.adamborowski.utils.interfaces.IRange;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Poszukiwanie wszystkich odcinków, które przecinają zadany odcinek
 *
 * @author test
 * @param <TElement> typ elementu który posiada zakres start oraz end
 * @param <TDomain> typ klucza, który szukamy
 */
public class BinaryRangeSearchUtil<TElement extends IRange<TDomain>, TDomain extends Comparable>
{

    private TElement[] list;
    private TDomain start;
    private TDomain end;
    private LinkedList<TElement> afterFound = new LinkedList<>();
    private LinkedList<TElement> beforeFound = new LinkedList<>();
    private TElement randomFound;
    private int randomFoundIndex;

    synchronized public ArrayList<TElement> search(TElement[] list, TDomain at)
    {
        return search(list, at, at);
    }

    /**
     * Szuka dla danej listy i zadanego przedziału elementów, które zachaczają o
     * ten zakres.
     *
     * @param list posortowana lista elementów
     * @param start początek zakresu
     * @param end koniec zakresu
     * @return elementy, które przcinają dany zakres
     */
    synchronized public ArrayList<TElement> search(TElement[] list, TDomain start, TDomain end)
    {
        ArrayList<TElement> result = new ArrayList<>(beforeFound.size() + 1 + afterFound.size());
        //
        this.list = list;
        this.start = start;
        this.end = end;
        //
        randomFound = null;
        afterFound.clear();
        beforeFound.clear();
        /**
         * Pomysł jest taki:. 1. szukamy jakiegokolwiek elementu w przedziale 2.
         * szukamy lewych i prawych sąsiadów randomowego znalezionego elementu
         * (tych co dotykają przedziału) 3. łączymy listy oraz element w jeden
         * arraylist i zwracamy
         */
        randomFoundIndex = findAnyIndex();
        if (randomFoundIndex == -1)
        {
            return result;
        }
        randomFound = list[randomFoundIndex];
        findAfter();
        findBefore();

        result.addAll(beforeFound);
        result.add(randomFound);
        result.addAll(afterFound);
        return result;

    }

    /**
     * Binarnie szukany indeks który sie mieści w przedziale
     *
     * @return
     */
    private int findAnyIndex()
    {
        int left = 0;
        int right = list.length - 1;
        int guess;
        TElement guessObj;
        while (true)
        {
            guess = (left + right) / 2;
            guessObj = list[guess];
            if (isBefore(guessObj))
            {
                //podejrzany jest przed zakresem, przenieś lewy na podejrzany+1
                left = guess + 1;
            } else if (isAfter(guessObj))
            {
                //podejrzany jest za zakresem, przenieś prawy na podejrzany -1
                right = guess - 1;
            } else
            {
                //ten jest w zakresie
                return guess;
            }
            //jeśli right==left, to już na pewno nic nie znajdziemy
            if (left >= right)
            {
                return -1;
            }
        }
    }

    private boolean isBefore(TElement element)
    {
        //[nie przecina tylko wtedy, gdy koniec jest przed początkiem albo początek jest za końcem]...//stare
        return element.getEnd().compareTo(start) < 0;
    }

    private boolean isAfter(TElement element)
    {
        return element.getStart().compareTo(end) > 0;
    }

    private void findAfter()
    {
        TElement e;
        for (int i = randomFoundIndex + 1; i < list.length; i++)
        {
            e = list[i];
            if (isAfter(e))
            {
                // no ten już nam wyszedł za zakres
                return;
            }
            //dodajemy do co możemy do listy
            afterFound.add(e);
        }
    }

    private void findBefore()
    {
        TElement e;
        for (int i = randomFoundIndex - 1; i >= 0; i--)
        {
            e = list[i];
            if (isBefore(e))
            {
                // no ten już nam wyszedł za zakres
                return;
            }
            //dodajemy do co możemy do listy
            afterFound.add(e);
        }
    }
}
