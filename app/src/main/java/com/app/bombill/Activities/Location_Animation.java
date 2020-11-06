package com.app.bombill.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.app.bombill.R;

public class Location_Animation extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static final String SHARED_PREF_NAME = "BOMBILL_USER";
    TextView Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__animation);


        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Address=findViewById(R.id.address);

       String location_address=sharedPreferences.getString("gpsAddress",null);


        Address.setText(location_address);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Location_Animation.this,Home_Activity.class));
                finish();
            }
        },2400);
    }
}