/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.reading.OverlayReader;

/**
 *
 * @author test
 */
public class OverlayImporter extends ImporterBase<OverlayReader>
{

    private DataSequence sequence;

    /**
     * OverlayImpoter wymaga dwóch plików: *.bin oraz *.mask.bin
     *
     * @return
     */
    @Override
    public boolean isCacheValidImpl()
    {
        return super.isCacheValidImpl() && hasValidBinary(sourceFile, ".mask");
    }

    @Override
    public long getComplexityRatio()
    {
        return (long) (super.getComplexityRatio() * 3);
    }
    private final MainImporter mainImporter;
    private final Assembly.Serie.Overlay overlay;
    private long firstTime;
    private int numMasks;
    private int numLines;
    private int currentLine = 0; // licznik lini. counter*interval+firstTime zwraca nam czas próbki

    public OverlayImporter(AssemblyImporter importer, Assembly.Serie.Overlay overlay, MainImporter mainImporter) throws IOException
    {
        super(importer, importer.getSourceFile(overlay.getSource()));
        setReader(new OverlayReader(mainImporter, this, overlay));
        this.overlay = overlay;
        this.mainImporter = mainImporter;
    }

    @Override
    protected void importImpl() throws IOException, ParseException
    {
        firstTime = mainImporter.getReader().getStart();
        sequence = mainImporter.getReader().getSequence();
        RandomAccessFile raf = createBinary(sourceFile, "");
        RandomAccessFile maskOutput = createBinary(sourceFile, ".mask");
        String line;
        numLines = overlay.getLine().size();
        numMasks = overlay.getMask().size();
        int i = 0;
        masks = new Mask[numMasks]; //tworzy maski dla wszystkich kolumn ale nie są używane gdy są to typy line
        for (i = 0; i < numMasks; i++)
        {
            masks[i] = new Mask(overlay.getMask().get(i));
        }
        int leftBound = -1;
        int rightBound;
        raf.writeInt(numLines); // header w pliku żeby wiedzieć ile jest kolumn
        while ((line = sourceStream.readLine()) != null)
        {

            //najpierw linie
            for (i = 0; i < numLines; i++)
            {
                rightBound = line.indexOf(',', leftBound + 1);
                raf.writeFloat(Float.parseFloat(line.substring(leftBound + 1, (rightBound == -1 ? line.length() : rightBound))));
                leftBound = rightBound;
            }
            for (i = 0; i < numMasks; i++)
            {
                rightBound = line.indexOf(',', leftBound + 1);
                masks[i].reportValue(line.substring(leftBound + 1, (rightBound == -1 ? line.length() : rightBound)));
                leftBound = rightBound;
            }
            setLineProgress(++currentLine);
        }
        raf.close();
        //teraz już zgromadziliśmy zaznaczenia, więc zapiszmy:
        maskOutput.writeInt(numMasks);
        for (Mask mask : masks)
        {
            if (mask != null)
            {
                //zapisz, ile obszarów ma maska
                maskOutput.writeInt(mask.items.size());
                for (MaskItem item : mask.items)
                {
                    //zapisz krańce zaznaczenia
                    maskOutput.writeLong(item.start);
                    maskOutput.writeLong(item.end);
                }
                if (mask.selectionItems != null)//jeśli importToSelection jest zaznaczone
                {
                    SelectionController.serialize(mask.selectionItems, assemblyImporter, mask.binding.getImportToSelection());
                }
            }

        }
        maskOutput.close();

    }
    private Mask[] masks;

    private class Mask
    {

        private ArrayList<SelectionItem> selectionItems; //jeśli importToSelection jest zaznaczone
        public final Assembly.Serie.Overlay.Mask binding;

        public Mask(Assembly.Serie.Overlay.Mask binding)
        {
            this.binding = binding;
            if (binding != null)
            {
                selectionItems = new ArrayList<>(1000); //będziemy dodawać do
            }
        }
        private MaskItem currentMask = null;
        private SelectionItem currentSelection = null;
        private ArrayList<MaskItem> items = new ArrayList<>();

        void reportValue(String str)
        {
            long currentData = sequence.toTime(currentLine);
            if (str.equals("1"))
            {
                if (currentMask == null)
                {
                    //chcemy rozpocząć nowe zaznaczanie
                    currentMask = new MaskItem();
                    currentMask.start = currentData;
                    currentMask.end = currentData;
                    items.add(currentMask);
                    if (selectionItems != null)//mamy tryb importToSelection
                    {
                        currentSelection = new SelectionItem(currentData, currentData);
                        selectionItems.add(currentSelection);
                    }
                } else
                {
                    //poszerzamy tylko zakres
                    currentMask.end = currentData;
                    if (selectionItems != null)//mamy tryb importToSelection
                    {
                        currentSelection.setEnd(currentData);
                    }
                }
            } else
            {
                if (currentMask == null)
                {
                    //poszerzamy przerwę, czyli nic nie robimy
                } else
                {
                    //kończymy zaznaczenie, obecne zostaje z takim zakresem jaki miał
                    currentMask = null;
                    currentSelection = null;
                }
            }
        }
    }

    private static class MaskItem
    {

        long start;
        long end;
    }
}
