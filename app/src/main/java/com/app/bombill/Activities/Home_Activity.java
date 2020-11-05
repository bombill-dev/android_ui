package com.app.bombill.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.bombill.Cart.Cart_Fragment;
import com.app.bombill.Fragments.Customer_profile_fragment;
import com.app.bombill.Fragments.Home_fragment;
import com.app.bombill.Fragments.Your_Order;
import com.app.bombill.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarteist.autoimageslider.SliderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Activity extends AppCompatActivity {

    private static final String TAG = "Home_Activity";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    //Decalretion of Variable
    //  private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle DrawerToggle;
    private Toolbar toolbar;
    TextView Address;
    public static final String SHARED_PREF_NAME = "BOMBILL_USER";

    //
    public CircleImageView Profile_photo;
    public TextView lat_lng;
    EditText Search_Location;

    String FetchCustomerImage;


    //For Menu CartIon
    MenuItem mCartMenuItem;
    ImageButton Cartbtn;


//For Navigation View

    //NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    String image, name, gmail, contact_no, address;

//For Home Page Vendor Image Silder

    private SliderLayout sliderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bombill_home);

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("Bombill Notification","Bombill Notification",
                            NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            FirebaseMessaging.getInstance().subscribeToTopic("general")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Successfull";
                            if (!task.isSuccessful()) {
                                msg = "Failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(Home_Activity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
       // toolbar = findViewById(R.id.toolbar);
        //  mDrawerLayout = findViewById(R.id.drawerlayout);
        //DrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
//        mDrawerLayout.addDrawerListener(DrawerToggle);
//        DrawerToggle.syncState();
//        Address = toolbar.findViewById(R.id.address);
  //      SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(Home_Activity.this);
    //    address = prefs1.getString("address", null);
      //  Address.setText(address);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For Navigation View
        //  navigationView = findViewById(R.id.navigation_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //View view =navigationView.getHeaderView(0);
        //For Profile Photo
        //Profile_photo=view.findViewById(R.id.profile_photo_navi);
        //cus_Name=view.findViewById(R.id.Customer_name);
        //Gmail=view.findViewById(R.id.gmail_addres);




        String cus_id = "2";
//        FetchCustomerImage = "http://nirvaacls.com/bombill_pages/customer_get_profile.php?customer_id=" + cus_id;
        FetchCustomerImage = "https://new.bombill.com/bombill_pages/customer_get_profile.php?customer_id=" + cus_id;

        FetchCustomerImage();
//This frgamnet for Replaceing main to other Fragment


        //For Image Silder

       /* sliderLayout=findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(2);
        setSliderView();*/

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi_home:
                        Log.d(TAG,"Inside onNavigationItemSelected");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                                Home_fragment()).commit();
//                        FetchCustomerImage();
                        break;

                    case R.id.navi_account:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
//                                Account_Fragment()).commit();
                        String customer_id1 = sharedPreferences.getString("customer_id", null);
                        if (customer_id1 == null)
                        {
                            alertLoginSignup();

                        }else
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                                    Customer_profile_fragment()).commit();
                        break;
                    case R.id.navi_yourorder:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                                Your_Order()).commit();
                        break;

                    case R.id.navi_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                                Cart_Fragment()).commit();
                        break;
                }

                return true;
            }
        });
    }

    void alertLoginSignup() {
        //Delete sesion token and stored user credentials
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Not Logged in !!")
                .setMessage("Do you want to Login or Register?")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home_Activity.this, LoginActivity.class);
                        intent.putExtra("next_activity", "none");
                        startActivity(intent);
                    }
                })
                .setNeutralButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home_Activity.this, SignUpActivity.class);
                        intent.putExtra("next_activity", "none");
                        startActivity(intent);
                    }
                }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int fragmentNumber = sharedPreferences.getInt("home_fragment", 0);

        switch (fragmentNumber) {
            case 0:
                /** Home */
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        Home_fragment()).commit();
                break;
            case 1:
                /** Cart */
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        Cart_Fragment()).commit();
                break;
            case 2:
                /** Your Orders */
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        Your_Order()).commit();
                break;
            case 3:
                /** Manage Profile */
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        Customer_profile_fragment()).commit();
                break;
        }
    }


    private void FetchCustomerImage() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                FetchCustomerImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    //String json;
                    //      JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);


                        //  name = jo.getString("name ") ;
                       /* contact_no = jo.getString("contact_no");
                        address = jo.getString("address");
                        image = jo.getString("cust_profile");

                        cus_Name.setText(jo.getString("name"));
                        Gmail.setText(jo.getString("email"));
                        Glide.with(Home_Activity.this)
                                .load("http://nirvaacls.com/profile_image/10001.jpeg")
                                .into(Profile_photo);*/

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(getActivity(), "No Internet Connection" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //Creating a menu in layot file use for sidel navigation bar
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        mCartMenuItem = menu.findItem(R.id.cart_count_menu);

        View actionView = mCartMenuItem.getActionView();
        Cartbtn = actionView.findViewById(R.id.cart_btn);

        Cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home_Activity.this, AddToCart.class));
              *//*  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                        Cart_Fragment()).commit();
*//*
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Asks for confirmation before user logs out
     */
    void logOutAlert() {
        //Delete sesion token and stored user credentials
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation")
                .setMessage("Are you sure, you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getBaseContext(), "Welcome Back", Toast.LENGTH_SHORT).show();
                    }
                }).show();
        SharedPreferences sharedPreferences = this.getSharedPreferences("BOMBILL_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("saveLogin", false);
        editor.putString("customer_id", "");
        editor.putString("customer_name", "");
        editor.putString("customer_password", "");
        editor.commit();
    }
}
