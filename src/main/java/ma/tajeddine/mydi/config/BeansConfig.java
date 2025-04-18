package ma.tajeddine.mydi.config;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class BeansConfig {
    @XmlElement(name = "bean")
    private List<BeanConfig> beans;

    // getters / setters (optionnels, JAXB n’en tiendra pas compte)
    public List<BeanConfig> getBeans() {
        return beans;
    }
    public void setBeans(List<BeanConfig> beans) {
        this.beans = beans;
    }
}
