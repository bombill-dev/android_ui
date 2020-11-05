package com.app.bombill.Activities;

/**
 * Created by amolmhatre on 9/22/20
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.bombill.PayUMoney.PayUMoneyActivity;
import com.app.bombill.R;
import com.app.bombill.model.OrderResponse;
import com.app.bombill.network.RetrofitClient;
import com.app.bombill.util.DatabaseHelper;

import retrofit2.Call;
import retrofit2.Callback;

public class Check_Out_Order extends AppCompatActivity {
    private static final String TAG = "Check_Out_Order";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static DatabaseHelper databaseHelper;

    private static String vendor_id, customer_id,
            strTotal, strServiceCharge, strDeliveryCharge, strGrandTotal,   //Charges
            product_price, product_name, product_id, product_quantity,product_total,  //Product details
            payment_method, transaction_id, strSpecialInstruction,        //Extras
            customer_lat_lon, customer_email, customer_name, customer_address, customer_phone;  //Customer Details

    private Button btnCOD, btnPUM;
    private EditText etDeliveryAddress, etSpecialInstruction;
    private TextView tvTotal, tvServiceCharge, tvDeliveryCharge, tvGrandTotal;

    String delivery_Address;
    String g1, g2;
    String g_Total;
    int i = 5;
    CountDownTimer CDT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_order);
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(this);

        /** Initialize UI elements */
        tvTotal = findViewById(R.id.tvTotal);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvServiceCharge = findViewById(R.id.tvServiceCharge);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress);
        etSpecialInstruction = findViewById(R.id.etSpecialInstruction);
        btnCOD = findViewById(R.id.btnCOD);
        btnPUM = findViewById(R.id.btnPUM);

        /** Gathering data and filling in parameter strings */
        strTotal = databaseHelper.getTotalPrice();
        int order_amount = Integer.parseInt(strTotal);
        int int_delivery_charge = 0, int_service_charge = 0;
        if (order_amount < 500) {
            int_delivery_charge = 30;
        } else if (order_amount > 500) {
            int_delivery_charge = 0;
        }
        strDeliveryCharge = String.valueOf(int_delivery_charge);
        strServiceCharge = String.valueOf(int_service_charge);
//        strGrandTotal = String.valueOf(order_amount + int_delivery_charge);
        strGrandTotal = databaseHelper.getTotalPrice();

        product_name = productDetails(2);
        product_id = productDetails(1);
        product_quantity = productDetails(3);
        product_price = productDetails(4);
        product_total = productDetails(5);

        transaction_id = "0";
//        vendor_id = sharedPreferences.getString("vendor_id", null);
        vendor_id = databaseHelper.getVendorId();
        customer_id = sharedPreferences.getString("customer_id", null);
        customer_address = sharedPreferences.getString("customer_address", null);
        customer_phone = sharedPreferences.getString("customer_phone", null);
        customer_lat_lon = sharedPreferences.getString("gpsLocation", null);
        customer_email = sharedPreferences.getString("customer_email", null);
        customer_name = sharedPreferences.getString("customer_name", null);

        /** assigning values to UI elements*/
        tvTotal.setText("₹ " + strTotal);
        tvDeliveryCharge.setText("₹ " + strDeliveryCharge);
        tvServiceCharge.setText("₹ " + strServiceCharge);
        tvGrandTotal.setText("₹ " + (Integer.parseInt(strGrandTotal)
                +Integer.parseInt(strDeliveryCharge)
                +Integer.parseInt(strServiceCharge)));
        etDeliveryAddress.setText(customer_address);

        btnCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method = "cod";
                createOrder();
                //    SendToDatabaseOrder();
            }
        });

        btnPUM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "PayUMoney option is coming soon !!!", Toast.LENGTH_LONG).show();
//                payment_method = "payumoney";
//                createOrder();
            }
        });
    }

    private String productDetails(int columnIndex){
        /** Extract product data from database */
        Cursor cursor = databaseHelper.getData();
        /**cursor is just pointer to read table*/
        /**cursor.getString(0) denotes zeroth column*/
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            stringBuffer.append(cursor.getString(columnIndex)+",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        Log.d(TAG,stringBuffer.toString());
        return stringBuffer.toString();
    }

    private void createOrder() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Placing order");
        progressDialog.setMessage("Wait while order is being placed");
        progressDialog.setIcon(R.drawable.bombill);
        progressDialog.setProgress(i);
        progressDialog.show();
//        CDT = new CountDownTimer(5000,1000) {
//            int i = 0;
//            @Override
//            public void onTick(long milliSecondUntilFinished) {
//                i++;
//                progressDialog.setProgress((int) i * 1000 / (5000/1000));
//            }
//            @Override
//            public void onFinish() {
//                progressDialog.setProgress(100);
//            }
//        }.start();
        /** Get updated values form text area*/
        strSpecialInstruction = etSpecialInstruction.getText().toString();
        customer_address = etDeliveryAddress.getText().toString();
        /** Progress dialogue */

        Log.d(TAG, "Order Details\n" +
                product_id + "\n" +
                product_quantity + "\n" +
                product_price + "\n" +
                product_total + "\n" +
                strServiceCharge + "\n" +
                strDeliveryCharge + "\n" +
                strGrandTotal);
        Call<OrderResponse> call = RetrofitClient.getInstance().getApi().customer_create_order(
                vendor_id, customer_id, product_name, product_id, product_quantity,
                product_price, product_total, strServiceCharge, strDeliveryCharge, strGrandTotal,
                payment_method, transaction_id, customer_address, customer_phone,
                customer_lat_lon, customer_email, customer_name, strSpecialInstruction);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, retrofit2.Response<OrderResponse> response) {
                Log.d(TAG, "Order Created, ID: " + response.body().getVendorOrderId());
                if (payment_method.equals("cod")) {
                    Log.d(TAG, response.body().toString());
                    ShowDialogBox();
                    /** This case Empty All Items from database */
                    Log.d(TAG, "Vendor ID: " +vendor_id);
                    databaseHelper.deleteTable(vendor_id);
                    editor.putInt("home_fragment", 2).commit();
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Order Placed:" + response.body().getVendorOrderId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Check_Out_Order.this, Home_Activity.class));
                    finish();
                } else {
                    editor.putString("txnid", response.body().getTxnid());
                    editor.putString("vendor_order_id", response.body().getVendorOrderId());
                    editor.putString("hash", response.body().getHash());
                    editor.putString("paymentAmount", strGrandTotal);
                    editor.putString("paymentTitle", "Bombill Payment");
                    editor.putString("paymentStatus", "Thank you for business with Bombill");
                    editor.apply();
                    progressDialog.dismiss();
                    startActivity(new Intent(Check_Out_Order.this, PayUMoneyActivity.class));
                    finish();
                }
            }
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d(TAG, "Placing Order Failed !!");
            }
        });
    }

    private void ShowDialogBox() {
        new AlertDialog.Builder(Check_Out_Order.this)
                .setTitle("Thanks for ordering on Bombill")
                .setMessage("Items :"+ product_name +","+"\nQuantity : "+ product_quantity +","+"\nGrand total :₹"+strGrandTotal)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do your task
                        startActivity(new Intent(Check_Out_Order.this, Home_Activity.class));
                        finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do your task
                        startActivity(new Intent(Check_Out_Order.this, Home_Activity.class));
                        finish();
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.bombill)
                .show();
    }
}