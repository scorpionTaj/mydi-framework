package ma.tajeddine.mydi.reader;

import ma.tajeddine.mydi.annotation.*;
import ma.tajeddine.mydi.core.BeanDefinition;
import org.reflections.Reflections;

import java.beans.Introspector;
import java.lang.reflect.*;
import java.util.*;

public class AnnotationBeanDefinitionReader {
    public List<BeanDefinition> loadBeanDefinitions(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class);
        List<BeanDefinition> defs = new ArrayList<>();

        for (Class<?> cls : types) {
            Component comp = cls.getAnnotation(Component.class);
            String id = comp.value().isEmpty()
                    ? Introspector.decapitalize(cls.getSimpleName())
                    : comp.value();

            BeanDefinition def = new BeanDefinition(id, cls.getName(), "singleton");
            detectInjectionPoints(cls, def);
            defs.add(def);
        }
        return defs;
    }

    private void detectInjectionPoints(Class<?> cls, BeanDefinition def) {
        // injection constructeur
        for (Constructor<?> ctor : cls.getDeclaredConstructors()) {
            if (ctor.isAnnotationPresent(Autowired.class)) {
                for (Class<?> param : ctor.getParameterTypes()) {
                    def.addConstructorArg(param.getName());
                }
            }
        }
        // injection champ
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                def.addFieldInjection(field.getName(), field.getType().getName());
            }
        }
        // injection setter
        for (Method m : cls.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Autowired.class)
                    && m.getName().startsWith("set")
                    && m.getParameterCount() == 1) {

                String prop = Introspector.decapitalize(m.getName().substring(3));
                def.addProperty(prop, m.getParameterTypes()[0].getName());
            }
        }
    }
}
