package com.app.bombill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {

    @SerializedName("order_item_id")
    @Expose
    private String orderItemId;
    @SerializedName("name_of_product")
    @Expose
    private String nameOfProduct;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("vendor_product_id")
    @Expose
    private String vendorProductId;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderDetailResponse() {
    }

    /**
     *
     * @param vendorProductId
     * @param quantity
     * @param totalPrice
     * @param orderItemId
     * @param price
     * @param nameOfProduct
     */
    public OrderDetailResponse(String orderItemId, String nameOfProduct, String quantity, String price, String totalPrice, String vendorProductId) {
        super();
        this.orderItemId = orderItemId;
        this.nameOfProduct = nameOfProduct;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.vendorProductId = vendorProductId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public void setNameOfProduct(String nameOfProduct) {
        this.nameOfProduct = nameOfProduct;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

}