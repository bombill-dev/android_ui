package com.app.bombill.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.bombill.Adapters.Order_Details_Adapter;
import com.app.bombill.Adapters.VendorProductAdapter;
import com.app.bombill.model.OrderDetailResponse;
import com.app.bombill.model.VendorProductResponse;
import com.app.bombill.network.RetrofitClient;
import com.app.bombill.Fragments.Your_order.OrderAdapter;
import com.app.bombill.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Order_Details extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "Order_Details";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final int PERMISSION_REQUEST = 100;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OrderDetailResponse> developersLists;
    String Order_id, order_status, order_date, sub_total, grand_total, fileURL,URL,status;
    TextView Order_Id, Order_Status, Order_date, Total, Service_Charges, Delivery_Charges, Grand_Total,Status;
    Button btnInvoice;

    LottieAnimationView lottieAnimationView,lottieAnimationView1,lottieAnimationView2,lottieAnimationView3;
  public   ProgressBar progressBar;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("home_fragment",2).commit();

        Status=findViewById(R.id.status1);
        progressBar=findViewById(R.id.progressBar);
lottieAnimationView=findViewById(R.id.lottie);
lottieAnimationView1=findViewById(R.id.lottie1);
lottieAnimationView2=findViewById(R.id.lottie2);
lottieAnimationView3=findViewById(R.id.lottie3);


        if (ContextCompat.checkSelfPermission(Order_Details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Order_Details.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        }

        Intent intent = getIntent();
        Order_id = intent.getStringExtra(OrderAdapter.KEY_ORDER_ID);

        URL="https://bombill.com/bombill_pages/customer_get_order_status.php?order_id="+Order_id;

        loadurl();

        order_status = intent.getStringExtra(OrderAdapter.KEY_ORDERSTATUS);
        order_date = intent.getStringExtra(OrderAdapter.KEY_PURCHES_DATE);
        grand_total = intent.getStringExtra(OrderAdapter.KEY_GRANDTOTAL);
        recyclerView = (RecyclerView) findViewById(R.id.orders_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Order_Id = findViewById(R.id.order_id);
        Order_Status = findViewById(R.id.order_status);
        Order_date = findViewById(R.id.order_date);
        Total = findViewById(R.id.total);
        Service_Charges = findViewById(R.id.tvServiceCharge);
        Delivery_Charges = findViewById(R.id.tvDeliveryCharge);
        Grand_Total = findViewById(R.id.tvGrandTotal);
        btnInvoice = findViewById(R.id.btnInvoice);


        Order_Id.setText(Order_id);
        if (order_status.equals("Completed")||order_status.equals("Delivered")){
            Order_Status.setTextColor(Color.parseColor("#228B22"));
        } else {
            Order_Status.setTextColor(Color.parseColor("#df4344"));
        }
        Order_Status.setText(order_status);

        float floatintGrandTotal =  Float.parseFloat(grand_total);
        int intsubTotal=0,
                intServiceCharge = 0,
                intDeliveryCharge = 0,
                intGrandTotal = (int) floatintGrandTotal;

        if (intGrandTotal<550){
            intDeliveryCharge=30;
        } else {
            intDeliveryCharge =0;
        }
        intsubTotal = intGrandTotal - (intDeliveryCharge+intServiceCharge);

        Order_date.setText(order_date);
        Total.setText("Total                                ₹ "+intsubTotal);
        Service_Charges.setText("Service Charge              ₹ "+intServiceCharge);
        Delivery_Charges.setText("Delivery Charge             ₹ "+intDeliveryCharge);
        Grand_Total.setText("Grand Total                     ₹ " + grand_total);
        developersLists = new ArrayList<>();
        getorder();
       fileURL = "https://bombill.com/invoice.php?id="+Order_id;
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**download pdf from link below*/
//                https://new.bombill.com/invoice.php?id=AP-1600065468
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileURL));
                startActivity(browserIntent);
            }
        });
    }

    private void loadurl() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);

                      status=jo.getString("order_status");
                      Status.setText(status);


                        if (status.equals("Completed")) {
                            progressBar.setProgress(100);
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.blue), PorterDuff.Mode.SRC_IN );
                            lottieAnimationView3.setVisibility(View.VISIBLE);

                        }

                        if (status.equals("Order Placed")) {
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.blue), PorterDuff.Mode.SRC_IN );
                            progressBar.setProgress(15);
                        }

                        if (status.equals("Vendor Declined")){
                             progressBar.setProgress(100);
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.red), PorterDuff.Mode.SRC_IN );
                            lottieAnimationView2.setVisibility(View.VISIBLE);
                        }

                        if (status.equals("Ready For Pickup")) {
                            progressBar.setProgress(45);
                        }

                        if (status.equals("Vendor Accepted")) {
                            progressBar.setProgress(45);
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.blue), PorterDuff.Mode.SRC_IN );

                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }

                        if (status.equals("In Transit")) {
                            progressBar.setProgress(75);
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.blue), PorterDuff.Mode.SRC_IN );

                            lottieAnimationView1.setVisibility(View.VISIBLE);
                        }

                        if (status.equals("Deliverd")) {
                            progressBar.setProgress(90);
                            progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(Order_Details.this, R.color.blue), PorterDuff.Mode.SRC_IN );

                            lottieAnimationView3.setVisibility(View.VISIBLE);

                        }

                       // SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        //editor.putString("order_status", status);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

     //   String news = sharedPreferences.getString("order_status", null);
       // Status.setText(news);

        /*SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("order_status", null);
        Status.setText(name);*/

    }
    private void getorder() {
        Call<List<OrderDetailResponse>> call = RetrofitClient.getInstance().getApi().customer_get_orders_details(Order_id);
        call.enqueue(new Callback<List<OrderDetailResponse>>() {
            @Override
            public void onResponse(Call<List<OrderDetailResponse>> call, retrofit2.Response<List<OrderDetailResponse>> response) {
                recyclerView.setAdapter(new Order_Details_Adapter(response.body(),getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<OrderDetailResponse>> call, Throwable t) {
                Toast.makeText(Order_Details.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}