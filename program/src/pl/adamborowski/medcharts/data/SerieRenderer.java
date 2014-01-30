/**
 * renderer jednej serii pomiarowej (np pleth overlay 1) ten renderer ma
 * wybierać currentAggregationReader w zależności od zmiany skali w poziomie
 */
package pl.adamborowski.medcharts.data;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import pl.adamborowski.medcharts.renderers.DataRendererBase;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 *
 * @author adam
 */
public class SerieRenderer {

    private AggregationRenderer currentAggregationRenderer;
    private final SerieReader reader;
    private final Map<Integer, AggregationRenderer> aggregationRenderers = new HashMap<>();//key is aggregation range
    private SpaceManager sp;
    private int goodRange;

    public SerieRenderer(SerieReader reader,  SpaceManager sp) {
        this.reader = reader;
        this.sp = sp;
    }

    public void addAggregationRenderer(int range, AggregationRenderer aggregationRenderer) {
        this.aggregationRenderers.put(range, aggregationRenderer);

    }

    public void render(Graphics2D g) {
       //uwaga! calculateExtremum ma być wywołane tuż przed.
        currentAggregationRenderer.render(g);

    }

    int[] ranges;

    /**
     * get ranges and sort to enable automatic aggregation adjustment
     */
    public void initAutomation() {
        ranges = new int[reader.getNumAggregationReaders()];
        int i = 0;
        for (AggregationReader a : reader.getAggregationReaders()) {
            ranges[i] = a.getRange();
            System.out.println("aggregation: " + a.ad);
            i++;
        }
        Arrays.sort(ranges);

    }

    /**
     * iterate from largest range to smallest and get first lower than
     * spaceManager.scale
     *
     * @return
     */
    private int getBestRange(float timePerPixel) {

        for (int i = ranges.length - 1; i >= 0; i--) {
            int range = ranges[i];
            float realRange = range;
//            if (realRange == -1) {
//                realRange = (int) ((float) sp.getSequence().getSequenceLength() / sp.getSequence().getSequenceCount());
//                System.out.println(realRange);
//            }
            if (realRange <= timePerPixel) {
                return range;
            }
        }
//        
//        for (int range : ranges) {
//            float realRange = range;
//            if (realRange == -1) {
//                realRange = (int) ((float) sp.getSequence().getSequenceLength() / sp.getSequence().getSequenceCount());
//                System.out.println(realRange);
//            }
//            if (realRange >= timePerPixel) {
//                return range;
//            }
//        }

        throw new RuntimeException("get best range exception");

    }

    public void calculateExtremum(DataRendererBase.Extremum extremum) {
         float scaleX = sp.getScaleX();
        int lastGoodRange = goodRange;
        this.goodRange = getBestRange(scaleX);
        currentAggregationRenderer = aggregationRenderers.get(goodRange);//todo test

        if (goodRange != lastGoodRange) {
//            System.out.println("scaleX: " + scaleX + ", goodRange:" + goodRange);
        }
        currentAggregationRenderer.calculateExtremum(extremum);

    }
}
