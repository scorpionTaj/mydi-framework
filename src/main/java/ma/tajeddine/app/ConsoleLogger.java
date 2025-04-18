package ma.tajeddine.app;

import ma.tajeddine.mydi.annotation.Component;

@Component("logger")
public class ConsoleLogger {
    public void log(String msg) { System.out.println("[LOG] " + msg); }
}