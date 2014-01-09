/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

/**
 *
 * @author test
 */
public class BinarySearchClosestUtil
{

    public static int search(long[] array, int from, int to, long key)
    {
        int left = 0;
        int right = array.length - 1;
        int guess;
        long guessObj;
        long nextGuessObj;
        

        while (true)
        {
            guess = (left + right) / 2;
            guessObj = array[guess];
            nextGuessObj = guess == to ? Integer.MAX_VALUE : array[guess + 1];
            if (guessObj <= key && key < nextGuessObj)
            {
                //nasz klucz znajduje się między podejrzanym, a następnym
                //więc to jest już wynk;
                return guess;
            } else if (guessObj < key)
            {
                //podejrzany jest przed zakresem, przenieś lewy na podejrzany+1
                left = guess + 1;
            } else
            {
                //podejrzany jest za zakresem, przenieś prawy na podejrzany -1
                right = guess - 1;
            }
            //jeśli right==left, to już na pewno nic nie znajdziemy
            if (left > right)
            {
                return -1;
            }
        }
    }


    public static int search(int[] array, int from, int to, int key)
    {
         int left = 0;
        int right = array.length - 1;
        int guess;
        int guessObj;
        int nextGuessObj;
        

        while (true)
        {
            guess = (left + right) / 2;
            guessObj = array[guess];
            nextGuessObj = guess == to ? Integer.MAX_VALUE : array[guess + 1];
            if (guessObj <= key && key < nextGuessObj)
            {
                //nasz klucz znajduje się między podejrzanym, a następnym
                //więc to jest już wynk;
                return guess;
            } else if (guessObj < key)
            {
                //podejrzany jest przed zakresem, przenieś lewy na podejrzany+1
                left = guess + 1;
            } else
            {
                //podejrzany jest za zakresem, przenieś prawy na podejrzany -1
                right = guess - 1;
            }
            //jeśli right==left, to już na pewno nic nie znajdziemy
            if (left > right)
            {
                return -1;
            }
        }
    }
}
