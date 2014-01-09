//package pl.adamborowski.medcharts.assembly.data;
//
//import java.io.IOException;
//import pl.adamborowski.medcharts.assembly.reading.IDataReader;
//
///**
// * .
// * klasa która pośredniczy między renderererm a readerem, żeby pamiętać elementy
// * TDATA z TCOLLECTION
// *
// * @author test
// */
//public class CacheProxy<TValue>
//{
//
//    private final IDataReader<IDataCollection<TValue>> reader;
//    private TValue[] oldValues;
//    private TValue[] newValues;
//    private IDataCollection<TValue> oldCollection;
//    private IDataCollection<TValue> collection;
//    private long oldStart;
//    private long oldEnd;
//    private float oldResolution;
//
//    public CacheProxy(IDataReader reader)
//    {
//        this.reader = reader;
//    }
//    //jakoś zwróć nową kolekcję zawierajacą stare lewo lub prawo złączone z innym datarange
//
//    @SuppressWarnings(
//            {
//        "null", "ConstantConditions"
//    })
//    public IDataCollection<TValue> getData(DataRange range) throws IOException
//    {
//        boolean regenerateAll = false;
//        if (oldCollection == null||oldResolution!=range.getResolution())
//        {
//            //nie ma w cachu nic.
//            regenerateAll = true;
//        } else
//        {
//            if (oldEnd <= range.getStart() || oldStart >= range.getEnd())
//            {
//                //zakresy w ogóle są rozłączone
//                regenerateAll = true;
//            } else
//            {
//                try
//                {
//                    //można coś uszczypnąć z ostatniej kolekcji
//                    //ustal start i koniec wspólny (iloczyn) dla nowego i starego przedziału
//                    //FIXME adjustX może przenosić poza obszar pliku (wystarczy że MAX wygra reader.getStart oraz oldCollection adjustuje go roundem do góry - poza obszar
//                    long rangeStart = oldCollection.adjustX(Math.max(range.getStart(), reader.getStart()));
//                    long rangeEnd = oldCollection.adjustX(Math.min(range.getEnd(), reader.getEnd()));
//                    long jointStart = oldCollection.adjustX(Math.max(oldStart, rangeStart));
////                    long disjointStart = oldCollection.adjustX(Math.min(oldStart, rangeStart));
//                    long jointEnd = oldCollection.adjustX(Math.min(oldEnd, rangeEnd));
////                    long disjointEnd = oldCollection.adjustX(Math.max(oldEnd, rangeEnd));
//                    IDataCollection<TValue> recoveredCollection = oldCollection.getSubCollection(new DataRange(jointStart, jointEnd, range.getResolution()));
////                    DataCollection test = (DataCollection) reader.getData(new DataRange(jointStart, jointEnd, range.getResolution()));
////                    DataCollection rec = (DataCollection) recoveredCollection;
////                    System.out.println("kolekcja recovered ma: " + rec.getSize() + ", nowa ma: " + test.getSize());
////                    System.out.println("kolekcja recovered zaczyna od: " + rec.getFirstX() + ", nowa od: " + test.getFirstX());
////                    System.out.println("kolekcja recovered kończy na: " + rec.getLastX() + ", nowa na: " + test.getLastX());
////                    System.out.println("kolekcja recovered iteruje po: " + rec.getInterval() + ", nowa po: " + test.getInterval());
////                    for (DataCollection.DataItem iTest : rec)
////                    {
////                        long recX = rec.getXAt(iTest.i);
////                        float recY = rec.getY(iTest.x);
////
////                        long testX = test.getXAt(iTest.i);
////                        float testY = test.getY(iTest.x);
////
////                        boolean isBreak = false;
////                        if (recX != testX)
////                        {
////                            System.out.println("niezgodność X");
////                            isBreak = true;
////                        }
////                        if ((recY != testY) && Float.isNaN(recY))
////                        {
////                            System.out.println("niezgodność Y: recovered ma: " + recY + ", nowa ma: " + testY);
////
////                            isBreak = true;
////                        }
////                        if (isBreak)
////                        {
////                            System.out.println("testX: " + testX + " recX: " + recX);
////                            break;
////                        }
////                    }
//
//                    //co mamy do dorobienia z lewej strony?
//                    IDataCollection<TValue> leftCollection = null;
//                    IDataCollection<TValue> rightCollection = null;
//                    int lSize = 0;
//                    int rSize = 0;
//                    if (rangeStart < oldStart)// czy z tej strony jest LUKA
//                    {
//                        //mamy coś do dorobienia z lewej
//                        leftCollection = reader.getData(new DataRange(rangeStart, oldStart - collection.getInterval(), range.getResolution()));
//                        lSize = leftCollection.getSize();
//                    }
//                    if (rangeEnd > oldEnd)
//                    {
//                        rightCollection = reader.getData(new DataRange(oldEnd + collection.getInterval(), rangeEnd, range.getResolution()));
//                        rSize = rightCollection.getSize();
//                    }
//                    TValue[] newY = collection.create(lSize + recoveredCollection.getSize() + rSize);
////                    System.out.printf("disjointStart: %s, jointStart: %s, jointEnd: %s, disjointEnd: %s\n", disjointStart, jointStart, jointEnd, disjointEnd);
////                    System.out.println("newY: " + newY.length);
//                    if (lSize != 0)
//                    {
//                        System.arraycopy(leftCollection.getYData(), 0, newY, 0, lSize);
//                    }
//                    System.arraycopy(recoveredCollection.getYData(), 0, newY, lSize, recoveredCollection.getSize());
//                    if (rSize != 0)
//                    {
//                        System.arraycopy(rightCollection.getYData(), 0, newY, lSize + recoveredCollection.getSize(), rSize);
//                    }
//                    // teraz już skopiowano!!
//
//                    collection = collection.create(rangeStart, collection.getInterval(), newY, new DataRange(rangeStart, rangeEnd, range.getResolution()));
//                    //zawęź na chwile przedział do tego co jest w oldCollection
//                    //spróbuj na oldCollection createSubCollection(start, end, skala taka lub wieksza
//                    //potem pobierz od readera brakujące kolekcje z prawej lub z lewej
//                    //złóż wszsytko w jedną tablicę i oldCollection=new DataCollection(.....)
//                    //zwróc tę kolekcję.
//                } catch (SubCollectionException e)
//                {
////                    System.out.println("SUBCOLLECTION EXCEPTION@@@@@@@");
//                    regenerateAll = true;
//                }
//            }
//        }
//
//        if (regenerateAll)
//        {
//            collection = reader.getData(range);
//        }
//        oldCollection = collection;
//        oldStart = oldCollection.adjustX(oldCollection.getFirstX());
//        oldEnd = oldCollection.adjustX(oldCollection.getLastX());
//        oldResolution = range.getResolution();
//        return collection;
//    }
//}
