package com.app.bombill.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.bombill.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.location.LocationManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Splash_Screen extends AppCompatActivity {

//    Animation topanimation;
    ImageView imageView;
    private static final String TAG = "Splash_Screen";
    public static final String SHARED_PREF_NAME = "BOMBILL_USER";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    LocationManager locationManager;
    String latitudee;
    String userAddress;

    //mobile location on/of
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_CHECK_CODE = 8989;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("home_fragment", 0).apply();


  //      topanimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        imageView=findViewById(R.id.splash_screen_image);
    //    imageView.setAnimation(topanimation);
        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);

       Task<LocationSettingsResponse> result =
               LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());


             result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                 @Override
                 public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                     try {
                         task.getResult(ApiException.class);
                     } catch (ApiException e) {
                       switch (e.getStatusCode())
                       {
                           case LocationSettingsStatusCodes
                                   .RESOLUTION_REQUIRED:

                               try {
                                   ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                   resolvableApiException.startResolutionForResult(Splash_Screen.this,REQUEST_CHECK_CODE);
                               } catch (IntentSender.SendIntentException ex) {
                                   ex.printStackTrace();
                               }catch (ClassCastException ex){

                               }
                               break;

                           case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                           {
                               break;
                           }
                       }
                     }
                 }
             });

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Splash_Screen.this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getCurrentLocation();
                }
            }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "permission denaied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(Splash_Screen.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Splash_Screen.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() >0 ){
                            int latestLocation = locationResult.getLocations().size() -1 ;
                            double latitude
                                    =locationResult.getLocations().get(latestLocation).getLatitude();
                            double longitide=
                                    locationResult.getLocations().get(latestLocation).getLongitude();

                            try {

                                Geocoder geocoder = new Geocoder(Splash_Screen.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitide, 1);
                                if (addresses != null && addresses.size() > 0) {

                                    userAddress = addresses.get(0).getAddressLine(0);
                                   // String a=userAddress= addresses.get(0).getAddressLine(0);

                                    editor.putString("gpsLocation", latitude + "," + longitide);
                                    editor.putString("gpsAddress",userAddress);
                                    Log.d(TAG ,"gpsAddress: "+latitude);
                                    editor.commit();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            latitudee= String.valueOf(locationResult.getLocations().get(latestLocation));


                            if (latitudee !=null)
                            {
                                startActivity(new Intent(Splash_Screen.this, Location_Animation.class));
                                overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                                finish();
                               // Toast.makeText(Splash_Screen.this, "Getting your Location ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, Looper.getMainLooper());

    }
}