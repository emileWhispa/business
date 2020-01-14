package controllers;

import Helper.Finder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.CSRF;
import play.libs.Files.TemporaryFile;
import play.libs.Json;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static io.ebean.Ebean.json;

abstract public class BaseController extends Controller implements WSBodyReadables, WSBodyWritables {
    final FormFactory formFactory;
    public final String defaultUser = "def-user-session";
    public final String rolePrint = "def-user-session-the-role";
    public static final String adminSession = "admin";
    public static final String boardSession = "board";
    public static final String representativeSession = "representative";
    public static final String cashierSession = "cashier";
    public static final String storeKeeperSession = "storeKeeper";
    public static final String memberSession = "member_session";
    private final WSClient ws;

    public String getJWT(){
        Date now = new Date();
        long t = now.getTime();
        Date expirationTime = new Date(t + 1300819380);

        return Jwts.builder()
                .setSubject("any subject")
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, "username")
                .compact();
    }

    public static boolean validateJWT(String jwt){
        try {
            Jwts.parser().setSigningKey("username").parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;

        }

    }



    private ObjectNode oneObject(){
        return Json.newObject().put("status",true).put("text","done");
    }

    Result one(){
        return ok(oneObject());
    }

    protected BaseController( FormFactory formFactory, WSClient ws) {
        this.formFactory = formFactory;
        this.ws = ws;
    }

    public static boolean isNumeric(String v) {
        try {
            Double.parseDouble(v);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private Optional<String> getUser(Http.Request request){
        return request.session().getOptional(defaultUser);
    }


    protected void putToken(ObjectNode node, Http.Request request) {
        Optional<CSRF.Token> token = CSRF.getToken(request);
        token.ifPresent(t -> {
            node.put("tokenName", t.name());
            node.put("tokenValue", t.value());
        });
    }


    static String uploadFile(Http.Request request, String dft, String fName) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile(fName);
        return singleFile(picture, dft);
    }



    static String randomString() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        UUID uuid2 = UUID.randomUUID();
        String randomUUIDString2 = uuid2.toString();
        return randomUUIDString + randomUUIDString2;
    }

    static String singleFile(Http.MultipartFormData.FilePart<TemporaryFile> file, String dft) {
        try {
            if (file != null) {
                String fileName = file.getFilename();
                TemporaryFile temp = file.getRef();
                String text = (new Date().getTime()) + randomString() + fileName;
                //final boolean b = temp.renameTo(new File(getDef(), text));

                temp.copyTo(Paths.get(getDef() + "/" + text), true);
                return text;
            } else {
                return dft;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return dft;
        }
    }
    private static String rootFolder = "public/";


    protected static String defaultFolder() {
        return rootFolder + "uploads/";
    }

    public static String getDef() {
        File testExist = new File(defaultFolder());
        if (!testExist.exists()) {
            boolean mkDir = testExist.mkdir();
        }
        return defaultFolder();
    }

    public static String getPicture(String pro){
        // return pro == null || pro.isEmpty() ? routes.Assets.versioned(Assets.Asset.apply("assets/images/boys.jpg")).url() : routes.Assets.versioned(Assets.Asset.apply("uploads/"+pro)).url();
        return  "assets"+pro;
    }

    void sendSms(String text,String phone,long id){
        String token = "bZALZyzqQJvrMvFWgn9KN5lkWChVjikj";

        String url = "https://sms.besoft.rw/api/v1/client/bulksms";
        JsonNode json = Json.newObject()
                .put("id", id)
                .put("message",text)
                .put("token",token)
                .put("sender_name","I-ACHIEVERS")
                .put("phone","+25"+phone);


        CompletionStage<WSResponse> post = ws.url(url).post(json);


         post.thenApply( response -> {
            JsonNode body = response.getBody(json());

            System.out.println(response.toString());
            return ok();
        });
    }



    public Result userEditor(Http.Request request, long id, String string) {


        try {
            Class<?> aClass = Class.forName(string);
            Finder<?> finder = new Finder<>(aClass);
            finder.formData(id, formFactory, request);


            finder.update(id);
        } catch (ClassNotFoundException e) {

            return one();
        }


        return one();
    }



}
