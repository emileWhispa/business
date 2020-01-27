package models;

import Helper.Exist;
import Helper.Finder;
import Helper.FormProp;
import controllers.BaseController;
import io.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Objects;

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
    public User exist() {
        return finder.existList("username", username).existList("password", password).object();
    }


    public String picture() {
        return BaseController.getPicture(profile);
    }


    public int Count(String start, String end) {
        return Sale.finder.query().between("date", start, end).eq("user.id", id).findCount();
    }


    public int Count2(String start, String end) {
        return Objects.requireNonNull(Ebean
                .createSqlQuery("SELECT ( SUM(s.int_package) * SUM(s.piece) ) as sum FROM sale s WHERE s.user_id=:id AND (s.date between :start and :end)")
                .setParameter("start",start)
                .setParameter("id",id)
                .setParameter("end",end).findOne()).getInteger("sum");
    }


    @Transient
    public int count;

    @Transient
    public int count2;

}
