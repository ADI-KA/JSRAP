
package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author adika
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDescriptor { 
    ActionDescriptor[] actions();
}
