/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.adamborowski.medcharts.assembly.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

///////////////////////////////

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"aggregation"})
public class Aggregations {
    @XmlElement(required = true)
    protected List<Aggregations.Aggregation> aggregation;

    public void setAggregation(List<Aggregation> aggregations) {
        this.aggregation = aggregations;
    }

    public List<Aggregations.Aggregation> getAggregation() {
        if (aggregation == null) {
            aggregation = new ArrayList<>();
        }
        return this.aggregation;
    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Aggregation {

        @XmlAttribute(name = "type", required = true)
        protected String type;
        @XmlAttribute(name = "range")
        protected String range;
        @XmlAttribute(name = "fromScale")
        protected String fromScale;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public void setFromScale(String fromScale) {
            this.fromScale = fromScale;
        }

        public String getRange() {
            return range;
        }

        public String getFromScale() {
            if (fromScale == null) {
                fromScale = "auto";
            }
            return fromScale;
        }
    }
    
}
