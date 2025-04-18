package ma.tajeddine.mydi.annotation;

import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    boolean required() default true;
}
