
package controller.entity;

import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */
@RestController
public class Delete {
       
       @RequestMapping(
            value = "/api/{entity}/{id}",
            method = DELETE)
    public Object single(@PathVariable("entity") String entityName,
            @PathVariable("id") String entityId) throws CustomException {
        Class entityClass = EntityReflectionUtil.getClass("entity." + entityName);
        EntityReflectionUtil.stopIfOperationIsNotPermitted("DELETE", entityClass);
        HibernateUtil.deleteEntityObject(entityId, entityClass, entityName);
        return "";
    }
    
    
   

}
