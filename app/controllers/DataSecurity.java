package controllers;

import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Optional;

public class DataSecurity extends Security.Authenticator {
    @Override
    public Optional<String> getUsername(Http.Request req) {
        Optional<String> header = req.header("access-header");
        String s = header.isPresent() && BaseController.validateJWT(header.get()) ? header.get() : null;
        return Optional.ofNullable(s);
    }


    public Result onUnauthorized(Http.Request req) {
        return ok(Json.newObject().put("error","Not authenticated").put("redirect","/user/page/home"));
    }
}
