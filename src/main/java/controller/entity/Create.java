
package controller.entity;

import entity.ProductAsset;
import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import hibernate.HibernateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */
@RestController
public class Create {    
    
    @RequestMapping(
            value = "/api/{entity}",
            method = POST)
    @ResponseStatus(HttpStatus.CREATED)

    public Object single(@PathVariable("entity") String entityName, @RequestBody Object jsonEntity) throws CustomException, ClassNotFoundException {
        
        Class entityClass = EntityReflectionUtil.createClassWithEntityName("entity." + entityName);
        
        EntityReflectionUtil.stopIfOperationIsNotPermitted("POST", entityClass);

        Object entityObject = null;
        
        try {    
            entityObject = entityClass.getDeclaredConstructor().newInstance();

            entityObject = EntityReflectionUtil.convertJsonObjectToEntityObject(entityClass, jsonEntity, entityObject, true);
            

            HibernateUtil.persistEntityObject(entityObject);

        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            CustomException ce = new CustomException();
            ce.setDescription(ex.getMessage());
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);
            throw ce;
        }

        return entityObject;
    }
}
