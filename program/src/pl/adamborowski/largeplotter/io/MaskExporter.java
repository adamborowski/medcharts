/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.largeplotter.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import pl.adamborowski.medcharts.SelectionController;
import pl.adamborowski.medcharts.SelectionItem;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.utils.DateUtil;
import pl.adamborowski.utils.interfaces.IProgressListener;

/**
 *
 * @author test
 */
public class MaskExporter
{

    private IProgressListener listener;
    private final IDataReader data;
    private final SelectionController selectionController;
    private BufferedWriter dos;
    private long dataCompleted;
    private long dataTotal;
    private final DataSequence sequence;

    public void setListener(IProgressListener listener)
    {
        this.listener = listener;
    }

    public MaskExporter(IDataReader data, SelectionController selectionController)
    {
        this.data = data;
        this.selectionController = selectionController;
        this.sequence = data.getSequence();

    }

    public void export(File file) throws IOException
    {
        file.delete();
        dataTotal = data.getEnd() - data.getStart();
        dataCompleted = 0;
        try
        {
            dos = new BufferedWriter(new FileWriter(file));
            exportImpl();
            //i naprzemian: wpisywać zera i jedynki
        } catch (IOException ex)
        {
            throw ex;
        } finally
        {
            dos.close();
        }
    }
    private final int cycle = 50000;
    private int counter = cycle;
    //przedział obustronnie domknięty

    private void writeValues(long xStart, long xEnd, boolean writeOnes) throws IOException
    {
        if (xStart > xEnd)
        {
            return;
        }
        float xStartPerc = Math.round((float) (xStart - data.getStart()) / dataTotal * 1000) / 10f;
        float xEndPerc = Math.round((float) (xEnd - data.getStart()) / dataTotal * 1000) / 10f;
        System.out.println("write " + (writeOnes ? "ones" : "zeros") + " from " + xStart + " to " + xEnd + " ( " + xStartPerc + "% - " + xEndPerc + "% )");
        char val=writeOnes?'1':'0';
        for (long x = xStart; x <= xEnd; x =data.getSequence().nextTime(x))
        {
            DateUtil.stringifyFast_HH_MM__SS_ssss(x, dos);
            dos.append('\t');
            
            dos.append(val);
            dos.newLine();
            counter--;
//            dataCompleted += interval;
            if (counter == 0)
            {
                counter = cycle;
                if (listener != null)
                {
                    listener.progressChanged((float) (x-data.getStart()) / (float) dataTotal);
                }
            }
        }
    }

    private void exportImpl() throws IOException
    {
        System.out.println("\f");
        System.out.println("export from " + data.getStart() + " to " + data.getEnd());
        //nagłówek
//        dos.writeLong(data.getStart());
//        dos.writeInt(data.getInterval());
        //pomysł jest taki, aby iterować po posortowanej kolekcji zaznaczeń, które przechodzą przez dane.
        //założenie: obszary są rozłączne (nie nachodzą na siebie)
        ArrayList<SelectionItem> items = selectionController.getItemsInRange(data.getStart(), data.getEnd());
        if (items.size() == 1)
        {
            //jest tylko jeden obszar, więc może wychodzić poza dane z obu stron
            SelectionItem onlyOneItem = (SelectionItem) items.get(0).clone();
            onlyOneItem.fixBounds();
            onlyOneItem.setStart(Math.max(data.getStart(), onlyOneItem.getStart()));
            onlyOneItem.setEnd(Math.min(data.getEnd(), onlyOneItem.getEnd()));
            items.set(0, onlyOneItem);
        } else if (items.isEmpty())
        {
            writeValues(data.getStart(), data.getEnd(), false);
        } else
        {
//            Collections.sort(items); //getItemsInRange już sam sortuje
            //obetnij ewentualnie graniczne zaznaczenia, aby nie wychodziły poza dane
            SelectionItem firstSelection = (SelectionItem) items.get(0).clone();
            SelectionItem lastSelection = (SelectionItem) items.get(items.size() - 1).clone();
            firstSelection.fixBounds();
            lastSelection.fixBounds();
            firstSelection.setStart(Math.max(data.getStart(), firstSelection.getStart()));
            lastSelection.setEnd(Math.min(data.getEnd(), lastSelection.getEnd()));
            //nie chcemy obcinać istniejącym zaznaczeniom...
            items.set(0, (SelectionItem) firstSelection);
            items.set(items.size() - 1, lastSelection);
            //obcięto części obszarów wychodzącyc poza zakres.
        }
        Iterator<SelectionItem> it = items.iterator();
        long lastEnd = data.getStart();
        SelectionItem item = null;
        int c = 0;
        while (it.hasNext())
        {
            System.out.println("Selection area:" + c++);
            item = it.next();
            //najpierw napisz zera przed obszarem
            writeValues(data.getSequence().nextTime(lastEnd), data.getSequence().prevTime(item.getStart()), false);
            //potem napisz jedynki w obszarze
            writeValues(item.getStart(), item.getEnd(), true);
            lastEnd = item.getEnd();
        }
        System.out.println("Rest fill:");
        //poza ostatnim obszarem moze trzeba wpisać parę zer...
        writeValues(data.getSequence().nextTime(item.getEnd()), data.getEnd(), false);
    }
}
