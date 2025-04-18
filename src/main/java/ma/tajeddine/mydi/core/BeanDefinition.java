package ma.tajeddine.mydi.core;

import java.util.*;

public class BeanDefinition {
    private final String id;
    private final String className;
    private final String scope;
    private final List<String> constructorArgs = new ArrayList<>();
    private final Map<String, String> properties = new LinkedHashMap<>();
    private final Map<String, String> fieldInjections = new LinkedHashMap<>();

    public BeanDefinition(String id, String className, String scope) {
        this.id = id;
        this.className = className;
        this.scope = scope;
    }

    public String getId() { return id; }
    public String getClassName() { return className; }
    public String getScope() { return scope; }
    public List<String> getConstructorArgs() { return constructorArgs; }
    public Map<String, String> getProperties() { return properties; }
    public Map<String, String> getFieldInjections() { return fieldInjections; }

    public void addConstructorArg(String ref) {
        constructorArgs.add(ref);
    }
    public void addProperty(String name, String ref) {
        properties.put(name, ref);
    }
    public void addFieldInjection(String name, String ref) {
        fieldInjections.put(name, ref);
    }
}
