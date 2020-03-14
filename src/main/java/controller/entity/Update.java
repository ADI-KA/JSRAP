
package controller.entity;

import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import hibernate.HibernateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */
@RestController
public class Update {
    
    @RequestMapping(
            value = "/api/{entity}/{id}",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)

    public Object single(@PathVariable("entity") String entityName, @PathVariable("id") String entityId, @RequestBody Object jsonEntity) throws CustomException, ClassNotFoundException {

        
        Class entityClass = EntityReflectionUtil.createClassWithEntityName("entity." + entityName);
        EntityReflectionUtil.stopIfOperationIsNotPermitted("PUT", entityClass);
        Object entityObject;        
        entityObject = HibernateUtil.getEntityObject(entityId, entityClass);        
        entityObject = EntityReflectionUtil.convertJsonObjectToEntityObject(entityClass, jsonEntity, entityObject, false);
          
        return HibernateUtil.mergeEntityObject(entityObject);
    }
    
}
