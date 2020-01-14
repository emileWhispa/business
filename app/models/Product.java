package models;

import Helper.Finder;
import Helper.FormProp;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Product extends BaseModel {
    @FormProp(tbl = true,display = "Product name")
    public String name;
    @FormProp(tbl = true,display = "Product package")
    public int intPackage;
    @FormProp(tbl = true,display = "Product piece")
    public int piece;
    @ManyToOne @FormProp
    public ProductCategory category;


    public static Finder<Product> finder = new Finder<>(Product.class);

    @FormProp(tblOnly = true,display = "Category name")
    public String categoryName(){
        return category.categoryName;
    }

    @JsonProperty
    public String print(){
        return name;
    }
}
