package models;

import Helper.FormProp;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.DataController;
import io.ebean.Model;
import play.libs.Json;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@MappedSuperclass
abstract public class BaseModel extends Model {
   @Id
   public long id;

   public Date date = new Date();

   @FormProp(tblOnly = true,order = 998)
   public JsonNode edit(){
      return Json.newObject().put("edit",true).put("url", DataController.getUrl(id,this.getClass()));
   }

   @FormProp(tblOnly = true,display = "Delete",order = 999)
   public JsonNode deleteButton(){
      return Json.newObject().put("delete",true).put("url", DataController.getUrl(id,this.getClass()));
   }


   protected String fileSize(double bytes) {
      double kilobytes = (bytes / 1024);
      double megabytes = (kilobytes / 1024);
      double gigabytes = (megabytes / 1024);
      double terabytes = (gigabytes / 1024);
      double petabytes = (terabytes / 1024);
      double exabytes = (petabytes / 1024);
      double zettaBytes = (exabytes / 1024);
      double yottaBytes = (zettaBytes / 1024);

      return bytes <= 1024 ? bytes + " B" : kilobytes <= 1024 ? kilobytes + " KB" : megabytes <= 1024 ? megabytes + " MB" : gigabytes + " GB";

   }


   private static NumberFormat formatInstance = NumberFormat.getNumberInstance(Locale.US);


   private static String pattern = "EEE , dd-MMM-yyyy HH:mm:ss";
   private static String pattern2 = "yyyy-MM-dd";
   private static SimpleDateFormat format = new SimpleDateFormat(pattern);
   private static SimpleDateFormat format2 = new SimpleDateFormat(pattern2);

   public static String formatNbr(double dbl) {
      return formatNbr(dbl,"");
   }

   public static String formatNbr2(double dbl) {
      return formatNbr(dbl," $");
   }

   protected static String formatNbr(double dbl,String s) {
      return formatInstance.format(dbl) + s;
   }

   public String dateFmt(){
      return format.format(date);
   }

   protected String format2(Date date){
      return format2.format(date);
   }


   @JsonProperty
   public String print(){
      return this.getClass().getName();
   }
}
