package com.app.bombill.Activities;

/**
 * Created by amolmhatre on 9/28/20
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bombill.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Vendor_Product_Detail extends AppCompatActivity {
    private static final String TAG = "Vendor_Product_Detail";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static RequestOptions options;
    private static String strProductId,strProductPhoto, strProductName,
            strPrice, strProductType,strProductUnit,
            strProductUnitDescription, strProductCategory,
            strInventory, strProductDescription;
    private static TextView tvProductName, tvPricePerUnit, tvInventory, tvProductDescription;
    private static ImageView ivProductPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_product_details);

        Bundle bundle = getIntent().getExtras();
        strProductId = bundle.getString("product_id");
        strProductPhoto = bundle.getString("product_photo");
        strProductName = bundle.getString("product_name");
        strPrice = bundle.getString("product_price");
        strProductType = bundle.getString("product_type");
        strProductUnit = bundle.getString("product_unit");
        strProductUnitDescription = bundle.getString("product_unitDescription");
        strProductCategory = bundle.getString("product_category");
        strInventory = bundle.getString("product_availability");
        strProductDescription = bundle.getString("product_description");

        ivProductPhoto = (ImageView) findViewById(R.id.ivProductPhoto);
        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvPricePerUnit = (TextView) findViewById(R.id.tvPricePerUnit);
        tvInventory = (TextView) findViewById(R.id.tvInventory);
        tvProductDescription = (TextView) findViewById(R.id.tvProductDescription);

        options = new RequestOptions().centerCrop().placeholder(R.drawable.bombill).error(R.drawable.bombill);


        Glide.with(this).load(strProductPhoto).into(ivProductPhoto);
        tvProductName.setText(strProductName);
        tvPricePerUnit.setText("₹"+strPrice+"/"+strProductUnitDescription+strProductUnit);
        tvInventory.setText("Only "+strInventory+" left in stock");
        tvProductDescription.setText(strProductDescription);
    }
}
