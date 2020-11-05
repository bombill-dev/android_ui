package com.app.bombill.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.bombill.Adapters.VendorProductAdapter;
import com.app.bombill.model.VendorProductResponse;
import com.app.bombill.model.Vendor_Product_list;
import com.app.bombill.network.RetrofitClient;
import com.app.bombill.Adapters.HomePageVendorAdapter;
import com.app.bombill.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vendor_Product extends AppCompatActivity {

    private static final String TAG = "Vendor_Product";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static RecyclerView
            recyclerSeawaterFish, recyclerFreshwaterFish,
            recyclerDryFish, recyclerCuisine,
            recyclerSpices, recyclerPoultry,
            recyclerMeat, recyclerCondiments,
            recyclerDessert, recyclerMerchandise;

    private static List<VendorProductResponse>
            listSeawaterFish,listFreshwaterFish,
            listDryFish, listCuisine,
            listSpices, listPoultry,
            listMeat, listCondiments,
            listDessert, listMerchandise;

    private RecyclerView.Adapter
            adapterSeawaterFish, adapterFreshwaterFish,
            adapterDryFish, adapterCuisine,
            adapterSpices, adapterPoultry,
            adapterMeat, adapterCondiments,
            adapterDessert, adapterMerchandise, adapter;

    private RelativeLayout
            relativeSeawaterFish, relativeFreshwaterFish,
            relativeDryFish, relativeCuisine,
            relativeSpices, relativePoultry,
            relativeMeat, relativeCondiments,
            relativeDessert, relativeMerchandise;

    private Toolbar toolbar;
    private TextView tvVendorName;
    ImageButton Cartbtn;
    MenuItem mCartMenuItem;

    String vendor_id;

    String URL0,URL1,URL2,URL3,URL4,URL5,URL6,URL7,URL8,URL9;
    int Category;

    //Restrorant info
    TextView Restroant_name,Deliver_distance,Minmum_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_product);

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Restroant_name=findViewById(R.id.restroant_name);
        Deliver_distance=findViewById(R.id.delivery_distance);
        Minmum_amount=findViewById(R.id.min_amount);



        String restroant_name = sharedPreferences.getString("vendor_name", null);
        String delivery_distance = sharedPreferences.getString("delivery_distance", null);
        String time = sharedPreferences.getString("time", null);
        String min_amount = sharedPreferences.getString("min_amount", null);

        Restroant_name.setText(restroant_name);
        Deliver_distance.setText(" "+time+"mins"+" | "+"Minimum Order ₹"+min_amount);
       // Minmum_amount.setText("Minimum Order ₹."+min_amount);


        /** Relative Layout **/
        relativeSeawaterFish = (RelativeLayout) findViewById(R.id.relativeSeawaterFish);
        relativeFreshwaterFish = (RelativeLayout) findViewById(R.id.relativeFreshwaterFish);
        relativeDryFish = (RelativeLayout) findViewById(R.id.relativeDryFish);
        relativeCuisine = relativeSeawaterFish = (RelativeLayout) findViewById(R.id.relativeCuisine);
        relativeSpices = (RelativeLayout) findViewById(R.id.relativeSpices);
        relativePoultry = (RelativeLayout) findViewById(R.id.relativePoultry);
        relativeMeat = (RelativeLayout) findViewById(R.id.relativeMeat);
        relativeCondiments = (RelativeLayout) findViewById(R.id.relativeCondiments);
        relativeDessert = (RelativeLayout) findViewById(R.id.relativeDessert);
        relativeMerchandise = (RelativeLayout) findViewById(R.id.relativeMerchandise);

        //RecyclerSeatwaterFish
        recyclerSeawaterFish = (RecyclerView) findViewById(R.id.recyclerSeawaterFish);
        recyclerSeawaterFish.setHasFixedSize(true);
        recyclerSeawaterFish.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewFreshwaterFish
        recyclerFreshwaterFish = (RecyclerView) findViewById(R.id.recyclerFreshwaterFish);
        recyclerFreshwaterFish.setHasFixedSize(true);
        recyclerFreshwaterFish.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewDryFish
        recyclerDryFish = (RecyclerView) findViewById(R.id.recyclerDryFish);
        recyclerDryFish.setHasFixedSize(true);
        recyclerDryFish.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewCuisine
        recyclerCuisine = (RecyclerView) findViewById(R.id.recyclerCuisine);
        recyclerCuisine.setHasFixedSize(true);
        recyclerCuisine.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewSpices
        recyclerSpices = (RecyclerView) findViewById(R.id.recyclerSpices);
        recyclerSpices.setHasFixedSize(true);
        recyclerSpices.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewPoultry
        recyclerPoultry = (RecyclerView) findViewById(R.id.recyclerPoultry);
        recyclerPoultry.setHasFixedSize(true);
        recyclerPoultry.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewMeat
        recyclerMeat = (RecyclerView) findViewById(R.id.recyclerMeat);
        recyclerMeat.setHasFixedSize(true);
        recyclerMeat.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewCondiments
        recyclerCondiments = (RecyclerView) findViewById(R.id.recyclerCondiments);
        recyclerCondiments.setHasFixedSize(true);
        recyclerCondiments.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewDessert
        recyclerDessert = (RecyclerView) findViewById(R.id.recyclerDessert);
        recyclerDessert.setHasFixedSize(true);
        recyclerDessert.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerviewMerchandise
        recyclerMerchandise = (RecyclerView) findViewById(R.id.recyclerMerchandise);
        recyclerMerchandise.setHasFixedSize(true);
        recyclerMerchandise.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        tvVendorName = findViewById(R.id.tvVendorName);
      //  tvVendorName.setText(sharedPreferences.getString("vendor_name", null));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String vendor_id = intent.getStringExtra(HomePageVendorAdapter.KEY_VENDORID);

        listSeawaterFish = new ArrayList<>();
        listFreshwaterFish = new ArrayList<>();
        listDryFish = new ArrayList<>();
        listCuisine = new ArrayList<>();
        listSpices = new ArrayList<>();
        listPoultry = new ArrayList<>();
        listMeat = new ArrayList<>();
        listCondiments = new ArrayList<>();
        listDessert = new ArrayList<>();
        listMerchandise = new ArrayList<>();

        vendor_id = sharedPreferences.getString("vendor_id", null);

        //For Category 0
        URL0="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+0;

        // For Category 1
        URL1="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+1;

        // For Category 2
        URL2="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+2;

        // For Category 3
        URL3="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+3;

        // For Category 4
        URL4="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+4;

        // For Category 5
        URL5="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+5;

        // For Category 6
        URL6="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+6;

        // For Category 7
        URL7="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+7;

        // For Category 8
        URL8="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+8;

        // For Category 9
        URL9="https://bombill.com/bombill_pages/customer_get_vendor_product1.php?vendor_id="+vendor_id + "&c_id="+9;

