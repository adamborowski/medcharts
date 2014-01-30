package pl.adamborowski.medcharts.assembly.imporing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.reading.MainReader;
import pl.adamborowski.medcharts.data.AggregationDescription;
import pl.adamborowski.medcharts.data.SerieImporter;
import pl.adamborowski.medcharts.data.SerieReader;
import pl.adamborowski.utils.ParseUtil;
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
public final class MainImporter extends ImporterBase<MainReader> {

    @Override
    protected boolean isCacheValidImpl() {
        return false;// this is not used because serie importer is null while super().
    }

    public static final int MAX_SEQUENCE_SIZE = 10;
    public final SerieImporter serieImporter;
    private final Assembly.Serie serie;

    public MainImporter(AssemblyImporter importer, Assembly.Serie serie) throws IOException {
        super(importer, importer.getSourceFile(serie.getSource()));
        setReader(new MainReader(this));
        this.serie = serie;
        this.serieImporter = new SerieImporter(cacheFileManager);
        if (serie.getAggregations() != null) {
            for (Assembly.Serie.Aggregations.Aggregation aggregation : serie.getAggregations().getAggregation()) {
                List<AggregationDescription.Type> types = ParseUtil.parseTypes(aggregation.getType());
                for (AggregationDescription.Type type : types) {
                    serieImporter.addAggregation(new AggregationDescription(type.name() + aggregation.getRange(), type, ParseUtil.parseRange(aggregation.getRange())));
                }
            }
        }
        final AggregationDescription actAggregationImporter = new AggregationDescription("act", AggregationDescription.Type.ACT, 0);
        serieImporter.addAggregation(actAggregationImporter);
        isCacheValid = serieImporter.isCacheValid();
    }
    ///////////////////////
    final private int NUM_ILLEGAL_LINES = 5;
    private DataSequence sequence;
    DateFormat df = new SimpleDateFormat("HH:mm:ss:SSSS");

    @Override
    protected void importImpl() throws IOException, ParseException {
        String line;
        for (int i = 0; i < NUM_ILLEGAL_LINES; i++) {
            sourceStream.readLine();
        }
        float maxModule = 0;
        float sampleValue;
        ArrayList<Float> values = checkSequence();
        //przygotuj agregacje
        serieImporter.begin(sequence);
        //zapisz sekwencję do pliku
        long sampleTime = sequence.getStart();
        //wpisz wartości, które były testowane do pomiaru sekwencji
        for (Float testValue : values) {
            maxModule = Math.max(maxModule, Math.abs(testValue));
            serieImporter.process(sampleTime, testValue);
            sampleTime = sequence.nextTime(sampleTime);
        }
        //zapisz pozostałe próbki w wielkiej pętli
        long currentLine = values.size();
        while ((line = sourceStream.readLine()) != null) {
            try {
                sampleValue = Float.parseFloat(line.substring(line.indexOf("\t") + 1));
                maxModule = Math.max(maxModule, Math.abs(sampleValue));
            } catch (NumberFormatException ex) {
                sampleValue = Float.NaN;
            }
            serieImporter.process(sampleTime, sampleValue);
            sampleTime = sequence.nextTime(sampleTime);
            setLineProgress(++currentLine);
        }
        //zapisz maksymalny moduł wartości

        serieImporter.save();
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
    private ArrayList<Float> checkSequence() throws ParseException, IOException {
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
        while (true) {
            if (i++ > MAX_SEQUENCE_SIZE) {
                x = 1;
                b = 0;
                break;
            }
            readValue(sourceStream.readLine());
            prevTime = currentTime;
            currentTime = readValue_date;
            values.add(readValue_value);
            if (currentTime - prevTime == a) {
                //tak, kolejny element "a"
                x++;//wydłużamy sekwencję o kolejny a
            } else {
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

    private void readValue(String line) throws ParseException {
        splitted = line.split("\t");
        readValue_date = df.parse(splitted[0]).getTime();
        try {
            readValue_value = Float.parseFloat(splitted[1]);
        } catch (NumberFormatException ex) {
            readValue_value = Float.NaN;
        }
    }
}
