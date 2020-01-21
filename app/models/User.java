package models;

import Helper.Exist;
import Helper.Finder;
import Helper.FormProp;
import controllers.BaseController;

import javax.persistence.Entity;

@Entity
public class User extends BaseModel {
    public String fullName = "";
    @FormProp(tbl = true)
    public String email;
    public String profile = "";

    @FormProp(tbl = true)
    public String username;

    @FormProp(type = "password")
    public String password;

    @FormProp(tblOnly = true)
    public String role;

    public static Finder<User> finder = new Finder<>(User.class);



    @Exist
    public User exist(){
        return finder.existList("username",username).existList("password",password).object();
    }


    public String picture(){
        return BaseController.getPicture(profile);
    }


}
