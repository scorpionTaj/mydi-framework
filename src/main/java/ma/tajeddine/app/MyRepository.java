package ma.tajeddine.app;

import ma.tajeddine.mydi.annotation.Component;

@Component("myRepository")
public class MyRepository {
    public String fetchData() { return "Données"; }
}