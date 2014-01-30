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
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author adam
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"overlay", "selection", "aggregations"})
public class Serie {
    @XmlElement(required = true)
    protected List<Overlay> overlay;
    @XmlElement(required = false)
    protected Aggregations aggregations;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Selection selection;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "source")
    protected String source;
    @XmlAttribute(name = "color")
    protected String color;
    @XmlAttribute(name = "alpha")
    protected Float alpha;
    @XmlAttribute(name = "treshold")
    protected Integer treshold;

    public Aggregations getAggregations() {
        return aggregations;
    }

    public void setAggregations(Aggregations aggregations) {
        this.aggregations = aggregations;
    }

    public List<Overlay> getOverlay() {
        if (overlay == null) {
            overlay = new ArrayList<>();
        }
        return this.overlay;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection value) {
        this.selection = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getColor() {
        if (color == null) {
            return "#cc55ff";
        } else {
            return color;
        }
    }

    public void setColor(String value) {
        this.color = value;
    }

    public float getAlpha() {
        if (alpha == null) {
            return 1.0F;
        } else {
            return alpha;
        }
    }

    public void setAlpha(Float value) {
        this.alpha = value;
    }

    public int getTreshold() {
        if (treshold == null) {
            return 0;
        } else {
            return treshold;
        }
    }

    public void setTreshold(Integer value) {
        this.treshold = value;
    }
    
}
