package controller.entity;

import entitymanager.EntityListReflection;
import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import hibernate.HibernateUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adi
 */
@RestController
public class Retrive {    

    @RequestMapping(
            value = "/api/{entity}",
            method = GET)
    public Object list(HttpServletRequest request, @PathVariable("entity") String entityName,
            @RequestParam("_per_page") Optional<String> perPage,
            @RequestParam("_page") Optional<String> page) throws CustomException {

        Class classe = EntityReflectionUtil.createClassWithEntityName("entity." + entityName);     
        EntityReflectionUtil.stopIfOperationIsNotPermitted("GET", classe);
                    
        Map<String, String[]> parameters = new HashMap<> (request.getParameterMap());        
     
        int perPageInt = 10;
        int pageInt = 1;
        
        String queryParams = HibernateUtil.createParametersQuery(classe , parameters);
        
        if (perPage.isPresent() && page.isPresent()) {     
        perPageInt = EntityReflectionUtil.validateAndConvertStringToNumber("_per_page", perPage.get());
         pageInt = EntityReflectionUtil.validateAndConvertStringToNumber("_page", page.get());
        }

        return new EntityListReflection(entityName, perPageInt, pageInt, queryParams);
    } 
    
    
      
    @RequestMapping(
            value = "/api/{entity}/{id}",
            method = GET)
    public Object single(@PathVariable("entity") String entityName,
            @PathVariable("id") String entityId) throws CustomException, ClassNotFoundException {              
        Class entityClass = EntityReflectionUtil.createClassWithEntityName("entity." + entityName);
        EntityReflectionUtil.stopIfOperationIsNotPermitted("GET", entityClass);         
        return HibernateUtil.getEntityObject(entityId, entityClass);
    }
    
}
