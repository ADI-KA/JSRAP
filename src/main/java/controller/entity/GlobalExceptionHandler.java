
package controller.entity;

import exceptionhandler.CustomException;
import exceptionhandler.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author adi.omanovic
 */

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler {
    
    @ExceptionHandler({Exception.class} )
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ExceptionResponse er = new ExceptionResponse();
        er.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        System.out.println(ex.toString());
        er.setDescription(ex.getMessage());        
        return new ResponseEntity<>(er,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @ExceptionHandler(CustomException.class )
    public ResponseEntity<ExceptionResponse> handleException(CustomException ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ExceptionResponse er = new ExceptionResponse();

        headers.setContentType(MediaType.APPLICATION_JSON);
        er.setDescription(ex.getDescription());
        er.setCode(ex.getCode().value());
        
        
        return new ResponseEntity<>(er, ex.getCode());
    }

    
}
