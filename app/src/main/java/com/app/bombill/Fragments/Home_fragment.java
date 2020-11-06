package com.app.bombill.Fragments;

/**
 * Created by amolmhatre on 8/25/20
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.app.bombill.Activities.Fish_Nutriation;
import com.app.bombill.Adapters.HomePageVendorAdapter;
import com.app.bombill.model.HomePageVendorList;
import com.app.bombill.R;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A
 */
public class Home_fragment extends Fragment {

    private static final String TAG = "Home_fragment";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private  static  final String[] FISHNAME = new String[]{
            "Prawns","bombill","bangada","Butter fish","Blowfish","Bombay duck"
            ,"fantya","english masa","khekda","chimbori","shimple","paplet","jitada",
            "kolambi","surmai","katla","paala","vaam"
    };

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Toolbar toolbar1;
    List<HomePageVendorList> developersLists;
    String URL_DATA;
    String Newsearch_lat_lng;
    HomePageVendorAdapter homePageVendorAdapter;
    //  final String BASE_URL ="http://nirvaacls.com/bombill_pages/customer_get_vendors.php?coordinate=" ;

    TextView latshow,Address1,Clear;
  //  EditText Search_Location;
    AutoCompleteTextView Search_Restrorant;
    String strSerach_restro;

    private Toolbar toolbar,toolbar2;
    TextView Address;
    String address="empty address", location="0";
    ImageButton Change_Location,Btn_Search_Restrorant;
    String location_address;
//
BottomSheetDialog bottomSheetDialog;
     EditText Search_Location;
    TextView latshow1,Address12;
    String addresforconfirm;
    //
    ImageView Nutriation_Image;


    //For Silder
    private SliderLayout sliderLayout;

    //Progress bar

    ProgressDialog progressDialog;

    public Home_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar = view.findViewById(R.id.toolbar1);
        toolbar2 = view.findViewById(R.id.toolbar2);
        toolbar1 = view.findViewById(R.id.toolbar1);
        Address = view.findViewById(R.id.address);
        Change_Location=view.findViewById(R.id.btn_changeaddress);
   //     Search_Location=view.findViewById(R.id.Search_location);
        latshow=view.findViewById(R.id.lat);
        Address1=view.findViewById(R.id.address1);
        Clear=view.findViewById(R.id.btn_clear);
        Search_Restrorant=view.findViewById(R.id.search_Restaurant);
        Btn_Search_Restrorant=view.findViewById(R.id.btn_search_restro);
        Nutriation_Image=view.findViewById(R.id.fish_nutri_image);


        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        //SilderLayou

        sliderLayout=view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);

        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2);
        setSliderView();





        Search_Location =bottomSheetDialog.findViewById(R.id.Search_location);
         latshow1  =bottomSheetDialog. findViewById(R.id.lat);
         Address12 =bottomSheetDialog.findViewById(R.id.address1);

        Nutriation_Image.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), Fish_Nutriation.class));
    }
});

        toolbar2.inflateMenu(R.menu.searchrestaro);
        //    Places.initialize(getActivity().getApplicationContext(),"AIzaSyAtGNggJkmiG03uKdnexRFnFhSd6-VJTpU");

        Places.initialize(getActivity().getApplicationContext(),"AIzaSyC6fWXAqzOejOZcBC-01z1svmXPu5p8nBU");


        //Change Location

        Change_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  startActivity(new Intent(getActivity(), Search_Location_Vendor.class));
                /**
                Places.initialize(getContext().getApplicationContext(), "AIzaSyC6fWXAqzOejOZcBC-01z1svmXPu5p8nBU");

                Search_Location.setFocusable(false);
                Search_Location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                                , Place.Field.LAT_LNG, Place.Field.NAME);
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                                , fieldList).build(getContext());

                        //start Activity result

                        startActivityForResult(intent, 100);
                    }
                });

                bottomSheetDialog.show();
                 **/
            }

        });


        toolbar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places.initialize(getContext().getApplicationContext(), "AIzaSyC6fWXAqzOejOZcBC-01z1svmXPu5p8nBU");

                Search_Location.setFocusable(false);
                Search_Location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                                , Place.Field.LAT_LNG, Place.Field.NAME);
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                                , fieldList).build(getContext());

                        //start Activity result

                        startActivityForResult(intent, 100);
                    }
                });

                bottomSheetDialog.show();
            }
        });

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //   btnlocation=view.findViewById(R.id.btn_location);





        //This API Url is Get Customer Venodr from database to display in Home_Vendor_Aadapter

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        developersLists = new ArrayList<>();


        RatingBar rr=(RatingBar) view.findViewById(R.id.ratingBar);
      /*  rr.setRating((float) 2);
        rr.setStepSize((float) 0.3);*/

        Intent intent=getActivity().getIntent();
        String latlong = intent.getStringExtra("gpsLocation");

        //   URL_DATA = BASE_URL+latlong;
        //   Log.d(TAG,URL_DATA);

