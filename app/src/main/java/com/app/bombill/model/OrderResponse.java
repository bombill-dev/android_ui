package com.app.bombill.model;

/**
 * Created by amolmhatre on 7/31/20
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("vendor_order_id")
    @Expose
    private String vendorOrderId;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getVendorOrderId() {
        return vendorOrderId;
    }

    public void setVendorOrderId(String vendorOrderId) {
        this.vendorOrderId = vendorOrderId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
