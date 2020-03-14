
package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.http.HttpMethod;

/**
 *
 * @author adika
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionDescriptor {
    HttpMethod httpMethod();
    String path() default "";
    String nameSpace();
    String body() default "";    
    String label() default "";
    String input() default "";
}

