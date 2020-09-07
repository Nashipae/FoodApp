package android.example.foodapp.Model;

public class Products {
    private String prod_desc, prod_id, prod_name, prod_price, prod_raters, prod_rating, prod_stock;
    public Products(){

    }

    public Products(String prod_desc, String prod_id, String prod_name, String prod_price, String prod_raters, String prod_rating, String prod_stock) {
        this.prod_desc = prod_desc;
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
        this.prod_raters = prod_raters;
        this.prod_rating = prod_rating;
        this.prod_stock = prod_stock;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_raters() {
        return prod_raters;
    }

    public void setProd_raters(String prod_raters) {
        this.prod_raters = prod_raters;
    }

    public String getProd_rating() {
        return prod_rating;
    }

    public void setProd_rating(String prod_rating) {
        this.prod_rating = prod_rating;
    }

    public String getProd_stock() {
        return prod_stock;
    }

    public void setProd_stock(String prod_stock) {
        this.prod_stock = prod_stock;
    }
}