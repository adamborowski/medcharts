/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import pl.adamborowski.utils.FontUtil;
import pl.adamborowski.utils.DateUtil;
import pl.adamborowski.utils.MathUtil;
import pl.adamborowski.utils.PaintUtil;
import pl.adamborowski.utils.PlotUtil;
import pl.adamborowski.utils.geom.ShapeFactory;
import pl.adamborowski.utils.geom.ShapeFactory.ShapeProxy;

/**
 *
 * @author test
 */
public final class HelpRenderer extends RendererBase
{

    public static final int LABEL_SPACE = 140;
    public static final int text_v_padding = 4;
    public static final int VERTICAL_LABEL_HEIGHT = 22;
    final static int labelHeight = 26;
    final static int arrowWidth = 12;
    final static int arrowHeight = 5;
    final static int textPadding = 5;
    final static int radius = 6;
    public static final Color COLOR_AXIS = new Color(0xcccccc);
    public static final Color COLOR_GRID = new Color(0xaaaaaa);
    public static final Color COLOR_MINOR_GRID = new Color(0xcccccc);
    public static final Color VERTICAL_GRID_MAJOR = new Color(0xdfdfdf);
    public static final Color VERTICAL_GRID_MINOR = new Color(0xe0e0e0);
    public static final int LABEL_GAP = 16;
    private final static int Q_1ms = 1;
    private final static int Q_5ms = Q_1ms * 5;
    private final static int Q_25ms = Q_1ms * 25;
    private final static int Q_50ms = Q_1ms * 50;
    private final static int Q_250ms = Q_1ms * 250;
    private final static int Q_1s = Q_1ms * 1000;
    private final static int Q_5s = Q_1s * 5;
    private final static int Q_15s = Q_1s * 15;
    private final static int Q_1m = Q_1s * 60;
    private final static int Q_5m = Q_1m * 5;
    private final static int Q_15m = Q_1m * 15;
    private final static int Q_1h = Q_1m * 60;
    private final static int Q[] = new int[]
    {
        Q_1ms, Q_5ms, Q_25ms, Q_50ms, Q_250ms, Q_1s, Q_5s, Q_15s, Q_1m, Q_5m, Q_15m, Q_1h
    };
    private final static Font hourFont = FontUtil.getFont("DroidSansMono", Font.PLAIN, 16);
    private final static Font minuteFont = FontUtil.getFont("DroidSansMono", Font.PLAIN, 16);
    private final static Font millisecondFont = FontUtil.getFont("DroidSansMono", Font.ITALIC, 14);
    private final static Color hourColor = new Color(0x335522);
    private final static Color minuteColor = new Color(0x777733);
    private final static Color secondColor = new Color(0x77bb55);
    private final static Color millisecondColor = new Color(0xbb6644);
    private final static RectangleRenderer background = new RectangleRenderer();
    public static final Color VERTICAL_LABEL_MAJOR_TICK_COLOR = new Color(0x666666);
    public static final Color VERTICAL_LABEL_MINOR_TICK_COLOR = new Color(0xaaaaaa);
    public static final Color VERTICAL_LABEL_BACKGROUND = PaintUtil.createColor(0xffffff, 0.9f);
    public static final Color VERTICAL_LABEL_BORDER = new Color(0xdedede);
    public static final Color VERTICAL_LABEL_FOREGROUND = new Color(0x888888);
    public static final Color VERTICAL_AXE_BACKGROUND = PaintUtil.createColor(0xffffff, 0.8f);
    public static final Color VERTICAL_GRID_ZERO = COLOR_GRID;

    static
    {
        background.setBackgroundPaint(PaintUtil.createColor(0xffffff, 0.5f));
        background.setBorderPaint(PaintUtil.createColor(0x333333, 0.5f));
        background.setRadius(7);
        background.setHeight(36);

    }
    final int labelWidth = 118;
    private ShapeProxy arrow;
    private boolean flipLabel = false;
    private int majorScaleX;
    private int minorScaleX;
    private final Calendar tmpCalendar = new GregorianCalendar();
    private final DecimalFormat twoDecimalFormat = new DecimalFormat("00");
    private final DecimalFormat threeDecimalFormat = new DecimalFormat("000");
    int labelDynamicWidth;
    PlotUtil.MajorMinorTick tick;
    DecimalFormat df = new DecimalFormat("#.#");

    public HelpRenderer(RendererBase parent)
    {
        super(parent);
    }

    public void setFlipLabel(boolean flipLabel)
    {
        this.flipLabel = flipLabel;
    }

    @Override
    public void render(Graphics2D g)
    {

        ///old_rebderAcis(g);
        renderGrid(g);
    }

    public void renderUnderSerie(Graphics2D g)
    {
//        g.setPaint(COLOR_AXIS);
//        g.drawLine(0, sp().getZeroYPosition(), sp().getVisibleWidth(), sp().getZeroYPosition());
    }

