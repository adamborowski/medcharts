    //
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.22 at 07:31:25 PM CEST 
//
package pl.adamborowski.medcharts.assembly.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder
        = {
            "selection",
            "serie"
        })
@XmlRootElement(name = "assembly")
public class Assembly {

    @XmlElement(required = true)
    protected List<Assembly.Selection> selection;
    @XmlElement(required = true)
    protected List<Assembly.Serie> serie;

    public List<Assembly.Selection> getSelection() {
        if (selection == null) {
            selection = new ArrayList<Assembly.Selection>();
        }
        return this.selection;
    }

    public List<Assembly.Serie> getSerie() {
        if (serie == null) {
            serie = new ArrayList<>();
        }
        return this.serie;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Selection {

        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "title")
        protected String title;

        public String getId() {
            return id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String value) {
            this.title = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder
            = {
                "overlay",
                "selection",
                "aggregations"
            })
    public static class Serie {

        @XmlElement(required = true)
        protected List<Assembly.Serie.Overlay> overlay;
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

        public List<Assembly.Serie.Overlay> getOverlay() {
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

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder
                = {
                    "line",
                    "mask"
                })
        public static class Overlay {

            @XmlElement(required = true)
            protected List<Assembly.Serie.Overlay.Line> line;
            @XmlElement(required = true)
            protected List<Assembly.Serie.Overlay.Mask> mask;
            @XmlAttribute(name = "source")
            protected String source;

            public List<Assembly.Serie.Overlay.Line> getLine() {
                if (line == null) {
                    line = new ArrayList<Assembly.Serie.Overlay.Line>();
                }
                return this.line;
            }

            public List<Assembly.Serie.Overlay.Mask> getMask() {
                if (mask == null) {
                    mask = new ArrayList<Assembly.Serie.Overlay.Mask>();
                }
                return this.mask;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String value) {
                this.source = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
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

            @XmlAccessorType(XmlAccessType.FIELD)
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

        ///////////////////////////////
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"aggregation"})
        public static class Aggregations {

            @XmlElement(required = true)
            protected List<Assembly.Serie.Aggregations.Aggregation> aggregation;

            public void setAggregation(List<Aggregation> aggregations) {
                this.aggregation = aggregations;
            }

            public List<Assembly.Serie.Aggregations.Aggregation> getAggregation() {
                if (aggregation == null) {
                    aggregation = new ArrayList<>();
                }
                return this.aggregation;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
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
    }
}
