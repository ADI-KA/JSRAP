

package entity;

/**
 *
 * @author adi
 */

import annotations.ActionDescriptor;
import annotations.EntityDescriptor;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.http.HttpMethod;

@Entity
@Table(name="product")
@EntityDescriptor(actions={@ActionDescriptor(httpMethod = HttpMethod.GET, nameSpace = "GET"), @ActionDescriptor(httpMethod = HttpMethod.POST, nameSpace = "POST"), @ActionDescriptor(httpMethod = HttpMethod.DELETE, nameSpace = "DELETE")})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="price")
    private Integer price;
    
    
    @Column(name="description")
    private String description;
    
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    //@JsonIgnore
    private Set<ProductAsset> productAssets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
    public Integer getPrice() {
        return price;
    }

   
    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<ProductAsset> getProdcutAssets() {
        return productAssets;
    }
    
    public void setProdcutAssets(Set<ProductAsset> prodcutAssets) {
        this.productAssets = prodcutAssets;
    }

    
}
