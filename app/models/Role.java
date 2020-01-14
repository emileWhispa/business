package models;

import Helper.Finder;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;

@Entity
public class Role extends BaseModel {

    @JsonProperty
    public String name;
    public String logic;


    public static Finder<Role> finder = new Finder<>(Role.class);

    public static Role byLogic(String logic){
        return finder.query().eq("logic",logic).setMaxRows(1).findOne();
    }
}
