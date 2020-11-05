package com.app.bombill.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.bombill.Fragments.Favourite_Page.Favourite_Adapter;
import com.app.bombill.Fragments.Favourite_Page.Favourite_List;
import com.app.bombill.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite_Fragment extends Fragment {

    //Declartion of recycler view

    RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    //create a Favourite list to display list in recyclerView
    List<Favourite_List> developersLists;
    String URL_DATA;

    public Favourite_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        String customerId="11";

      //This API Url is Get Customer Favourite from database to display in  Navigation favourite_Fragment

//        URL_DATA = "http://nirvaacls.com/bombill_pages/customer_get_favourite.php?customer_id="+customerId;
        URL_DATA = "https://bombill.com/bombill_pages/customer_get_favourite.php?customer_id="+customerId;
        recyclerView = (RecyclerView) view.findViewById(R.id.favourites_recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        developersLists = new ArrayList<>();
        loadUrlData();


        return view;
    }

    private void loadUrlData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    //String json;
                    //      JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);

                        //Get Jason Data From URL and Put in Favourite List

                        Favourite_List developers = new Favourite_List(jo.getString("vendor_product_id"), jo.getString("name")
                                , jo.getString("price"), jo.getString("unit"),jo.getString("url"),jo.getString("description"));
                        developersLists.add(developers);
                    }

                    adapter = new Favourite_Adapter(developersLists, getActivity().getApplicationContext());
                    recyclerView.setAdapter(adapter);

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