/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate;

import annotations.ApiFilter;
import annotations.FilterType;
import controller.entity.Schema;
import entitymanager.EntityReflectionUtil;
import exceptionhandler.CustomException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author adi
 */
public class HibernateUtil {
    
    public static Object persistEntityObject(Object entityObject) throws  CustomException{
      
            Transaction trs = null;
            Session session;
            try {
                session = HibernateFactory.getSessionFactory().openSession();
                trs = session.beginTransaction();
                session.saveOrUpdate(entityObject);
                trs.commit();
            } catch (Exception exc) {

                if (trs != null) {
                    trs.rollback();
                }
                CustomException ce = new CustomException();
                ce.setDescription(exc.getMessage());
                Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, exc);
                throw ce;
            } 
            return entityObject;
    }
    
        public static Object mergeEntityObject(Object entityObject) throws  CustomException{
      
            Transaction trs = null;
            Session session;

            try {
                session = HibernateFactory.getSessionFactory().openSession();
                trs = session.beginTransaction();
                session.merge(entityObject);
                trs.commit();
            } catch (HibernateException exc) {

                if (trs != null) {
                    trs.rollback();
                }
                CustomException ce = new CustomException();
                ce.setDescription(exc.getMessage());
                Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, exc);
                throw ce;
            }
            
            return entityObject;
    }
        
        
        public static void deleteEntityObject(String id, Class entityClass, String entityName) throws CustomException{
      
        Object entityObject = null;
        Transaction trs = null;
        Session session;

        try {
            session = HibernateFactory.getSessionFactory().openSession();
            trs = session.beginTransaction();
            entityObject = session.get(entityClass, Integer.parseInt(id));
            session.delete(entityObject);
            trs.commit();
        } catch (Exception exc) {            
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, exc);

            if (trs != null) {
                trs.rollback();
            }
        }
        
          if (entityObject == null) {
            CustomException ce = new CustomException();

            ce.setDescription(entityName + ": " + id + " Not Found");
            throw ce;
        }
            
    }
        
    public static Object getEntityObject(String id, Class entityClass) throws CustomException {
      
        Object entityObject = null;
        Transaction trs = null;
        Session session;

        try {
            String idType = entityClass.getDeclaredField("id").getType().getSimpleName();
            session = HibernateFactory.getSessionFactory().openSession();
            trs = session.beginTransaction();
            entityObject = idType.equals("Long")?session.get(entityClass, Long.parseLong(id)): session.get(entityClass, Integer.parseInt(id));
            trs.commit();
        } catch (HibernateException exc) {
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, exc);
            if (trs != null) {
                trs.rollback();
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        if (entityObject == null) {
            CustomException ce = new CustomException();

            ce.setDescription("Entity with  " + id + " Not Found");
            throw ce;
        }        
        
            return entityObject;
    }
    
        public static String createParametersQuery(Class entityClass, Map<String, String[]> parameters) {

        String queryParameters = "";
        // TO DO 
        for (String key : parameters.keySet()) {
            Field field;
            try {
                if (!"_per_page".equals(key) && !"_page".equals(key) && (field = entityClass.getDeclaredField(key)) != null && field.isAnnotationPresent(ApiFilter.class)) {
                    ApiFilter apiFilter = field.getAnnotation(ApiFilter.class);

                    if (!queryParameters.equals("")) {
                        queryParameters = queryParameters.concat(" or ");
                    }

                    if (apiFilter.type() == FilterType.Partial) {
                        queryParameters = queryParameters.concat("e." + key + " like '%" + parameters.get(key)[0] + "%'");
                    } else if (apiFilter.type() == FilterType.Exact) {
                        queryParameters = queryParameters.concat("e." + key + "='" + parameters.get(key)[0] + "'");
                    }
                }
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(EntityReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return queryParameters;
    }
        
        
}
