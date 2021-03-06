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

/**
 *
 * @author adi
 */
@RestController
public class Schema {

    @RequestMapping(
            value = "/api/schema.json",
            method = GET)
    public Object get(HttpServletRequest request) throws CustomException {
        HashMap<String, HashMap> entityClassesMap = new HashMap<>();

        try {
            Class[] entityClasses = EntityReflectionUtil.getClassesFromPackage("entity");
            
            for (Class entityClass : entityClasses) {
            HashMap<String, ArrayList> entityClassMap = new HashMap<>();
                entityClassMap.put("supportedProperty", EntityReflectionUtil.getFieldListFromClass(entityClass));
                entityClassMap.put("supportedOperation", EntityReflectionUtil.getOperationListFromClass(entityClass));
                entityClassesMap.put(entityClass.getSimpleName(), entityClassMap);
            }
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);
        }

        return entityClassesMap;
    }
}
