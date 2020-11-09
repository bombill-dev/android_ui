package com.app.bombill.Adapters;

/**
 * Created by amolmhatre on 9/23/20
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.bombill.Activities.Home_Activity;
import com.app.bombill.Activities.Vendor_Product;
import com.app.bombill.Activities.Vendor_Product_Detail;
import com.app.bombill.R;
import com.app.bombill.model.VendorProductResponse;
import com.app.bombill.util.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

public class VendorProductAdapter extends RecyclerView.Adapter<VendorProductAdapter.ViewHolder> {
    private static final String TAG = "VendorProductAdapter";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static DatabaseHelper databaseHelper;
    private static Cursor cursor;
    private static String vendor_id = "0", vendor_id_cart ="0";
    private static List<VendorProductResponse> vendorProductResponseList;
    private static Context context;
    private static RequestOptions options;
    private static Snackbar snackbar;
    private static Snackbar.SnackbarLayout snackbarLayout;
    private static LayoutInflater inflater;

    public VendorProductAdapter(List<VendorProductResponse> vendorProductResponseList, Context context) {
//        Log.d(TAG,"VendorProductAdapter");
        this.vendorProductResponseList = vendorProductResponseList;
        this.context = context;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.bombill).error(R.drawable.bombill);
        sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id","0");
        databaseHelper = new DatabaseHelper(context);
        vendor_id_cart = databaseHelper.getVendorId();
        Log.d(TAG, "vendor_id:" + vendor_id + " , " + vendor_id_cart);
    }

    @NonNull
    @Override
    public VendorProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VendorProductAdapter.ViewHolder holder, final int position) {
//        Log.d(TAG,"onBindViewHolder");
        final VendorProductResponse vendorProductResponse = vendorProductResponseList.get(position);
        holder.tvProductName.setText(vendorProductResponse.getName());
        holder.tvProductPrice.setText("₹"+vendorProductResponse.getPrice()
                +" per "+vendorProductResponse.getUnitDesc()
                +" "+vendorProductResponse.getUnit());
        holder.tvItemQuantity.setText("0");
        holder.tvProductCategory.setText(
                getProductCategory(vendorProductResponse.getVendorCategoryId()));

        /** assigning veg/non-veg label dynamically **/
        if (!vendorProductResponse.getType().equals("non-veg")){
            holder.ivProductType.setImageResource(R.drawable.veg);
        }

        /** Setting product image and its click event for product details screen **/
        Glide.with(context).load(vendorProductResponse.getUrl()).apply(options)
                .into(holder.ivProductPhoto);
      /*  holder.ivProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VendorProductResponse e1 = vendorProductResponseList.get(position);
                Intent intent = new Intent(view.getContext(), Vendor_Product_Detail.class);
                intent.putExtra("product_id", vendorProductResponse.getVendorProductId());
                intent.putExtra("product_photo", vendorProductResponse.getUrl());
                intent.putExtra("product_name", vendorProductResponse.getName());
                intent.putExtra("product_price", vendorProductResponse.getPrice());
                intent.putExtra("product_type", vendorProductResponse.getType());
                intent.putExtra("product_unit", vendorProductResponse.getUnit());
                intent.putExtra("product_unitDescription", vendorProductResponse.getUnitDesc());
                intent.putExtra("product_category", vendorProductResponse.getVendorCategoryId());
                intent.putExtra("product_availability", vendorProductResponse.getProductAvailability());
                intent.putExtra("product_description", vendorProductResponse.getDescription());
                view.getContext().startActivity(intent);
            }
        });*/

        /** Check if product_id exists and show/hide ADD button accordingly */
        if (databaseHelper.findProductID(vendorProductResponse.getVendorProductId())) {
            holder.buttonSet.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.tvItemQuantity.setText(databaseHelper.getQuantity(vendorProductResponse.getVendorProductId()));
        } else {
            holder.buttonSet.setVisibility(View.GONE);
            holder.btnAddToCart.setVisibility(View.VISIBLE);
        }

        /** Add button **/
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_id_cart = databaseHelper.getVendorId();
                if (vendor_id_cart.equals(vendor_id) || vendor_id_cart.equals("0")) {
                    //We are assuming no entry for product_id exist
                    boolean isInsterted = insertData(vendor_id,
                            vendorProductResponse.getVendorProductId(),
                            vendorProductResponse.getName(),
                            "1",
                            vendorProductResponse.getPrice());
                    if (isInsterted) {
                        Log.d(TAG,"Data is Inserted");
                        holder.tvItemQuantity.setText("1");
                        openSnackbar(v);
                        holder.btnAddToCart.setVisibility(View.GONE);
                        holder.buttonSet.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG,"Insertion Failed");
                    }
                } else {
                    /** Ideally a alert box should open and ask user if he wish to empty the cart */
//                    switchVendorAlert(vendor_id_cart, vendor_id);
                    Toast.makeText(context,"Changing vendor clears cart",Toast.LENGTH_SHORT).show();
                    databaseHelper.deleteTable(vendor_id_cart);
                }
            }
        });

        /** + button **/
        holder.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if cart count is already equal to inventory
                String strCartQuantity = databaseHelper.getQuantity(vendorProductResponse.getVendorProductId());
                int intCartQuantity = Integer.parseInt(strCartQuantity);
                if (Integer.parseInt(vendorProductResponse.getProductAvailability())==intCartQuantity){
                    Toast.makeText(context, "Cannot add more than available",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //We are assuming at lease one entry for product_id exists
                    holder.tvItemQuantity.setText(databaseHelper.addItem(vendorProductResponse.getVendorProductId()));
                    openSnackbar(v);
                }
            }
        });

        /** - button **/
        holder.btnSubtractItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we are assuming at least 1 entry exists
                String result = databaseHelper.subtractItem(vendorProductResponse.getVendorProductId());
                if (Integer.parseInt(result) > 0) {
                    openSnackbar(v);
                    holder.tvItemQuantity.setText(result);
                } else {
                    holder.buttonSet.setVisibility(View.GONE);
                    holder.btnAddToCart.setVisibility(View.VISIBLE);
                    databaseHelper.deleteData(vendorProductResponse.getVendorProductId());
                }
            }
        });
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return vendorProductResponseList.size();
    }

    /** Converts productCategoryId into readable format **/
    private String getProductCategory(String productCategoryId){
        switch(productCategoryId){
            case "0":
                return "Seawater Fish";
            case "1":
                return "Freshwater Fish";
            case "2":
                return "Dry Fish";
            case "3":
                return "Cuisine";
            case "4":
                return "Spices";
            case "5":
                return "Poultry";
            case "6":
                return "Meat";
            case "7":
                return "Condiments";
            case "8":
                return "Dessert";
            case "9":
                return "Merchandise";
            default:
                return "Bombill Product";
        }
    }//getProductCategory

    /** ################ Snackbar ################# **/
    public void openSnackbar(View view) {
        String items = databaseHelper.getNumberOfItems();
        String price = databaseHelper.getTotalPrice();

        String vendorName = sharedPreferences.getString("vendor_name", null);
        snackbar = Snackbar.make(view, items+ " item | ₹" + price + "\nFrom: " + vendorName, Snackbar.LENGTH_INDEFINITE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view2 = inflater.inflate(R.layout.bottom_snackbar, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        View sbView = snackbar.getView();
        snackbarLayout.addView(view2, 0);
        snackbar.show();
        sbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, Home_Activity.class);
                //   Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show();
                editor.putInt("home_fragment", 1).commit();
                // intent.putExtra("screenNumber",2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }//openSnackbar

    /** ################ Database operation functions ################## **/
    public boolean insertData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price ) {
        return databaseHelper.insertData(vendor_id, product_id, product_name, product_quantity, product_price);
    }

    public boolean updateData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price ) {
        return databaseHelper.updateData(vendor_id, product_id, product_name, product_quantity, product_price);
    }

    public boolean deleteData(String product_id) {
        return databaseHelper.deleteData(product_id);
    }

    public boolean clearTable(String vendor_id) {
        return databaseHelper.deleteTable(vendor_id);
    }

    public void switchVendorAlert(String Vendor1, String Vendor2) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setIcon(R.drawable.bombill)//.setCancelable(true);
                .setTitle("Do you want to empty the cart?")
                .setMessage("Your cart contain products from vendor#" + Vendor1 +
                        ". Do you want to discard the selection and add products from vendor#" + Vendor2 + "?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.deleteTable(vendor_id_cart);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    /** ################ ViewHolder Class ################## **/
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProductPhoto,ivProductType;
        TextView tvProductName,tvProductCategory, tvProductPrice, tvItemQuantity;
        Button btnAddToCart, btnSubtractItem, btnAddItem;
        RelativeLayout buttonSet;
        CardView cardVendorProduct;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
//            Log.d(TAG,"ViewHolder");
            ivProductPhoto = itemView.findViewById(R.id.ivProductPhoto);
            ivProductType = itemView.findViewById(R.id.ivProductType);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnSubtractItem = itemView.findViewById(R.id.btnSubtractItem);
            btnAddItem = itemView.findViewById(R.id.btnAddItem);
            buttonSet = itemView.findViewById(R.id.buttonSet);
            cardVendorProduct = itemView.findViewById(R.id.cardVendorProduct);
        }
    }
}
