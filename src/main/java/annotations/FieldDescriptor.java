package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author adika
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDescriptor {    
    String label()  default "";
    boolean readable() default true;
    boolean requierd()  default false;
    boolean writable()  default true;
    boolean ignore() default false;
}
