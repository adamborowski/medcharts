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
@XmlType(name = "", propOrder = {"line", "mask"})
public class Overlay {
    @XmlElement(required = true)
    protected List<Overlay.Line> line;
    @XmlElement(required = true)
    protected List<Overlay.Mask> mask;
    @XmlAttribute(name = "source")
    protected String source;

    public List<Overlay.Line> getLine() {
        if (line == null) {
            line = new ArrayList<Overlay.Line>();
        }
        return this.line;
    }

    public List<Overlay.Mask> getMask() {
        if (mask == null) {
            mask = new ArrayList<Overlay.Mask>();
        }
        return this.mask;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Line {

        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "color")
        protected String color;
        @XmlAttribute(name = "alpha")
        protected Float alpha;

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getColor() {
            if (color == null) {
                return "#666666";
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
    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Mask {

        @XmlAttribute(name = "name", required = true)
        protected String name;
        @XmlAttribute(name = "color")
        protected String color;
        @XmlAttribute(name = "alpha")
        protected Float alpha;
        @XmlAttribute(name = "importToSelection")
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Selection importToSelection = null;

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getColor() {
            if (color == null) {
                return "#666666";
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

        public Selection getImportToSelection() {
            return importToSelection;
        }

        public void setImportToSelection(Selection value) {
            this.importToSelection = value;
        }
    }
    
}