    private void calculateFixedScaleX()
    {
        final long dataPerLabel = sp().toDataWidth(LABEL_SPACE);
        for (int i = 0; i < Q.length; i++)
        {
            if (dataPerLabel <= Q[i])
            {
                majorScaleX = Q[i];
                minorScaleX = i > 0 ? Q[i - 1] : -1;
                return;
            }
        }
        majorScaleX = (int) MathUtil.ceil(dataPerLabel, 1000 * 60 * 60);
        minorScaleX = Q_1h;
    }

    private void renderMajorGridLine(Graphics2D g, long dataX)
    {
        final int displayX = (int) sp().toDisplayX(dataX);


        //rysuj siatkę
        g.setPaint(COLOR_GRID);
        g.drawLine(displayX, 0, displayX, sp().getHeight());

    }

    public final void renderGrid(Graphics2D g)
    {
        calculateFixedScaleX();

        //GRID Y!!

        renderHorizontalGrid(g);


        long firstLabelX = MathUtil.floor(sp().visibleData.first, majorScaleX);
        if (minorScaleX > 0)
        {
            for (long x = firstLabelX; x <= sp().visibleData.last; x += minorScaleX)
            {
                renderMinorGridLine(g, x);
            }
        }

        for (long x = firstLabelX; x <= sp().visibleData.last; x += majorScaleX)
        {
            renderMajorGridLine(g, x);
        }


    }

    public final void renderLabels(Graphics2D g)
    {
        calculateFixedScaleX();

        labelDynamicWidth = labelWidth;
        if (majorScaleX >= Q_1m)
        {
            labelDynamicWidth -= 58;
        } else if (majorScaleX >= Q_1s)
        {
            labelDynamicWidth -= 30;
        }

        arrow = ShapeFactory.createCloudLabel(labelDynamicWidth, labelHeight, radius, arrowWidth, arrowHeight);
        if (flipLabel)
        {
            arrow.shape.transform(AffineTransform.getScaleInstance(1, -1));
            arrow.shape.transform(AffineTransform.getTranslateInstance(0, arrowHeight));
        }
        long firstLabelX = MathUtil.floor(sp().visibleData.first, majorScaleX);
        long lastLabelX = (long) MathUtil.ceil(sp().visibleData.last, majorScaleX);
        for (long x = firstLabelX; x <= lastLabelX; x += majorScaleX)
        {
            renderLabel(g, x);
        }



    }

    private void renderMinorGridLine(Graphics2D g, long x)
    {
        g.setPaint(COLOR_MINOR_GRID);
        int displayX = (int) sp().toDisplayX(x);
        g.drawLine(displayX, 0, displayX, sp().getHeight());
    }

    void renderLabel(Graphics2D g, long dataX)
    {
        final int displayX = (int) sp().toDisplayX(dataX);

        int labelWidth = labelDynamicWidth;


        //
        final int labelX = displayX - labelWidth / 2;
        final int labelY = flipLabel ? -2 : 2;

        arrow.setLocation(labelX, labelY + (flipLabel ? labelHeight : 0));

        //TODO wszystko renderować z granicznikami zależnymi od boxa, potem dopiero
        //od tego zależnić teksty

        ///arrow:

        g.setPaint(background.getBackgroundPaint());
        g.fill(arrow.shape);
        g.setPaint(background.getBorderPaint());
        g.draw(arrow.shape);
        ///
//        background.render(g);

        tmpCalendar.setTimeInMillis(dataX);
        final String hour = twoDecimalFormat.format(tmpCalendar.get(Calendar.HOUR_OF_DAY));
        final String minute = twoDecimalFormat.format(tmpCalendar.get(Calendar.MINUTE));

        //renderuj godziny - zawsze
        g.setPaint(hourColor);
        g.setFont(hourFont);
        g.drawString(hour + ":" + minute, labelX + textPadding, labelY + labelHeight - text_v_padding);
        if (majorScaleX < Q_1m)
        {
            // czas pokazać sekundy!
            g.setPaint(secondColor);
            g.setFont(minuteFont);
            final String second = twoDecimalFormat.format(tmpCalendar.get(Calendar.SECOND));
            g.drawString(":" + second, labelX + 53, labelY + labelHeight - text_v_padding);
            if (majorScaleX < Q_1s)
            {
                g.setPaint(millisecondColor);
                g.setFont(millisecondFont);
                final String millisecond = threeDecimalFormat.format(tmpCalendar.get(Calendar.MILLISECOND));
                g.drawString(millisecond, labelX + 88, labelY + labelHeight - text_v_padding - 1);
            }
        }
    }

