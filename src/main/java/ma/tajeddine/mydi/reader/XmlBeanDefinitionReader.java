package ma.tajeddine.mydi.reader;

import ma.tajeddine.mydi.config.*;
import ma.tajeddine.mydi.core.BeanDefinition;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class XmlBeanDefinitionReader {
    public List<BeanDefinition> loadBeanDefinitions(InputStream xml) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(BeansConfig.class);
        BeansConfig cfg = (BeansConfig) ctx.createUnmarshaller().unmarshal(xml);
        return cfg.getBeans().stream()
                .map(this::toBeanDefinition)
                .collect(Collectors.toList());
    }

    private BeanDefinition toBeanDefinition(BeanConfig bc) {
        BeanDefinition def = new BeanDefinition(bc.getId(), bc.getClassName(), bc.getScope());
        if (bc.getConstructorArgs() != null) {
            bc.getConstructorArgs().forEach(arg -> def.addConstructorArg(arg.getRef()));
        }
        if (bc.getProperties() != null) {
            bc.getProperties().forEach(prop -> def.addProperty(prop.getName(), prop.getRef()));
        }
        return def;
    }
}
