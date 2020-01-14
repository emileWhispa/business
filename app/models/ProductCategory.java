package models;

import Helper.Finder;
import Helper.FormProp;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;

@Entity
public class ProductCategory extends BaseModel{
    @FormProp(tbl = true,display = "Category name")
    public String categoryName;

    public static Finder<ProductCategory> finder = new Finder<>(ProductCategory.class);


    @JsonProperty
    public String print(){
        return categoryName;
    }
}
