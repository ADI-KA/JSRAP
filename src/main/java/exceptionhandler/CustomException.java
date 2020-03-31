
package exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */
@Component
public class CustomException extends Exception {
    
    
    private String description;
    
    private String context = "/api/contexts/Error";    

    private HttpStatus code;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }    
    
        /**
     * @return the code
     */
    public HttpStatus getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(HttpStatus code) {
        this.code = code;
    }
      
}
