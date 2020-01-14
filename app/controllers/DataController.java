package controllers;

import Helper.Finder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.History;
import models.Product;
import models.ProductCategory;
import models.Sale;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

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

    public Result salesHistory(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.saveSales().absoluteURL(request);
        return ok(Sale.finder.setToken(node).setSaveRoute(url).setPageTitle("Sales history").setTitle("New sales form").page());
    }

    public Result purchaseHistory(Http.Request request){
        ObjectNode node = Json.newObject();
        putToken(node,request);
        String url = routes.DataController.savePurchase().absoluteURL(request);
        return ok(History.finder.setToken(node).setSaveRoute(url).setPageTitle("Purchases history").setTitle("New sales form").page());
    }



    public Result saveCategory(Http.Request request){
        ProductCategory category = ProductCategory.finder.formData(formFactory, request);
        category.save();
        return one();
    }


    public Result savePurchase(Http.Request request){
        History category = History.finder.formData(formFactory, request);
        category.save();
        return one();
    }



    public Result saveSales(Http.Request request){
        Sale category = Sale.finder.formData(formFactory, request);
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
