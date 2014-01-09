///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package pl.adamborowski.largeplotter;
//
//import pl.adamborowski.medcharts.assembly.data.DataRange;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
//import pl.adamborowski.largeplotter.datadriver.IDataDriver;
//
///**
// *
// * @author test
// */
//public class SelectionController {
//
//    private IDataDriver driver;
//
//    public IDataDriver getDataDriver() {
//        return driver;
//    }
//
//    public void setDataDriver(IDataDriver driver) {
//        this.driver = driver;
//    }
//    private Plotter.SpaceConverter converter;
//
//    public Plotter.SpaceConverter getConverter() {
//        return converter;
//    }
//
//    public void setConverter(Plotter.SpaceConverter converter) {
//        this.converter = converter;
//    }
//
//    public void deselectAll() {
//        for (SelectionItem item : items) {
//            item.setSelected(false);
//        }
//    }
//
//    public void removeItem(SelectionItem item) {
//        items.remove(item);
//    }
//
//    public void clear() {
//        items.clear();
//    }
//
//    void addItem(SelectionItem newItem) {
//        items.add(newItem);
//    }
//
//    public enum ItemState {
//
//        GHOST, NORMAL, HOVERED, DOWN
//    };
//    private SelectionItem ghost;
//
//    public SelectionItem getGhost() {
//        return ghost;
//    }
//
//    public SelectionItem createGhost(long start, long end) {
//        ghost = new SelectionItem(this);
//        ghost.start = start;
//        ghost.end = end;
//        return ghost;
//    }
//
//    public void addGhostToItems() {
//        items.add(ghost);
//        ghost.setState(ItemState.NORMAL);
//        ghost = null;
//    }
//
//    public ArrayList<SelectionItem> getItems() {
//        return items;
//    }
//    private ArrayList<SelectionItem> items = new ArrayList<>();
//
//    public SelectionItem getItemContainingData(long xData) {
//        //przeszukiwanie binarne kolekcji
//        for (SelectionItem i : items) {
//            if (i.getStart() <= xData && i.getEnd() >= xData) {
//                return i;
//            }
//        }
//        return null;
//    }
//
//    public DataRange getRangeOfFreeSpaceUnderPoint(long xPoint, SelectionItem exclude) {
//        //idź w pętli po posortowanych obszarach i pamiętaj prawą krawędź
//        //itemu jeśli jest przed punktem. jesli po, to zwróć zakres o ile się nie zachaczają, wtedy null
//        Collections.sort(items);
//        long lastEnd = Integer.MIN_VALUE;
//        for (SelectionItem i : items) {
//            if (i == exclude) {
//                continue;
//            }
//            if (i.contains(xPoint)) {
//                //nie ma co, obszar pokrywa xPoint
//                return null;
//            }
//            if (i.getEnd() <= xPoint) {
//                //ten obszar jest przed xPoint, dlatego lastEnd to jego prawa krawędź
//                lastEnd = i.getEnd() + driver.getInterval();
//            } else if (i.getEnd() >= xPoint) {
//                //ten obszar jest już za, zatem jego lewa to nasza prawa krawędź
//                return new DataRange(lastEnd, i.getStart() - driver.getInterval());
//            }
//        }
//        return new DataRange(lastEnd, Integer.MAX_VALUE);
//    }
//
//    public ArrayList<SelectionItem> getItemsInRange(long xStart, long xEnd) {
//        return getItemsInRange(xStart, xEnd, null);
//    }
//
//    public ArrayList<SelectionItem> getItemsInRange(long xStart, long xEnd, SelectionItem excludeThis) {
//        Collections.sort(items);
//        ArrayList<SelectionItem> toReturn = new ArrayList<>();
//        Iterator<SelectionItem> iter = items.iterator();
//        while (iter.hasNext()) {
//            SelectionItem i = iter.next();
//            if (i == excludeThis) {
//                continue;
//            }
//            //patrzymy czy jest jakiś który zawiera lewą krawędź
//            if (i.getStart() > xEnd) {
//                //jesli ten jest poza to reszta i tak nie będzie
//                return toReturn;
//            }
//            if (i.getEnd() >= xStart) {
//                toReturn.add(i);
//                break;
//                //właśnie dodaliśmy pierwszego
//
//            }
//        }
//        //teraz dodawajmy kolejne, chyba, że któryś już zawiera xEnd, to przerwij
//        while (iter.hasNext()) {
//            SelectionItem i = iter.next();
//            if (i == excludeThis) {
//                continue;
//            }
//            if (i.getStart() > xEnd) {
//                break;
//            }
//            toReturn.add(i);
//            //
//        }
//        return toReturn;
//    }
//}
