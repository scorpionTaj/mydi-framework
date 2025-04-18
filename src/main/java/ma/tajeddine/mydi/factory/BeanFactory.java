package ma.tajeddine.mydi.factory;

import ma.tajeddine.mydi.core.BeanDefinition;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanFactory {
    private final Map<String, BeanDefinition> definitions;
    private final Map<String, Object> singletonCache = new ConcurrentHashMap<>();

    public BeanFactory(List<BeanDefinition> defs) {
        this.definitions = defs.stream()
                .collect(Collectors.toMap(BeanDefinition::getId, d -> d));
    }

    public Object getBean(String ref) {
        // 1. d’abord, si c’est un id
        if (definitions.containsKey(ref)) {
            return getBeanById(ref);
        }
        // 2. sinon, essaye de résoudre par type (className)
        for (BeanDefinition bd : definitions.values()) {
            if (bd.getClassName().equals(ref)) {
                return getBeanById(bd.getId());
            }
        }
        throw new RuntimeException("Bean introuvable : " + ref);
    }

    private Object getBeanById(String id) {
        BeanDefinition def = definitions.get(id);
        if ("singleton".equals(def.getScope())) {
            return singletonCache.computeIfAbsent(id, _k -> createBean(def));
        } else {
            return createBean(def);
        }
    }

    private Object createBean(BeanDefinition def) {
        try {
            Class<?> cls = Class.forName(def.getClassName());
            Object instance;

            // constructeur
            if (!def.getConstructorArgs().isEmpty()) {
                Object[] args = def.getConstructorArgs().stream()
                        .map(this::getBean)    // <- getBean fait désormais le fallback
                        .toArray();
                Constructor<?> ctor = findMatchingConstructor(cls, args.length);
                ctor.setAccessible(true);
                instance = ctor.newInstance(args);
            } else {
                Constructor<?> c0 = cls.getDeclaredConstructor();
                c0.setAccessible(true);
                instance = c0.newInstance();
            }

            // setters
            for (var prop : def.getProperties().entrySet()) {
                Object dep = getBean(prop.getValue());
                Method setter = findSetter(cls, prop.getKey(), dep.getClass());
                setter.setAccessible(true);
                setter.invoke(instance, dep);
            }
            // champs
            for (var inj : def.getFieldInjections().entrySet()) {
                Field f = cls.getDeclaredField(inj.getKey());
                f.setAccessible(true);
                f.set(instance, getBean(inj.getValue()));
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Constructor<?> findMatchingConstructor(Class<?> cls, int paramCount) {
        for (Constructor<?> ctor : cls.getDeclaredConstructors()) {
            if (ctor.getParameterCount() == paramCount) {
                ctor.setAccessible(true);
                return ctor;
            }
        }
        throw new RuntimeException("Aucun constructeur adapté trouvé dans " + cls);
    }

    private Method findSetter(Class<?> cls, String propName, Class<?> paramType) {
        String setter = "set" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
        try {
            return cls.getMethod(setter, paramType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Pas de setter pour " + propName + " dans " + cls, e);
        }
    }
}
