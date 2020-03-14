

package entity;

/**
 *
 * @author adi
 */

import annotations.ActionDescriptor;
import annotations.EntityDescriptor;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.http.HttpMethod;
import javax.persistence.JoinColumn;

@Entity
@Table(name="ProductAsset")
@EntityDescriptor(actions={@ActionDescriptor(httpMethod = HttpMethod.GET, nameSpace = "GET"), @ActionDescriptor(httpMethod = HttpMethod.POST, nameSpace = "POST"), @ActionDescriptor(httpMethod = HttpMethod.DELETE, nameSpace = "DELETE"), @ActionDescriptor(httpMethod = HttpMethod.PUT, nameSpace = "PUT")})

public class ProductAsset implements Serializable {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="path")
    private String path;
        
    @Column(name="type")
    private String type;
    
    
//    @ManyToOne
//    @JoinColumn(name="product_id", nullable=false)
    private Product product;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
     
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

   
    
}
