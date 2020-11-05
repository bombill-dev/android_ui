package com.app.bombill.model;

public class HomePageVendorList {

    private String Image;
    private String Name;
    private String VendorId;
    private String deliverydistance;
    private String min_amount;
    private int deliverydistancefromuser;

    public String getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public String getVendorId()
    {
        return VendorId;
    }

    public String getDeliverydistance()
    {
        return deliverydistance;
    }
    public String getMin_amount()
    {
        return min_amount;
    }

    public int getDeliverydistancefromuser()
    {
        return deliverydistancefromuser;
    }

    public HomePageVendorList(String vendorId, String Name, String Image, String deliverydistance, String min_amount, int deliverydistancefromuser) {
        this.VendorId=vendorId;
        this.Image = Image;
        this.Name = Name;
        this.deliverydistance = deliverydistance;
        this.min_amount=min_amount;
        this.deliverydistancefromuser=deliverydistancefromuser;
    }
}
