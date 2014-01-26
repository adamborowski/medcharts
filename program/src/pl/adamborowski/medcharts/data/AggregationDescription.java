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

    public enum AggregationType {

        MIN, MAX, ACT, AVG, MED

    }
    /**
     * for example: 1m10s (from configuration xml file)
     */
    public String name;
    /**
     * min, max, avg,...
     */
    public AggregationType type;
    /**
     * start time of data
     */
    long startTime;
    /**
     * number of probes per aggregation
     */
    int range;
}