//        vendorProductApiCall();
        Handler handlerSeawater = new Handler();
        handlerSeawater.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallSeawater();
            }
        },0000);

        Handler handlerFreshwater = new Handler();
        handlerFreshwater.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallFreshwater();
            }
        },300);

        Handler handlerDryFish = new Handler();
        handlerDryFish.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallDryFish();
            }
        },600);

        Handler handlerCuisine = new Handler();
        handlerCuisine.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallCuisine();
            }
        },1200);

        Handler handlerSpices = new Handler();
        handlerSpices.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallSpices();
            }
        },1500);

        Handler handlerPoultry = new Handler();
        handlerPoultry.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallPoultry();
            }
        },1800);

        Handler handlerMeat = new Handler();
        handlerMeat.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallMeat();
            }
        },2100);

        Handler handlerCondiments = new Handler();
        handlerCondiments.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallCondiments();
            }
        },2400);

        Handler handlerDessert = new Handler();
        handlerDessert.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallDessert();
            }
        },2700);

        Handler handlerMerchandise = new Handler();
        handlerMerchandise.postDelayed(new Runnable() {
            @Override
            public void run() {
                vendorProductApiCallMerchandise();
            }
        },3000);
    }



    public void vendorProductApiCallSeawater() {
        /** Seawater Fish **/
        Log.d(TAG, "Seawater Fish");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL0, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeSeawaterFish.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listSeawaterFish.add(vendorProductResponse);
                        adapterSeawaterFish = new VendorProductAdapter(listSeawaterFish, getApplicationContext());
                        recyclerSeawaterFish.setAdapter(adapterSeawaterFish);
                        adapterSeawaterFish.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void vendorProductApiCallFreshwater() {
        /** Freshwater Fish **/
        Log.d(TAG, "Freshwater Fish");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL1, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeFreshwaterFish.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listFreshwaterFish.add(vendorProductResponse);
                        adapterFreshwaterFish = new VendorProductAdapter(listFreshwaterFish, getApplicationContext());
                        recyclerFreshwaterFish.setAdapter(adapterFreshwaterFish);
                        adapterFreshwaterFish.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallDryFish() {
        /** Dry Fish **/
        Log.d(TAG, "Dry Fish");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL2, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeDryFish.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listDryFish.add(vendorProductResponse);
                        adapterDryFish = new VendorProductAdapter(listDryFish, getApplicationContext());
                        recyclerDryFish.setAdapter(adapterDryFish);
                        adapterDryFish.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallCuisine() {
        /** Cuisine **/
        Log.d(TAG, "Cuisine");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL3, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeCuisine.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listCuisine.add(vendorProductResponse);
                        adapterCuisine = new VendorProductAdapter(listCuisine, getApplicationContext());
                        recyclerCuisine.setAdapter(adapterCuisine);
                        adapterCuisine.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallSpices() {
        /** Spices **/
        Log.d(TAG, "Spices");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL4, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeSpices.setVisibility(View.GONE);

                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listSpices.add(vendorProductResponse);
                        adapterSpices = new VendorProductAdapter(listSpices, getApplicationContext());
                        recyclerSpices.setAdapter(adapterSpices);
                        adapterSpices.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallPoultry() {
        /** Poultry **/
        Log.d(TAG, "Poultry");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL5, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativePoultry.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listPoultry.add(vendorProductResponse);
                        adapterPoultry = new VendorProductAdapter(listPoultry, getApplicationContext());
                        recyclerPoultry.setAdapter(adapterPoultry);
                        adapterPoultry.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallMeat() {
        /** Meat **/
        Log.d(TAG, "Meat");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL6, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeMeat.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listMeat.add(vendorProductResponse);
                        adapterMeat = new VendorProductAdapter(listMeat, getApplicationContext());
                        recyclerMeat.setAdapter(adapterMeat);
                        adapterMeat.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallCondiments() {
        /** Condiments **/
        Log.d(TAG, "Condiments");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL7, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeCondiments.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listCondiments.add(vendorProductResponse);
                        adapterCondiments = new VendorProductAdapter(listCondiments, getApplicationContext());
                        recyclerCondiments.setAdapter(adapterCondiments);
                        adapterCondiments.notifyDataSetChanged();
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
    }

    public void vendorProductApiCallDessert() {
        /** Dessert **/
        Log.d(TAG, "Dessert");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL8, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeDessert.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listDessert.add(vendorProductResponse);
                        adapterDessert = new VendorProductAdapter(listDessert, getApplicationContext());
                        recyclerDessert.setAdapter(adapterDessert);
                        adapterDessert.notifyDataSetChanged();
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
    }
    public void vendorProductApiCallMerchandise() {
        /** Merchandise **/
        Log.d(TAG, "Merchandise");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL9, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length()==0){
                        relativeMerchandise.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        VendorProductResponse vendorProductResponse = new VendorProductResponse(jo.getString("vendor_product_id")
                                , jo.getString("name"), jo.getString("price"), jo.getString("unit"),
                                jo.getString("url"), jo.getString("Vendor_category_id"), jo.getString("type"), jo.getString("unit_desc")
                                , jo.getString("description"), jo.getString("product_availability"));
                        listMerchandise.add(vendorProductResponse);
                        adapterMerchandise = new VendorProductAdapter(listMerchandise, getApplicationContext());
                        recyclerMerchandise.setAdapter(adapterMerchandise);
                        adapterMerchandise.notifyDataSetChanged();
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
    }


    public void vendorProductApiCall() {
        Log.d(TAG, "vendorProductApiCall");
        String vendor_id = sharedPreferences.getString("vendor_id", null);
        //Login call
        Call<List<VendorProductResponse>> call = RetrofitClient.getInstance().getApi()
                .customer_get_vendor_product(vendor_id);
        call.enqueue(new Callback<List<VendorProductResponse>>() {
            @Override
            public void onResponse(Call<List<VendorProductResponse>> call, retrofit2.Response<List<VendorProductResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                } else {
                    Log.d(TAG, "Success !!!");
                    for (int i=0; i<response.body().size();i++){
                        switch (response.body().get(i).getVendorCategoryId()){
                            case "0":
                                listSeawaterFish.add(response.body().get(i));
                                break;
                            case "1":
                                listFreshwaterFish.add(response.body().get(i));
                                break;
                            case "2":
                                listDryFish.add(response.body().get(i));
                                break;
                            case "3":
                                listCuisine.add(response.body().get(i));
                                break;
                            case "4":
                                listSpices.add(response.body().get(i));
                                break;
                            case "5":
                                listPoultry.add(response.body().get(i));
                                break;
                            case "6":
                                listMeat.add(response.body().get(i));
                                break;
                            case "7":
                                listCondiments.add(response.body().get(i));
                                break;
                            case "8":
                                listDessert.add(response.body().get(i));
                                break;
                            case "9":
                                listMerchandise.add(response.body().get(i));
                                break;
                        }
                    }

                    adapterSeawaterFish = new VendorProductAdapter(listSeawaterFish, getApplicationContext());
                    adapterFreshwaterFish = new VendorProductAdapter(listFreshwaterFish, getApplicationContext());
                    adapterDryFish = new VendorProductAdapter(listDryFish, getApplicationContext());
                    adapterCuisine = new VendorProductAdapter(listCuisine, getApplicationContext());
                    adapterSpices = new VendorProductAdapter(listSpices, getApplicationContext());
                    adapterPoultry = new VendorProductAdapter(listPoultry, getApplicationContext());
                    adapterMeat = new VendorProductAdapter(listMeat, getApplicationContext());
                    adapterCondiments = new VendorProductAdapter(listCondiments, getApplicationContext());
                    adapterDessert = new VendorProductAdapter(listDessert, getApplicationContext());
                    adapterMerchandise = new VendorProductAdapter(listMerchandise, getApplicationContext());

                    recyclerSeawaterFish.setAdapter(adapterSeawaterFish);
                    recyclerFreshwaterFish.setAdapter(adapterFreshwaterFish);
                    recyclerDryFish.setAdapter(adapterDryFish);
                    recyclerCuisine.setAdapter(adapterCuisine);
                    recyclerSpices.setAdapter(adapterSpices);
                    recyclerPoultry.setAdapter(adapterPoultry);
                    recyclerMeat.setAdapter(adapterMeat);
                    recyclerCondiments.setAdapter(adapterCondiments);
                    recyclerDessert.setAdapter(adapterDessert);
                    recyclerMerchandise.setAdapter(adapterMerchandise);

//                    recyclerCuisine.setNestedScrollingEnabled(false);
//                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<VendorProductResponse>> call, Throwable t) {
                Log.d(TAG, "Failure !!!");
            }
        });
    }

    public void printResponse(VendorProductResponse vendorProductResponse) {
        Log.d(TAG, "\n" + vendorProductResponse.getName() +
                "\n" + vendorProductResponse.getType() +
                "\n" + vendorProductResponse.getVendorCategoryId() +
                "\n" + vendorProductResponse.getDescription() +
                "\n" + vendorProductResponse.getPrice() +
                "\n" + vendorProductResponse.getProductAvailability() +
                "\n" + vendorProductResponse.getUnit() +
                "\n" + vendorProductResponse.getUrl()
        );
    }

    public void killVendorProductActivity() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
