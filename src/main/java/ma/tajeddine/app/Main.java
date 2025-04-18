package ma.tajeddine.app;

import ma.tajeddine.mydi.core.BeanDefinition;
import ma.tajeddine.mydi.reader.XmlBeanDefinitionReader;
import ma.tajeddine.mydi.reader.AnnotationBeanDefinitionReader;
import ma.tajeddine.mydi.factory.BeanFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // --- MODE XML ---
        try (InputStream xml = Files.newInputStream(
                Paths.get("src/main/resources/beans.xml"))) {
            List<BeanDefinition> defs =
                    new XmlBeanDefinitionReader().loadBeanDefinitions(xml);
            BeanFactory factory = new BeanFactory(defs);
            MyService svc = (MyService) factory.getBean("myService");
            svc.doSomething();
        }

        // --- MODE ANNOTATIONS ---
        List<BeanDefinition> defs2 =
                new AnnotationBeanDefinitionReader()
                        .loadBeanDefinitions("ma.tajeddine.app");  // <— ici
        BeanFactory factory2 = new BeanFactory(defs2);
        MyService svc2 = (MyService) factory2.getBean("myService");
        svc2.doSomething();
    }
}