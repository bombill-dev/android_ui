package com.app.bombill.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bombill.R;
import com.app.bombill.model.CustomerProfile;
import com.app.bombill.model.LoginResponse;
import com.app.bombill.model.User;
import com.app.bombill.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static String next_activity, name, email, phone, address, password, token;
    private static final String forgotPasswordPage ="https://bombill.com/forgetpassword.php";
    private static EditText etUsername, etPassword;
    private static TextView tvForgotPassword;
    private static CheckBox checkRememberMe;
    private static User user = new User();
    private static CustomerProfile customerProfile = new CustomerProfile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        next_activity = getIntent().getStringExtra("next_activity");
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        checkRememberMe = (CheckBox) findViewById(R.id.checkRememberMe);

        sharedPreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("saveLogin", false)) {
            Login();
        }

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

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**download pdf from link below*/
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(forgotPasswordPage));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    public void LoginButton(View view) {
        loginApiCall();
    }

    public void loginApiCall() {
        //Login call
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().customer_login(
                        etUsername.getText().toString(),
                        etPassword.getText().toString(),
                        token);

        Log.d(TAG, etUsername.getText().toString() +
                etPassword.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse.getError().equals("200")) {
                    Log.d(TAG, "\n Success !!! " + loginResponse.getUser().getCustomerId());
                    editor.putString("customer_id", loginResponse.getUser().getCustomerId());
                    editor.commit();
//                    BombillSharedPreference.getInstance(MainActivity.this).setUser(loginResponse.getUser());
                    user = loginResponse.getUser();
                    customerGetProfile(loginResponse.getUser().getCustomerId());
                } else if (loginResponse.getError().equals("403")){
                    Toast.makeText(getBaseContext(),loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "Failure !!!");
            }
        });
    }

    private void customerGetProfile(String customer_id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("We are connecting to our servers.");
        progressDialog.setIcon(R.drawable.bombill);
        progressDialog.show();
        Log.d(TAG, "Inside customerGetProfile");
        Call<CustomerProfile> call = RetrofitClient
                .getInstance().getApi().customer_get_profile(customer_id);
        call.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                name = etUsername.getText().toString();
                email = response.body().getEmail();
                phone = response.body().getContactNo();
                address = response.body().getAddress();
                password = etPassword.getText().toString();
                customerProfile = response.body();
                Log.d(TAG, "customerGetProfile: " + response.message());
                saveUserLogin();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomerProfile> call, Throwable t) {

            }
        });
    }


    private void saveUserLogin() {
        // Checking if user have selected Remember me option
        if (checkRememberMe.isChecked()) {
            Log.d(TAG, "Remember me  clicked");
            editor.putBoolean("saveLogin", true);
        } else {
            Log.d(TAG, "Remember me not clicked");
        }
        String password = etPassword.getText().toString();
        editor.putString("customer_name", user.getCustomerName());
        editor.putString("customer_id", user.getCustomerId());
        editor.putString("customer_email", email);
        editor.putString("customer_phone", phone);
        editor.putString("customer_image", customerProfile.getCustProfile());
        editor.putString("customer_address", address);
        editor.putString("customer_password", password);
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
                editor.putInt("home_fragment",3);
                editor.commit();
                startActivity(new Intent(this, Home_Activity.class));
                Log.d(TAG, "Going to Home");
                finish();
                break;
            case "Check_Out_Order":
                startActivity(new Intent(this, Check_Out_Order.class).putExtra("customer_address",address));
                Log.d(TAG, "Going to checkout screen");
                finish();
                break;
        }
    }
}