    public void renderVerticalAxe(Graphics2D g)
    {
        tick = PlotUtil.getVerticalMajorMinorTick(sp().toDataHeight(sp().getHeight()), sp().toDataHeight(VERTICAL_LABEL_HEIGHT));
        float firstLabelY = (float) MathUtil.ceil(sp().toDataY(sp().getHeight()), (float) tick.major) - tick.major;//dajmy się narysować częsci labela dla argumentu który jeszcze nie jest widzialny
        float lastLabelY = (float) MathUtil.floor(sp().toDataY(0), (float) tick.major) + tick.major;//jw.




        final int tickX2 = sp().getVisibleWidth();
        final int tickX1 = tickX2 - 10;
        final int minorTickX2 = tickX2;
        final int minorTickX1 = minorTickX2 - 7;


        g.setPaint(VERTICAL_AXE_BACKGROUND);
        g.fillRect(tickX1 - 4, 0, 14, sp().getHeight());


        final float firstMinorTick = (float) MathUtil.ceil(sp().toDataY(sp().getHeight()), (float) tick.minor);
        final float lastMinorTick = (float) MathUtil.floor(sp().toDataY(0), (float) tick.minor);
        if (firstMinorTick + tick.minor != firstMinorTick)
        {
            for (double minorY = firstMinorTick; minorY <= lastMinorTick; minorY += tick.minor)
            {
                int minorYPixel = (int) sp().toDisplayY((float) minorY);
                g.setPaint(VERTICAL_LABEL_MINOR_TICK_COLOR);
                g.drawLine(minorTickX1, minorYPixel, minorTickX2, minorYPixel);
            }
        }

        if (firstLabelY + tick.major != firstLabelY)//błąd zaokrąglenia może wystąpić przy dużej skali
        {
            for (float y = firstLabelY; y <= lastLabelY; y += tick.major) // pętla schodzi od góry na wykresie, ale w danych od dołu 
            {
                int yPixel = (int) sp().toDisplayY(y);

                g.setPaint(VERTICAL_LABEL_MAJOR_TICK_COLOR);
                g.drawLine(tickX1, yPixel, tickX2, yPixel);
                plotVerticalLabel(g, y, yPixel);

            }
        }

    }

    private void plotVerticalLabel(Graphics2D g, float y, int yPixel)
    {

        if (tick.diffDigitPosition >= 0)
        {
            df.setMaximumFractionDigits(0);
        } else
        {
            df.setMaximumFractionDigits(-tick.diffDigitPosition);
            df.setMinimumFractionDigits(-tick.diffDigitPosition);
        }
        final String text = df.format(y);

        final Rectangle2D textBounds = FontUtil.getStringBounds(text, g);



        final int textWidth = (int) textBounds.getWidth();
        final int padding = 3;

        final int x2 = sp().getVisibleWidth() - 20;
        final int width = 2 * padding + textWidth;
        final int x1 = x2 - width;

        final int textHeight = (int) textBounds.getHeight();
        final int height = 2 * padding + textHeight;

        final int y1 = yPixel - height / 2;
        final int y2 = y1 + height;

        g.setPaint(VERTICAL_LABEL_BACKGROUND);
        g.fillRect(x1, y1, width, height);
//        g.setPaint(VERTICAL_LABEL_BORDER);
//        g.drawRect(x1, y1, width, height);

        g.setPaint(VERTICAL_LABEL_FOREGROUND);
        g.drawString(text, x1 + padding, y2 - padding - 2);
    }

    private void renderHorizontalGrid(Graphics2D g)
    {

        tick = PlotUtil.getVerticalMajorMinorTick(sp().toDataHeight(sp().getHeight()), sp().toDataHeight(VERTICAL_LABEL_HEIGHT));
        float firstLabelY = (float) MathUtil.ceil(sp().toDataY(sp().getHeight()), (float) tick.major) - tick.major;//dajmy się narysować częsci labela dla argumentu który jeszcze nie jest widzialny
        float lastLabelY = (float) MathUtil.floor(sp().toDataY(0), (float) tick.major) + tick.major;//jw.






//        final float firstMinorTick = (float) MathUtil.ceil(sp().toDataY(sp().getHeight()), (float) tick.minor);
//        final float lastMinorTick = (float) MathUtil.floor(sp().toDataY(0), (float) tick.minor);
//
//        g.setPaint(VERTICAL_GRID_MINOR);
//        for (double minorY = firstMinorTick; minorY <= lastMinorTick; minorY += tick.minor)
//        {
//            int minorYPixel = (int) sp().toDisplayY((float) minorY);
//            g.drawLine(0, minorYPixel, sp().getVisibleWidth(), minorYPixel);
//        }
        g.setPaint(VERTICAL_GRID_MAJOR);
        if (firstLabelY + tick.major != firstLabelY)//błąd zaokrąglenia może wystąpić przy dużej skali
        {
            for (float y = firstLabelY; y <= lastLabelY; y += tick.major) // pętla schodzi od góry na wykresie, ale w danych od dołu 
            {
                int yPixel = (int) sp().toDisplayY(y);
                g.drawLine(0, yPixel, sp().getVisibleWidth(), yPixel);
            }
        }


        if (firstLabelY < 0 && lastLabelY > 0)
        {
            //RYSUJEMY MC ZEROWE
            g.setPaint(VERTICAL_GRID_ZERO);
            int zeroPos = (int) sp().toDisplayY(0);

            g.drawLine(0, zeroPos, sp().getVisibleWidth(), zeroPos);
        }
    }
}