//        SharedPreferences sharedPreferences1 =getContext().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);


        String newsearch_lat_lng = sharedPreferences.getString("gpsLocation", null);

        //Get Location from SplashScreen cuurent location

        location_address=sharedPreferences.getString("gpsAddress",null);
        Log.d(TAG ,"gpsAddress: "+location_address);
       /* location_address = sharedPreferences.getString("addresscurrent", null);*/
        Address.setText(location_address);

        //   newsearch_lat_lng="19.0167,73.0393";
//        URL_DATA = "http://nirvaacls.com/bombill_pages/customer_get_vendors.php?coordinate=" +newsearch_lat_lng;
        URL_DATA = "https://bombill.com/bombill_pages/customer_get_vendors.php?coordinate=" +newsearch_lat_lng;
        loadUrlData();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,FISHNAME);
        Search_Restrorant.setAdapter(adapter);
        Search_Restrorant.setFocusable(true);


        Search_Restrorant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });

        Search_Restrorant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Btn_Search_Restrorant.setVisibility(View.VISIBLE);
            }
        });
        Newsearch_lat_lng = newsearch_lat_lng;

        //Clear and goto Home_fragment

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),getClass()));
            }
        });

        Btn_Search_Restrorant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Clear.setVisibility(View.VISIBLE);
                strSerach_restro=Search_Restrorant.getText().toString();

                //Clear the List if search again
                developersLists.clear();
                URL_DATA="https://bombill.com/bombill_pages/search_vendor_product.php?key="+strSerach_restro + "&latlon="+Newsearch_lat_lng;

                loadUrlData();
            }
        });
        return view;
    }

    private void setSliderView() {

        for (int i=0; i<=2;i++){

            DefaultSliderView sliderView=new DefaultSliderView(getContext());

            switch (i){

                case 0:
                    sliderView.setImageDrawable(R.drawable.btn_nutrition);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.btn_sustainable);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.bombill);
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            final  int finalI=i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    startActivity(new Intent(getContext(),Fish_Nutriation.class));
                   // Toast.makeText(getContext(), "This is Banner"+(finalI+1), Toast.LENGTH_SHORT).show();
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }

    private void sendMessage() {
        Clear.setVisibility(View.VISIBLE);
        strSerach_restro=Search_Restrorant.getText().toString();

        //Clear the List if search again
        developersLists.clear();
        URL_DATA="https://bombill.com/bombill_pages/search_vendor_product.php?key="+strSerach_restro + "&latlon="+Newsearch_lat_lng;

        loadUrlData();
    }


   @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK ) {

            //when sucess

            Place place =Autocomplete.getPlaceFromIntent(data);

            Search_Location.setText(place.getAddress());

             Address12.setText(place.getAddress());
            addresforconfirm= Address12.getText().toString();
           // addresforconfirm=place.getAddress();


//                Address1.setText(String.format("Locality Name : %s",place.getName()));
           // address= place.getAddress();
            Log.d(TAG,place.getName());
//                latshow.setText(String.valueOf(place.getLatLng()));
            location = place.getLatLng().toString();
            if (!location.equals("")) {
                location = location.substring(10, 30);
//                    latshow.setText(location);
            }
            Log.d(TAG,"Inside onResume");
            Log.d(TAG, location);
//        Log.d(TAG, address);\
            developersLists.clear();
            URL_DATA = "https://bombill.com/bombill_pages/customer_get_vendors.php?coordinate=" +location;
           // latshow.setText(location);
            Address.setText(addresforconfirm);

            bottomSheetDialog.dismiss();

            loadUrlData();




        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            //Where error

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }
//ProgessDialog




   }
    private void loadUrlData() {


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //  URL_DATA = "http://nirvaacls.com/bombill_pages/customer_get_vendors.php?coordinate=" +latlong;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    //String json;
                    //      JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = new JSONArray(response);
                    if (array.length()!=0){
                        for (int i = 0; i < array.length(); i++){
                            JSONObject jo = array.getJSONObject(i);
                            HomePageVendorList developers = new HomePageVendorList(jo.getString("vendor_id"),jo.getString("name"), jo.getString("url")
                                    ,jo.getString("delivery_distance"),jo.getString("min_amount"),jo.getInt("Distance_from_user"));
                            developersLists.add(developers);

                        }
                        adapter = new HomePageVendorAdapter(developersLists, getActivity());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        homePageVendorAdapter = new HomePageVendorAdapter(developersLists, getActivity().getApplicationContext());
                        recyclerView.setAdapter(homePageVendorAdapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "No Internet Connection" + error.toString(), Toast.LENGTH_SHORT).show();
             //   progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.searchrestaro, menu);
        //  MenuItem menuItem=menu.findItem(R.id.search);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
        /* SearchView searchView=(SearchView)menuItem.getActionView();*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                homePageVendorAdapter.getFilter().filter(s);
                return false;
            }
        });

    }
}
