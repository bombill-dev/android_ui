package com.app.bombill.model;

/**
 * Created by amolmhatre on 9/23/20
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorProductResponse {

    @SerializedName("vendor_product_id")
    @Expose
    private String vendorProductId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("Vendor_category_id")
    @Expose
    private String vendorCategoryId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("unit_desc")
    @Expose
    private String unitDesc;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("product_availability")
    @Expose
    private String productAvailability;

    /**
     * No args constructor for use in serialization
     *
     */
    public VendorProductResponse() {
    }

    /**
     *
     * @param productAvailability
     * @param vendorProductId
     * @param vendorCategoryId
     * @param unit
     * @param unitDesc
     * @param price
     * @param name
     * @param description
     * @param type
     * @param url
     */
    public VendorProductResponse(String vendorProductId, String name, String price, String unit, String url, String vendorCategoryId, String type, String unitDesc, String description, String productAvailability) {
        super();
        this.vendorProductId = vendorProductId;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.url = url;
        this.vendorCategoryId = vendorCategoryId;
        this.type = type;
        this.unitDesc = unitDesc;
        this.description = description;
        this.productAvailability = productAvailability;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVendorCategoryId() {
        return vendorCategoryId;
    }

    public void setVendorCategoryId(String vendorCategoryId) {
        this.vendorCategoryId = vendorCategoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(String productAvailability) {
        this.productAvailability = productAvailability;
    }

}