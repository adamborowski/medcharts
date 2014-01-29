/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.data;

import java.io.Serializable;

/**
 *
 * @author adam
 */
public class AggregationDescription implements Serializable {

    public AggregationDescription(String name, Type type, int range) {
        this.name = name;
        this.type = type;
        this.range = range;
    }

    @Override
    public String toString() {
        return "AggregationDescription{" + "name=" + name + ", type=" + type + ", startTime=" + startTime + ", range=" + range + '}';
    }

    public enum Type {

        MIN, MAX, ACT, AVG, MED

    }
    /**
     * for example: 1m10s (from configuration xml file)
     */
    public String name;
    /**
     * min, max, avg,...
     */
    public Type type;
    /**
     * start time of data
     */
    public long startTime;
    /**
     * number of probes per aggregation
     */
    public int range;
}
