package ma.tajeddine.app;

import ma.tajeddine.mydi.annotation.Component;
import ma.tajeddine.mydi.annotation.Autowired;

@Component("myService")
public class MyService {
    private final MyRepository repo;
    private ConsoleLogger logger;

    @Autowired  // injection constructeur
    public MyService(MyRepository repo) {
        this.repo = repo;
    }

    @Autowired  // injection via setter
    public void setLogger(ConsoleLogger logger) {
        this.logger = logger;
    }

    public void doSomething() {
        String d = repo.fetchData();
        logger.log("Traitement de : " + d);
    }
}
