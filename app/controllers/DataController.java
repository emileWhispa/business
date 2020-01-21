package controllers;

import Helper.Finder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Expr;
import io.ebean.Expression;
import io.ebean.ExpressionList;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Optional;

@Security.Authenticated(value = DataSecurity.class)
public class DataController extends BaseController {
    @Inject
    protected DataController(FormFactory formFactory, WSClient ws) {
        super(formFactory, ws);
    }


    public Result index(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.saveProduct().absoluteURL(request);
        return ok(Product.finder.setToken(node).setSaveRoute(url).setPageTitle("List of products").setTitle("New product form").page());
    }

    public Result category(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.saveCategory().absoluteURL(request);
        return ok(ProductCategory.finder.setToken(node).setSaveRoute(url).setPageTitle("List of categories").setTitle("New category form").page());
    }

    public Result employees(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.saveEmployee().absoluteURL(request);
        return ok(User.finder.setToken(node).setSaveRoute(url).setPageTitle("List of employees").setTitle("New employee form").page());
    }

    public Result loadProducts(Http.Request request){
        return ok(Product.finder.nodeList());
    }



    public Result salesHistory(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.saveSales().absoluteURL(request);

        Expression expression = null;
        Optional<String> username = request.header("username");
        if( username.isPresent() ){
            expression = Expr.eq("user.username",username.get());
        }
        return ok(Sale.finder.setToken(node).setPageExp(expression).setSaveRoute(url).setPageTitle("Sales history").setTitle("New sales form").page());
    }

    public Result purchaseHistory(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.savePurchase().absoluteURL(request);

        Expression expression = null;
        Optional<String> username = request.header("username");
        if( username.isPresent() ){
            expression = Expr.eq("user.username",username.get());
        }
        return ok(History.finder.setToken(node).setPageExp(expression).setSaveRoute(url).setPageTitle("Purchases history").setTitle("New sales form").page());
    }



    public Result saveCategory(Http.Request request){
        ProductCategory category = ProductCategory.finder.formData(formFactory, request);
        category.save();
        return one();
    }


    public Result viewSaleReport(Http.Request request){

        DynamicForm form = formFactory.form().bindFromRequest(request);
        ExpressionList<Sale> query = Sale.finder.query();
        Optional<String> product = form.field("product").value();
        if( product.isPresent() && !product.get().equals("0")){
            query.eq("product.id",product.get());
        }
        Optional<String> start = form.field("start").value();
        Optional<String> end = form.field("end").value();
        if( start.isPresent() && end.isPresent() ){
            query.between("date",start.get(),end.get());
        }
        return ok(Json.toJson(query.findList()));
    }


    public Result viewHistoryReport(Http.Request request){
        DynamicForm form = formFactory.form().bindFromRequest(request);
        ExpressionList<History> query = History.finder.query();
        Optional<String> start = form.field("start").value();
        Optional<String> end = form.field("end").value();
        Optional<String> product = form.field("product").value();
        if( product.isPresent() && !product.get().equals("0")){
            query.eq("product.id",product.get());
        }
        if( start.isPresent() && end.isPresent() ){
            query.between("date",start.get(),end.get());
        }
        return ok(Json.toJson(query.findList()));
    }



    public Result saveEmployee(Http.Request request){
        User category = User.finder.formData(formFactory, request);
        category.role = "employee";
        category.save();
        return one();
    }


    public Result savePurchase(Http.Request request){
        History category = History.finder.formData(formFactory, request);

        Optional<String> username = request.header("username");
        username.ifPresent(s -> category.user = User.finder.query().eq("username", s).setMaxRows(1).findOne());
        category.save();
        return one();
    }



    public Result saveSales(Http.Request request){
        Sale category = Sale.finder.formData(formFactory, request);
        Optional<String> username = request.header("username");
        username.ifPresent(s -> category.user = User.finder.query().eq("username", s).setMaxRows(1).findOne());
        category.save();
        return one();
    }

    public Result saveProduct(Http.Request request){
        Product product = Product.finder.formData(formFactory, request);
        product.save();
        return one();
    }

    private Result updateProduct(long id,Http.Request request){
        Form<Product> productForm = formFactory.form(Product.class).bindFromRequest(request);
        if(!productForm.hasErrors()) {
            Product product = productForm.get();
            product.id = id;
            product.update();
        }

        return one();
    }

    private Result updateSalesHist(long id,Http.Request request){
        Form<Sale> productForm = formFactory.form(Sale.class).bindFromRequest(request);
        if(!productForm.hasErrors()) {
            Sale product = productForm.get();
            product.id = id;
            product.update();
        }

        return one();
    }

    private Result updatePurchaseHist(long id,Http.Request request){
        Form<History> productForm = formFactory.form(History.class).bindFromRequest(request);
        if(!productForm.hasErrors()) {
            History product = productForm.get();
            product.id = id;
            product.update();
        }

        return one();
    }

    private Result updateCategory(long id,Http.Request request){
        Form<ProductCategory> productForm = formFactory.form(ProductCategory.class).bindFromRequest(request);
        if(!productForm.hasErrors()) {
            ProductCategory product = productForm.get();
            product.id = id;
            product.update();
        }

        return one();
    }

    public Result saveEdited(long id,String clazz,Http.Request request){
        if( clazz.equals(Product.class.getName()) ) return updateProduct(id,request);
        if( clazz.equals(Sale.class.getName()) ) return updateSalesHist(id,request);
        if( clazz.equals(ProductCategory.class.getName()) ) return updateCategory(id,request);
        if( clazz.equals(History.class.getName()) ) return updatePurchaseHist(id,request);
        return one();
    }

    public static String getUrl(long id,Class<?> clazz){
        return routes.DataController.editModal(id,clazz.getName()).url();
    }

    public Result editModal(long id,String clazz,Http.Request request){
        try {
            Class<?> aClass = Class.forName(clazz);
            String url = routes.DataController.saveEdited(id,clazz).absoluteURL(request);
            ObjectNode node = Json.newObject();
            putToken(node,request);
            JsonNode page = new Finder<>(aClass).setToken(node).setSaveRoute(url).form(id);
            return ok(page);
        } catch (ClassNotFoundException e) {
            return one();
        }
    }
}
