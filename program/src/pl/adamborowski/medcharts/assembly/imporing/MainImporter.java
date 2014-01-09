package pl.adamborowski.medcharts.assembly.imporing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import sun.misc.FloatingDecimal;

/*
 * MainImporter importuje dane główne wykresu, żródła tekstowe zawierają daty i
 * wartości. zapisuje do pliku binarnego i kończy pracę.
 *
 * <b>Format pliku:</b>
 * [x][a][b][start] // sekwencja
 * [maxModule] //miejsce na maksymalny moduł
 * [value] * //kolejne wartości próbek
 *
 * @author test
 */
public final class MainImporter extends ImporterBase<MainReader>
{

    public static final int MAX_SEQUENCE_SIZE = 10;

    public MainImporter(AssemblyImporter importer, Assembly.Serie serie) throws IOException
    {
        super(importer, importer.getSourceFile(serie.getSource()));
        setReader(new MainReader(this));
    }
    ///////////////////////
    final private int NUM_ILLEGAL_LINES = 5;
    private DataSequence sequence;
    DateFormat df = new SimpleDateFormat("HH:mm:ss:SSSS");

    @Override
    protected void importImpl() throws IOException, ParseException
    {
        RandomAccessFile raf = createBinary(sourceFile, ""); //tworzymy nowy plik binarny
        long beginPos = raf.getFilePointer();
        String line;
        String[] splitted;

        for (int i = 0; i < NUM_ILLEGAL_LINES; i++)
        {
            sourceStream.readLine();
        }
        float maxModule = 0;
        long maxModuleFilePos;
        float currentValue;
        ArrayList<Float> values = checkSequence();
        //zapisz sekwencję do pliku
        raf.writeInt(sequence.getX());
        raf.writeInt(sequence.getA());
        raf.writeInt(sequence.getB());
        raf.writeLong(sequence.getStart());
        //zrób miejsce na maksymalny moduł
        maxModuleFilePos = raf.getFilePointer();
        raf.writeFloat(0f);
        //przewidywanie wielkości pliku
        raf.setLength((long) (raf.getFilePointer() + MainReader.FRAME_BYTES_Y * (numSourceLines - NUM_ILLEGAL_LINES)));
        System.out.println("length expected: " + raf.length());

        //wpisz wartości, które były testowane do pomiaru sekwencji
        for (Float testValue : values)
        {
            raf.writeFloat(testValue);
            maxModule = Math.max(maxModule, Math.abs(testValue));
        }
        //zapisz pozostałe próbki w wielkiej pętli
        long currentLine = values.size();
        while ((line = sourceStream.readLine()) != null)
        {
            try
            {
                currentValue = Float.parseFloat(line.substring(line.indexOf("\t") + 1));
                maxModule = Math.max(maxModule, Math.abs(currentValue));
            } catch (NumberFormatException ex)
            {
                currentValue = Float.NaN;
            }

            raf.writeFloat(currentValue);






            /*
             *       xxxxx
             *      xx    x
             *     xx     xx
             *    xx       x
             *   xx        xx
             *  xx          x    faza             
             *  x+----------------------> xx
             *               x           xx
             *               xx         xx
             *                x        xx
             *                 x      xx
             *                 xx   xxx
             *                  xxxxx
             *                  
             * tablice prymitywów:
             * a) długości kolejnych faz
             * b) maksima kolejnych faz
             * c) minima kolejnych faz
             * 
             * wykrywać zmianę znaku: lastValue*currentValue<0
             * wtedy dodać kolejny element w tablicach
             * oraz dodać długość aktualnej fazy do sumy
             * oraz zwiększyć licznik faz
             * 
             * na sam koniec policzyć średni czas fazy średnią ważoną powkładać jak na kartce do równych przedziałów
             * ustalić próg przejścia trybu wyświetlania na podstawie np średniej dł fazy
             */





            setLineProgress(++currentLine);
        }
        //zapisz maksymalny moduł wartości

        System.out.println("length final: " + raf.getFilePointer());
        raf.setLength(raf.getFilePointer());
        raf.seek(maxModuleFilePos);
        raf.writeFloat(maxModule);

        //spr:
        raf.seek(beginPos);
        System.out.println("x: " + raf.readInt());
        System.out.println("a: " + raf.readInt());
        System.out.println("b: " + raf.readInt());
        System.out.println("start: " + raf.readLong());
        System.out.println("maxModule: " + raf.readFloat());
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i + ": " + raf.readFloat());;
        }
        raf.seek(raf.length() - MainReader.FRAME_BYTES_Y);
        System.out.println("last i: " + raf.readFloat());;
        System.out.println("KONIEC");
    }

    /**
     * Odnajduje sekwencję w danych wejściowych. odczytuje próbki a jedocześnie
     * zlicza, ile jest takich samych: x++, a jak się zmieni, to nowy=b
     * modyfikuje pole sequence, aby ustawić na nowy obiekt
     *
     * @return sprawdzone próbki, które należy potem wprowadzić do pliku
     * binarnego razem z innymi, które potem będą odczytane
     */
    private ArrayList<Float> checkSequence() throws ParseException, IOException
    {
        readValue(sourceStream.readLine());
        long prevTime = readValue_date;
        long firstTime = prevTime;
        float prevValue = readValue_value;
        readValue(sourceStream.readLine());
        long currentTime = readValue_date;
        float currentValue = readValue_value;
        int x = 1;
        int a = (int) (currentTime - prevTime);
        int b;
        ArrayList<Float> values = new ArrayList<>(MAX_SEQUENCE_SIZE);
        values.add(prevValue);
        values.add(currentValue);
        //teraz mamy dwie pierwsze próbki, wartość a
        int i = 0;
        while (true)
        {
            if (i++ > MAX_SEQUENCE_SIZE)
            {
                x = 1;
                b = 0;
                break;
            }
            readValue(sourceStream.readLine());
            prevTime = currentTime;
            currentTime = readValue_date;
            values.add(readValue_value);
            if (currentTime - prevTime == a)
            {
                //tak, kolejny element "a"
                x++;//wydłużamy sekwencję o kolejny a
            } else
            {
                //nie, nowy element jest już w nowej sekwencji, gdyż przeskoczył o inny interwał
                b = (int) (currentTime - prevTime);
                break;
            }
        }

        sequence = new DataSequence(x, a, b, firstTime);

        return values;
    }
    private float readValue_value;
    private long readValue_date;
    private String[] splitted;

    private void readValue(String line) throws ParseException
    {
        splitted = line.split("\t");
        readValue_date = df.parse(splitted[0]).getTime();
        try
        {
            readValue_value = Float.parseFloat(splitted[1]);
        } catch (NumberFormatException ex)
        {
            readValue_value = Float.NaN;
        }
    }
}
