package ma.tajeddine.mydi.config;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BeanConfig {
    @XmlAttribute(name = "id")
    private String id;

    // "class" est un mot-clef Java, donc on mappe sur className
    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "scope")
    private String scope = "singleton";

    @XmlElement(name = "constructor-arg")
    private List<ArgConfig> constructorArgs;

    @XmlElement(name = "property")
    private List<PropertyConfig> properties;

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public List<ArgConfig> getConstructorArgs() { return constructorArgs; }
    public void setConstructorArgs(List<ArgConfig> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }

    public List<PropertyConfig> getProperties() { return properties; }
    public void setProperties(List<PropertyConfig> properties) {
        this.properties = properties;
    }
}

