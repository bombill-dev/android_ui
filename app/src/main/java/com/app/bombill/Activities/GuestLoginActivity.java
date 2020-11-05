package com.app.bombill.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bombill.R;
import com.app.bombill.model.GenericResponse;
import com.app.bombill.model.LoginResponse;
import com.app.bombill.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestLoginActivity extends AppCompatActivity {
    private static final String TAG = "GuestLoginActivity";
    private static String next_activity, username, email, phone, token, customer_id;
    private static EditText etUsername, etEmail, etPhone;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Button btnGuestLogin;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_activity = getIntent().getStringExtra("next_activity");
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnGuestLogin = (Button)findViewById(R.id.btnGuestLogin);
        btnGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                phone = etPhone.getText().toString();
                loginApiCall();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        // Log and toast
                        Log.d(TAG, "Token " + token);
                    }
                });
    }

    private void loginApiCall() {
        btnGuestLogin.setClickable(false);
        Call<GenericResponse> call = RetrofitClient.getInstance().getApi()
                .customer_guest_login(email, phone, username, token);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.body().getError().equals("200")){
                    customerGetId();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {

            }
        });
    }

    private void customerGetId(){
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().customer_get_id(email);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                customer_id = response.body().getUser().getCustomerId();
                saveUserData();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void saveUserData() {
        Log.d(TAG, "Customer: "+customer_id);
        editor.putString("customer_id", customer_id);
        editor.putString("customer_name", username);
        editor.putString("customer_email", email);
        editor.putString("customer_phone", phone);
        editor.putString(String.valueOf(R.string.TOKEN), token);
        editor.commit();
        Login();
    }

    private void Login() {
        switch (next_activity) {
            case "none":
                finish();
                break;
            case "customer_profile":
                editor.putInt("home_fragment",0);
                editor.commit();
                startActivity(new Intent(this, Home_Activity.class));
                finish();
                break;
            case "Check_Out_Order":
                startActivity(new Intent(this, Check_Out_Order.class).putExtra("customer_address","Please put your address here"));
                finish();
                break;
        }
        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    }
}