package entitymanager;

import controller.entity.Schema;
import hibernate.HibernateFactory;
import exceptionhandler.CustomException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;


/**
 *
 * @author Adi Omanovic
 */
public final class EntityListReflection {

    private String context;
    private String collectionPath;
    private int perPage;
    private int page;
    
    private Object[] members;
    
    private int totalItems;

    public EntityListReflection(String context, int perPage, int page, String queryParams) throws CustomException {
        this.context = context;
        this.perPage = perPage;
        this.page = page;
        this.getEntity(perPage, page, queryParams );
        this.collectionPath = "/api/" + context;        
    }

    public void getEntity(int perPage, int page, String queryParams) throws CustomException {
        
        HashMap<String, Integer> range = this.getPaginationRange(perPage, page);
        
        Session session = HibernateFactory.getSessionFactory().openSession();        
         
        String hql = "from " + this.context + " e"; 

        if(!queryParams.equals("")){
           hql=  hql.concat(" where " + queryParams);
        }
        
        Query query = session.createQuery(hql);        
        
        List<Object> entityList = null;

        Transaction trs = null;
        try {
            trs = session.beginTransaction();
            
            Long b = (Long) session.createQuery( "select count(*) " + hql).uniqueResult();
            
            this.totalItems = b.intValue();                
          
            query.setFirstResult(range.get("firstResult"));
            query.setMaxResults(range.get("maxResult"));

            entityList = query.list();
            
            trs.commit();
        } catch (HibernateException exc) {
                Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, exc);

            if (trs != null) {
                trs.rollback();
            }
            
             if (entityList == null) {
                CustomException e = new CustomException(); 
                e.setDescription(context + ": Not Found!");
                e.setCode(404);                
                throw e;
            }
             
        }finally {
            session.close();
        }
         
        this.members = new Object[entityList.size()];
        this.members = entityList.toArray();        
        
    }
    
    private HashMap getPaginationRange(int perPage, int page){
    
          HashMap<String,Integer> range = new HashMap<>();
          
        switch (page) {
            case 1:
                range.put("firstResult", 0);
                range.put("maxResult", perPage);
                break;
            case 2:
                range.put("firstResult", perPage);
                range.put("maxResult", perPage);
                break;
            default:
                range.put("firstResult", perPage * (page - 1 ));
                range.put("maxResult", perPage);
                break;
        }
           
        return range;
    }

    
    public Object[] getMembers() {
        return members;
    }
    
    public void setMembers(Object[] members) {
        this.members = members;
    }
    
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCollectionPath() {
        return collectionPath;
    }

    public void setCollectionPath(String collectionPath) {
        this.collectionPath = collectionPath;
    }

    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    
    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
