package com.app.bombill.Fragments.Your_order;

/**
 * Created by amolmhatre on 7/26/20
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("vendor_order_id")
    @Expose
    private String vendor_order_id;
    @SerializedName("purchase_date")
    @Expose
    private String purchase_date;
    @SerializedName("payment_status")
    @Expose
    private String payment_status;
    @SerializedName("payment_method")
    @Expose
    private String payment_method;
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("order_status")
    @Expose
    private String order_status;
    @SerializedName("grand_total")
    @Expose
    private String grand_total;
    @SerializedName("vendor_Name")
    @Expose
    private String vendor_Name;

    public String getVendor_order_id() {
        return vendor_order_id;
    }

    public void setVendor_order_id(String vendor_order_id) {
        this.vendor_order_id = vendor_order_id;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getVendor_Name() {
        return vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        this.vendor_Name = vendor_Name;
    }
}
