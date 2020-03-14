package controller.entity;

import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Schema {

    @RequestMapping(
            value = "/api/schema.json",
            method = GET)
    public Object get(HttpServletRequest request) throws CustomException {
        
        HashMap<String, HashMap> classesMap = new HashMap<>();

        try {
            Class[] classes = EntityReflectionUtil.getClassesFromPackage("entity");
            
            for (Class classe : classes) {
            HashMap<String, ArrayList> classMap = new HashMap<>(); 
                classMap.put("supportedProperty", EntityReflectionUtil.getFieldListFromClass(classe));
                 classMap.put("supportedOperation", EntityReflectionUtil.getOperationListFromClass(classe));
                classesMap.put(classe.getSimpleName(), classMap);
            }
            
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        return classesMap;
    
    }
}
