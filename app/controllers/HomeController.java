package controllers;

import io.ebean.Ebean;
import models.Product;
import models.ProductCategory;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends BaseController {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(Json.toJson(Ebean.createQuery(Product.class).findList()));
    }


    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi, WSClient ws) {
        super(formFactory, ws);
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }


    public Result login(Http.Request request) {
        Form<User> form = formFactory.form(User.class).bindFromRequest(request);

        if (form.hasErrors()) return ok(form.errorsAsJson());

        User user = form.get();

        User exist = user.exist();
        if (exist != null) {

            return ok(Json.newObject().put("token", getJWT())
                    .put("email",exist.email)
                    .put("role",exist.role)
                    .put("username", exist.username)
                    .put("password", user.password));
        } else {
            return ok(Json.newObject().put("error","Username or password is invalid"));
        }
    }

}
