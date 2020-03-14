package exceptionhandler;

import java.util.HashMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author adi
 */
@Component
public class ExceptionResponse {
    
    
    
    private String description;
    
    private int code;    
    
    private String context = "/api/contexts/Error";
    
    private String exception;
    
    private HashMap additionalInformation;

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
     * @return the exception
     */
    public String getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(String exception) {
        this.exception = exception;
    }

    /**
     * @return the additionalInformation
     */
    public HashMap getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * @param additionalInformation the additionalInformation to set
     */
    public void setAdditionalInformation(HashMap additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }
      
    
    
    
}
