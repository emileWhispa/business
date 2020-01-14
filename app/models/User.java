package models;

import Helper.Exist;
import Helper.Finder;
import controllers.BaseController;

import javax.persistence.Entity;

@Entity
public class User extends BaseModel {
    public String fullName = "";

    public String email;
    public String profile = "";

    public String username;

    public String password;


    public static Finder<User> finder = new Finder<>(User.class);



    @Exist
    public boolean exist(){
        return finder.existList("username",username).existList("password",password).executeExist();
    }


    public String picture(){
        return BaseController.getPicture(profile);
    }


}
