/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author test
 */
public class DateUtil
{

    public static String stringify(long x)
    {
        DateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
        return df.format(new Date(x));
    }
    final private static int timeLength = "HH:mm:ss:SSS".length();
    final private static GregorianCalendar gc = new GregorianCalendar();
    private static int h, m, s, ms;

    public static void stringifyFast_HH_MM__SS_ssss(long x, Writer writer) throws IOException
    {
        gc.setTimeInMillis(x);
        h = gc.get(Calendar.HOUR_OF_DAY);
        m = gc.get(Calendar.MINUTE);
        s = gc.get(Calendar.SECOND);
        ms = gc.get(Calendar.MILLISECOND);
        if (h < 10)
        {
            writer.append('0');
        }
        writer.append(String.valueOf(h));
        writer.append(':');
        if (m < 10)
        {
            writer.append('0');
        }
        writer.append(String.valueOf(m));
        writer.append(':');
        if (s < 10)
        {
            writer.append('0');
        }
        writer.append(String.valueOf(s));
        writer.append(':');
        if (ms < 100)
        {
            writer.append('0');
        }
        if (ms < 10)
        {
            writer.append('0');
        }
        writer.append(String.valueOf(ms));

    }
}
