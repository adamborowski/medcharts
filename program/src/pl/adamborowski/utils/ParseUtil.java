/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.adamborowski.medcharts.data.AggregationDescription;

/**
 *
 * @author adam
 */
public class ParseUtil {

    public static int parseRange(String range) {
        Pattern reg = Pattern.compile("(\\d+)(ms|s|m|h)+?");
        Matcher m = reg.matcher(range);
        final int second = 1000, minute = 60 * second, hour = 60 * minute;
        int num;
        String unit;
        int rangeI = 0;
        while (m.find()) {
            num = Integer.valueOf(m.group(1));
            unit = m.group(2).toLowerCase();
            switch (unit) {
                case "s":
                    num *= second;
                    break;
                case "ms":
                    num *= 1;
                    break;
                case "m":
                    num *= minute;
                    break;
                case "h":
                    num *= hour;
                    break;
            }
            rangeI += num;
        }
        return rangeI;
    }

    public static List<AggregationDescription.Type> parseTypes(String types) {
        Pattern reg = Pattern.compile("([A-z]+)");
        Matcher m = reg.matcher(types);
        List<AggregationDescription.Type> typesArray = new ArrayList<AggregationDescription.Type>(4);
        for (int i = 0; m.find(); i++) {
            typesArray.add( AggregationDescription.Type.valueOf(m.group(1).toUpperCase()));
        }
        return typesArray;
    }
}
