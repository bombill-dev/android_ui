package com.app.bombill.Fragments.Favourite_Page;

public class Favourite_List {
    private String Vendorid_Product_Id;
    private String FishName;
    private String FishPrice;
    private String FishUnit;
    private String FishImage;
    private String  Description;

    public String getVendorid_Product_Id() {
        return Vendorid_Product_Id;
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
    public String getDescription() {
        return Description;
    }

    public String getFishImage() {
        return FishImage;
    }


    public Favourite_List(String vendorid_Product_Id, String fishName, String fishPrice, String fishUnit, String fishImage, String description) {
        Vendorid_Product_Id = vendorid_Product_Id;
        FishName = fishName;
        FishPrice = fishPrice;
        FishUnit = fishUnit;
        FishImage = fishImage;
        Description = description;
    }
}
