package models;

import Helper.Finder;
import Helper.FormProp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Sale extends BaseModel {
    @ManyToOne @FormProp
    public Product product;
    @FormProp(tbl = true,display = "Package")
    public int intPackage;
    @FormProp(tbl = true)
    public int piece;
    @ManyToOne
    public User user;
    @FormProp(tbl = true)
    public double commission;

    public static Finder<Sale> finder = new Finder<>(Sale.class);

    @FormProp(tblOnly = true,display = "Product name")
    public String print(){
        return product.name;
    }

}
