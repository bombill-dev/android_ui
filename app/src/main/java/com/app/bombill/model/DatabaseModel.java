package com.app.bombill.model;

/**
 * Created by amolmhatre on 9/24/20
 */

public class DatabaseModel {
    String vendor_id;
    String product_id;
    String product_name;
    String product_quantity;
    String product_price;
    String product_total;
    String id;

    public DatabaseModel(String vendor_id, String product_id, String product_name, String product_quantity, String product_price, String product_total, String id) {
        this.vendor_id = vendor_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.product_total = product_total;
        this.id = id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_total() {
        return product_total;
    }

    public void setProduct_total(String product_total) {
        this.product_total = product_total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
