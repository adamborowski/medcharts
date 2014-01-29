/**
 * renderer jednej serii pomiarowej (np pleth overlay 1) ten renderer ma
 * wybierać currentAggregationReader w zależności od zmiany skali w poziomie
 */
package pl.adamborowski.medcharts.data;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 *
 * @author adam
 */
public class SerieRenderer {

    private AggregationRenderer currentAggregationRenderer;
    private final SerieReader reader;
    private final Map<AggregationReader, AggregationRenderer> aggregationRenderers = new HashMap<>();
    private final Assembly.Serie jaxb;
    private SpaceManager sp;

    public SerieRenderer(SerieReader reader, Assembly.Serie jaxb, SpaceManager sp) {
        this.reader = reader;
        this.jaxb = jaxb;
        this.sp = sp;
    }

    public void addAggregationRenderer(AggregationRenderer aggregationRenderer) {
        this.aggregationRenderers.put(aggregationRenderer.reader, aggregationRenderer);

    }

    public void render(Graphics2D g) {
        float scaleX = sp.getScaleX();
        int goodRange = getBestRange(scaleX);
        System.out.println("scaleX: " + scaleX + ", goodRange:" + goodRange);
        final AggregationReader aggregationReader = reader.getAggregationReader(goodRange, goodRange == -1 ? AggregationDescription.Type.ACT : AggregationDescription.Type.MAX);
        System.out.println("aggregation: " + aggregationReader.ad.name);
        currentAggregationRenderer = aggregationRenderers.get(aggregationReader);//todo test
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
}
