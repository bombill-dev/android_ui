package com.app.bombill.model;

public class CartModel {

    String id;
    String name;
    String price;
    String quantity;
    String totalprice;
    String productid;
    String vendorid;

    public CartModel(String id, String name, String price, String quantity, String totalprice,String productid,String vendorid) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.productid = productid;
        this.vendorid=vendorid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalprice()
    {
        return totalprice;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }
}

