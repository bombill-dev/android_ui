package com.app.bombill.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.bombill.Fragments.Your_order.OrderAdapter;
import com.app.bombill.Fragments.Your_order.OrderModel;
import com.app.bombill.Fragments.Your_order.Your_Order_List;
import com.app.bombill.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Your_Order extends Fragment {

    private static final String TAG = "Your_Order";
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SharedPreferences sharedPreferences;

    //create a Order list to display list in recyclerView
    List<Your_Order_List> developersLists;
    String URL_DATA,URL_DATA1;
    String customer_id;
//    FloatingActionButton WhatsappBtn;
    ImageButton WhatsappBtn;

    //Status

    TextView Status;
    ProgressBar progressBar;


    public Your_Order() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_order, container, false);
        sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
         customer_id = sharedPreferences.getString("customer_id", null);

//        URL_DATA = "http://nirvaacls.com/bombill_pages/customer_get_orders.php?customer_id="+customer_id;
        URL_DATA = "https://new.bombill.com/bombill_pages/customer_get_orders.php?customer_id="+customer_id;




        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_order);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Status=view.findViewById(R.id.status);
        progressBar=view.findViewById(R.id.progressbar);
        progressBar.setProgress(100);*/


        developersLists = new ArrayList<>();

   WhatsappBtn=view.findViewById(R.id.fab_whatsapp);
        final String num = "+918928376263";
       // final String num = "+918928376263";
        final String text = "Hello";
   WhatsappBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           boolean installed = isAppInstalled("com.whatsapp");

           if (installed)
           {
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+num+"&text="+ text));
               startActivity(intent);
           }
           else
           {
               Toast.makeText(getContext(), "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
           }
       }
   });


        loadUrlData();





        return view;
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getContext().getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }



    private void loadUrlData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //String json;
                    //      JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = new JSONArray(response);

                    try {
//                        Log.d(TAG, response);
                        ObjectMapper mapper = new ObjectMapper();
                        List<OrderModel> orderModel = mapper.readValue(response, new TypeReference<List<OrderModel>>() {});
                        for (int i = 0; i < orderModel.size(); i++){
                            Log.d(TAG, orderModel.get(i).getVendor_order_id());

                            //Status.setText(orderModel.get(i).getOrder_status());
                        }
                        adapter = new OrderAdapter(orderModel);
                        recyclerView.setAdapter(adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "No Internet Connection" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



}
