package models;

import Helper.Finder;
import Helper.FormProp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class History extends BaseModel {
    @ManyToOne @FormProp
    public Product product;
    @FormProp(tbl = true,display = "Package")
    public int intPackage;
    @FormProp(tbl = true)
    public int piece;
    @ManyToOne
    public User user;


    public static Finder<History> finder = new Finder<>(History.class);

    @FormProp(tblOnly = true,display = "Product name")
    public String print(){
        return product.name;
    }
}
