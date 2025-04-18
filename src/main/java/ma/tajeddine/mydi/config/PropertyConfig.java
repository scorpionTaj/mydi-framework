package ma.tajeddine.mydi.config;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyConfig {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "ref")
    private String ref;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
}