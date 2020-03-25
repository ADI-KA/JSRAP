package entitymanager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.entity.Schema;
import annotations.ActionDescriptor;
import annotations.EntityDescriptor;
import annotations.FieldDescriptor;
import exceptionhandler.CustomException;
import hibernate.HibernateUtil;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;
import sun.print.resources.serviceui_zh_TW;

/**
 *
 * @author adi
 */
public class EntityReflectionUtil {

    private static EntityReflectionUtil single_instance = null;

    private EntityReflectionUtil() {
    }

    public static EntityReflectionUtil getInstance() {
        if (single_instance == null) {
            single_instance = new EntityReflectionUtil();
        }

        return single_instance;
    }

    public static ArrayList<Method> findSettersOrGetters(Class<?> c, boolean setters) {
        ArrayList<Method> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if ((isMethodASetter(method) && setters) || (isMethodAGetter(method) && !setters)) {
                list.add(method);
            }
        }
        return list;
    }

    private static boolean isMethodAGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers())
                && method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*")
                    && !method.getReturnType().equals(void.class)) {
                return true;
            }
            if (method.getName().matches("^is[A-Z].*")
                    && method.getReturnType().equals(boolean.class)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isMethodASetter(Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(void.class)
                && method.getParameterTypes().length == 1
                && method.getName().matches("^set[A-Z].*");
    }

    public static Object convertJsonObjectToEntityObject(Class myClass, Object jsonEntity, Object entity, boolean create) throws CustomException, ClassNotFoundException {
        Field[] fields = myClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                Object jsonEntityValue = PropertyUtils.getProperty(jsonEntity, field.getName());
                if (jsonEntityValue == null) {
                    if (create && field.isAnnotationPresent(FieldDescriptor.class) && field.getAnnotation(FieldDescriptor.class).requierd()) {
                        CustomException ce = new CustomException();
                        ce.setDescription(field.getName() + "is a requierd field");
                        ce.setCode(400);
                        throw ce;
                    }
                    continue;
                } 
                
                // TO DO improve mapping * to Many Relation 
                    
                if(field.getType().getCanonicalName() == "java.util.Set"){

                    ArrayList idList = (ArrayList) jsonEntityValue;
                    Set<Object> objectSet = new HashSet<>();

                    for (int i = 0; i < idList.size(); i++){
                        Object object = castAndRetriveObject("entity."+ getParsedTypeNameFromField(field) ,idList.get(i).toString());
                        objectSet.add(object);
                    }

                    PropertyUtils.setProperty(entity, field.getName(), objectSet);

                }else  if(field.getType().getCanonicalName() == "java.util.List"){

                    ArrayList idList = (ArrayList) jsonEntityValue;
                    List<Object> objectSet = new ArrayList<>();

                    for (int i = 0; i < idList.size(); i++){
                        Object object = castAndRetriveObject("entity."+ getParsedTypeNameFromField(field) ,idList.get(i).toString());
                        objectSet.add(object);
                    }

                    PropertyUtils.setProperty(entity, field.getName(), objectSet);
                }else if(field.getType().getCanonicalName().contains("entity")){
                    Object object = castAndRetriveObject(field.getType().getCanonicalName() ,jsonEntityValue.toString());
                    PropertyUtils.setSimpleProperty(entity, field.getName(), object);
                }else{                
                    Class<?> castClass = Class.forName(field.getType().getCanonicalName());
                    PropertyUtils.setSimpleProperty(entity, field.getName(), castClass.cast(jsonEntityValue));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                CustomException ce = new CustomException();
                ce.setDescription(ex.getMessage());
                Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);

                throw ce;
            }

        }
        return entity;
    }

    
    
    

    public static Class createClassWithEntityName(String entityName) throws CustomException {

        Class myClass = null;

        try {
            myClass = Class.forName(entityName);

        } catch (ClassNotFoundException e) {
            CustomException ce = new CustomException();
            ce.setDescription(entityName + " is not a valid Entity Name!");
            throw ce;
        }

        return myClass;
    }

    public static Class[] getClassesFromPackage(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (Iterator it = dirs.iterator(); it.hasNext();) {
            File directory = (File) it.next();
            classes.addAll(getClassesNamesFromPackage(directory, packageName));
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    public static List getClassesNamesFromPackage(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(getClassesNamesFromPackage(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        
        return classes;
    }

    public static ArrayList getFieldListFromClass(Class classe) {
        Field[] fields = classe.getDeclaredFields();
        ArrayList fieldsList = new ArrayList();

        for (Field field : fields) {
            HashMap<String, Object> fieldMap = new HashMap<>();
            String label = field.getName();
            boolean readable = true;
            boolean requierd = false;
            boolean writable = true;

            if (field.isAnnotationPresent(FieldDescriptor.class)) {
                FieldDescriptor fieldDescriptor = field.getAnnotation(FieldDescriptor.class);
                label = fieldDescriptor.label();
                readable = fieldDescriptor.readable();
                requierd = fieldDescriptor.requierd();
                writable = fieldDescriptor.writable();
            }

            fieldMap.put("label", label);
            fieldMap.put("readable", readable);
            fieldMap.put("requierd", requierd);
            fieldMap.put("writable", writable);
            fieldMap.put("name", field.getName());
            fieldMap.put("type", getParsedTypeNameFromField(field));            
            fieldsList.add(fieldMap);            

        }

        return fieldsList;
    }

    public static ArrayList getOperationListFromClass(Class classe) {
        ArrayList operationsList = new ArrayList();

        if (classe.isAnnotationPresent(EntityDescriptor.class)) {

            EntityDescriptor entityDescriptor = (EntityDescriptor) classe.getAnnotation(EntityDescriptor.class);

            ActionDescriptor[] actionDescriptors = entityDescriptor.actions();

            for (ActionDescriptor actionDescriptor : actionDescriptors) {
                HashMap<String, Object> actionMap = new HashMap<>();
                actionMap.put("name", actionDescriptor.nameSpace());
                actionMap.put("path", actionDescriptor.path());
                actionMap.put("label", actionDescriptor.label());
                if (!actionDescriptor.input().equals("")) {
                    actionMap.put("input", actionDescriptor.input());
                }
                actionMap.put("output", classe.getSimpleName());
                actionMap.put("method", actionDescriptor.httpMethod().toString());
                operationsList.add(actionMap);
            }
        }
        return operationsList;
    }

    public static void stopIfOperationIsNotPermitted(String nameSpace, Class entityClass) throws CustomException {

        if (entityClass.isAnnotationPresent(EntityDescriptor.class)) {
            EntityDescriptor entityDescriptor = (EntityDescriptor) entityClass.getAnnotation(EntityDescriptor.class);
            ActionDescriptor[] actionDescriptors = entityDescriptor.actions();
            for (ActionDescriptor actionDescriptor : actionDescriptors) {
                if (actionDescriptor.nameSpace().equals(nameSpace)) {
                    return;
                }
            }
        }
            CustomException ce = new CustomException();
            ce.setDescription("Operation is not permitted!");
            throw ce;
    }
    
    private static String getParsedTypeNameFromField(Field field){
        String fieldType = "";
       if(field.getGenericType() instanceof ParameterizedType){
                ParameterizedType aType = (ParameterizedType) field.getGenericType();
               fieldType =  aType.getActualTypeArguments()[0].toString();
            }else{
                fieldType =  field.getType().getCanonicalName();
            }
       
        int lastIndex = fieldType.split("\\.").length-1;
        fieldType = fieldType.split("\\.")[lastIndex];
        return fieldType;
    }
    
    
    public static int validateAndConvertStringToNumber(String numberName, String numberStringValue) throws CustomException{        
        int number = 0;
        try {
                number = Integer.parseInt(numberStringValue);                
            } catch (NumberFormatException nfe) {
                CustomException e = new CustomException();
                e.setDescription(numberName + " is not a integer value");
                throw e;
            } catch (Exception e) {
                throw e;
            } finally {
                if (number < 0 ) {
                    CustomException e = new CustomException();
                    e.setDescription(numberName + " is not a positive integer");
                    throw e;
                }
            }
        return number;
    
    }
    
    public static Object castAndRetriveObject(String entityName, String entityId) throws CustomException{
     Class entityClass = EntityReflectionUtil.createClassWithEntityName(entityName);
        EntityReflectionUtil.stopIfOperationIsNotPermitted("GET", entityClass); 
        return HibernateUtil.getEntityObject(entityId, entityClass);
    }

}
