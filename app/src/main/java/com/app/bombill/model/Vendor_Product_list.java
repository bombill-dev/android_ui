package com.app.bombill.model;

public class Vendor_Product_list {

    private String Vendor_Product_id;
    private String FishName;
    private String FishPrice;
    private String FishUnit;
    private String FishImage;
    private String VendorCategoryId;


    /**
     params.put("customer_id", Customer_ID);
     //vendor_product_id
     params.put("total_price", total_price);
     params.put("service_charge", service_charge);
     params.put("delivery_charge", delivery_charge);
     params.put("final_total", FinalTotal);
     params.put("payment_method", PaymentMethod);
     params.put("transaction_id", transaction_id);
     params.put("customer_address", customer_address);
     params.put("customer_lat_lon", LatLog);*/

    public String getVendor_Product_id() {
        return Vendor_Product_id;
    }

    public String getFishName() {
        return FishName;
    }

    public String getFishPrice() {
        return FishPrice;
    }

    public String getFishUnit() {
        return FishUnit;
    }

    public String getFishImage() {
        return FishImage;
    }

    public String getVendorCategoryId() {
        return VendorCategoryId;
    }


    public Vendor_Product_list(String vendor_Product_id, String fishName, String fishPrice, String fishUnit, String fishImage, String vendorCategoryId) {
        Vendor_Product_id= vendor_Product_id;
        FishName = fishName;
        FishPrice = fishPrice;
        FishUnit = fishUnit;
        FishImage = fishImage;
        VendorCategoryId = vendorCategoryId;
    }
}
