package models;

import Helper.FormProp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class UserRole extends BaseModel {
    @FormProp @ManyToOne(cascade = CascadeType.ALL)
    public User user;
    @FormProp @ManyToOne(cascade = CascadeType.ALL)
    public  Role role;


}
