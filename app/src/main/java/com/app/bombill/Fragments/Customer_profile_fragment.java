package com.app.bombill.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.bombill.Activities.Home_Activity;
import com.app.bombill.Activities.Manage_Profile;
import com.app.bombill.R;
import com.app.bombill.model.CustomerProfile;
import com.app.bombill.network.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Customer_profile_fragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Customer_profile_fragme";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private GoogleMap googleMap;

    String Url;
    TextView Name, Email, Phone, Address,Password;
    CircleImageView Profile_photo;
    String name, email, phone, address, image,customer_id,password;
    Button Update_Profie;
    ImageButton btnLogout;


    public Customer_profile_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_profile_fragment, container, false);
        sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Name = view.findViewById(R.id.customer_name);
        Email = view.findViewById(R.id.customer_email_adrress);
        Phone = view.findViewById(R.id.customer_phone_number);
        Profile_photo = view.findViewById(R.id.ivProfileImage);
        Update_Profie = view.findViewById(R.id.update_profile);
        Address = view.findViewById(R.id.cus_address);
        Password = view.findViewById(R.id.customer_password);
        customer_id = sharedPreferences.getString("customer_id", null);

        Url= "https://bombill.com/bombill_pages/customer_get_profile.php?customer_id=" +customer_id;

        Update_Profie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Manage_Profile.class));
            }
        });

        btnLogout = (ImageButton) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutAlert();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        customerGetProfile();

    }

    private void customerGetProfile(){
        Call<CustomerProfile> call = RetrofitClient
                .getInstance().getApi().customer_get_profile(customer_id);
        call.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                name=response.body().getName();
                email = response.body().getEmail();
                phone = response.body().getContactNo();
                address = response.body().getAddress();
                image=response.body().getCustProfile();
                address=response.body().getAddress();
                password=response.body().getPassword();

                Name.setText(name);
                Phone.setText(phone);
                Email.setText(email);
                Address.setText(address);
                Password.setText(password);

                Glide.with(getContext())
                        .load(image)
                        .into(Profile_photo);

                editor.putString("customer_name",name);
                editor.putString("customer_phone",phone);
                editor.putString("customer_email",email);
                editor.putString("customer_address",address);
                editor.putString("customer_password",password).commit();
            }

            @Override
            public void onFailure(Call<CustomerProfile> call, Throwable t) {

            }
        });
    }

    void logOutAlert() {
        //Delete sesion token and stored user credentials
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Confirmation")
                .setMessage("Are you sure, you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putInt("home_fragment",0).apply();
                        getActivity().startActivity(new Intent(getContext(), Home_Activity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Welcome Back", Toast.LENGTH_SHORT).show();
                    }
                }).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("saveLogin", false);
        editor.putString("customer_id", null);
        editor.putString("customer_name", "");
        editor.putString("customer_password", "");
        editor.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String gpsLocation = sharedPreferences.getString("gpsLocation","0,0");
        String[] location =  gpsLocation.split(",");
        this.googleMap = googleMap;
        // Add a marker in Location and move the camera
        LatLng Location = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        this.googleMap.addMarker(new MarkerOptions().position(Location).title("Marker in Belapur"));
//        this.googleMap.setBuildingsEnabled(false);
//        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Location,15));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Location));

        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(Location).
//                tilt(60).
                zoom(15).
                bearing(0).
                build();
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}