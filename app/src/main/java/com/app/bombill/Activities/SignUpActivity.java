package com.app.bombill.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bombill.R;
import com.app.bombill.model.GenericResponse;
import com.app.bombill.model.LoginResponse;
import com.app.bombill.model.User;
import com.app.bombill.network.RetrofitClient;
import com.app.bombill.util.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;
    private static String next_activity, username, email, phone, address, password, token;
    private static EditText etUsername, etPassword, etEmail, etPhone, etAddress, etConfirmPassword;
    private static Button btnSingup;
    private static boolean dataValidity = false;
    private static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        next_activity = getIntent().getStringExtra("next_activity");

        sharedpreferences = this.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedpreferences.edit();

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
        btnSingup = (Button) findViewById(R.id.btnSignup);
        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
//                UserRegister();
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

    public void validateData() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialog.setTitle("Something went wrong!");

        username = etUsername.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        address = etAddress.getText().toString();
        password = etPassword.getText().toString();

        if (Validation.isCharacter(username)) {
            dataValidity = true;
            if (Validation.isValidEmail(email)) {
                dataValidity = true;
                if (Validation.isValidPhone(phone)) {
                    dataValidity = true;
                    if (!address.equals(null)) {
                        dataValidity = true;
                        if (!password.equals(null)) {
                            if (password.equals(etConfirmPassword.getText().toString())) {
                                dataValidity = true;

                            } else {
                                dataValidity = false;
                                alertDialog.setMessage("Please put matching password\"")
                                        .create()
                                        .show();
                            }
                        } else {
                            dataValidity = false;
                            alertDialog.setMessage("Password cannot be blank")
                                    .create()
                                    .show();
                        }
                    } else {
                        dataValidity = false;
                        alertDialog.setMessage("Please fill in the address")
                                .create()
                                .show();
                    }
                } else {
                    dataValidity = false;
                    alertDialog.setMessage("Put phone number in proper format.")
                            .create()
                            .show();
                }
            } else {
                dataValidity = false;
                alertDialog.setMessage("Put email in proper format.")
                        .create()
                        .show();
            }

        }else {
            dataValidity = false;
            alertDialog.setMessage("Only english alphabets are allowed for name.")
                    .create()
                    .show();
        }

        if (dataValidity) {
            customer_register();
        }

    }

    private void customer_register() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing up");
        progressDialog.setMessage("Your account is being created");
        progressDialog.setIcon(R.drawable.bombill);
        progressDialog.show();

            Call<GenericResponse> call = RetrofitClient
                    .getInstance().getApi().customer_register(username, email, phone, address, password);
            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                    /**Adding a delay for insert query in database */
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            loginApiCall();
                            progressDialog.dismiss();
                        }
                    }, 2000); /**Time required to execute insert query in bombill database*/
                }
                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Your account already exists",Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void loginApiCall() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Your newly created account is being used for signing in.");
        progressDialog.setIcon(R.drawable.bombill);
        progressDialog.show();

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
                    user = loginResponse.getUser();
                    saveUserLogin();
                    Login();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();     
                    finish();
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Failure !!!");
            }
        });
    }

    private void saveUserLogin() {
        Log.d(TAG, "Remember me  clicked");
        String password = etPassword.getText().toString();
        editor.putBoolean("saveLogin", true);
        editor.putString("customer_name", user.getCustomerName());
        editor.putString("customer_id", user.getCustomerId());
        editor.putString("customer_email", email);
        editor.putString("customer_phone", phone);
        editor.putString("customer_address", address);
        editor.putString("customer_password", password);
        editor.putString(String.valueOf(R.string.TOKEN), token);
        editor.commit();
    }

    private void Login() {
        switch (next_activity) {
            case "none":
                finish();
                break;
            case "customer_profile":
                editor.putInt("home_fragment",2);
                editor.commit();
                startActivity(new Intent(this, Home_Activity.class));
                finish();
                break;
            case "Check_Out_Order":
                startActivity(new Intent(this, Check_Out_Order.class).putExtra("customer_address",address));
                finish();
                break;
        }
        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
    }

}