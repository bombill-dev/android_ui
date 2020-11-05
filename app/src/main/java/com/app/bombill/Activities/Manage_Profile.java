package com.app.bombill.Activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.app.bombill.R;
import com.app.bombill.model.GenericResponse;
import com.app.bombill.network.RetrofitClientForImage;
import com.app.bombill.util.Validation;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amolmhatre on 9/10/20
 */

public class Manage_Profile extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "Manage_Profile";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static TextView etName, etEmail,etPhone,etAddress,etPassword;
    private static String customer_id,customer_name,customer_email,customer_phone,customer_address,customer_image , customer_password;
    private static Button btnSubmit;
    private static ImageButton btnProfileImage;
    private static CircleImageView ivProfileImage;
    private static Bitmap bitmap;
    private static boolean dataValidity = false, imageUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("home_fragment",3).apply();

        etName =findViewById(R.id.etName);
        etEmail =findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etAddress=findViewById(R.id.etAddress);
        etPassword=findViewById(R.id.etPassword);
        btnProfileImage=findViewById(R.id.btnProfileImage);
        ivProfileImage=findViewById(R.id.ivProfileImage);
        btnSubmit=findViewById(R.id.btnSubmit);

        customer_name = sharedPreferences.getString("customer_name", null);
        customer_email = sharedPreferences.getString("customer_email", null);
        customer_phone = sharedPreferences.getString("customer_phone", null);
        customer_address = sharedPreferences.getString("customer_address", null);
        customer_id = sharedPreferences.getString("customer_id", null);
        customer_image = sharedPreferences.getString("customer_image", null);
        customer_password = sharedPreferences.getString("customer_password", null);

        etName.setText(customer_name);
        etEmail.setText(customer_email);
        etPhone.setText(customer_phone);
        etAddress.setText(customer_address);
        etPassword.setText(customer_password);
        btnSubmit.setOnClickListener(this);
        btnProfileImage.setOnClickListener(this);

        Glide.with(this)
                .load(customer_image)
                .into(ivProfileImage);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnProfileImage:
                chooseFile();
                break;
            case R.id.btnSubmit:
                validateData();

        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                imageUpdate = true;
                bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), filepath);
                ivProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void validateData() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialog.setTitle("Something went wrong!");

        if (bitmap==null){
            ivProfileImage.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) ivProfileImage.getDrawable();
            bitmap = drawable.getBitmap();
        }

        customer_name = etName.getText().toString();
        if (Validation.isCharacter(customer_name)) {
            dataValidity = true;
            customer_email = etEmail.getText().toString();
            if (Validation.isValidEmail(customer_email)) {
                dataValidity = true;
                customer_phone = etPhone.getText().toString();
                if (Validation.isValidPhone(customer_phone)) {
                    dataValidity = true;
                    customer_address = etAddress.getText().toString();
                    if (!customer_address.equals(null)) {
                        dataValidity = true;
                        customer_password = etPassword.getText().toString();
                        if (!customer_password.equals(null)){
                            dataValidity = true;
                        }else {
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
        } else {
            dataValidity = false;
            alertDialog.setMessage("Only english alphabets are allowed for name.")
                    .create()
                    .show();
        }
        if (dataValidity) {
            if (imageUpdate)
                saveProfile(getStringImage(bitmap));
            else
                saveProfile("");
        }
    }

    public  String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
//        Log.d(TAG, "Photo: "+encodeImage );
        return encodeImage;
    }

    private void saveProfile(final String photo) {
        Call<GenericResponse> call = RetrofitClientForImage
                .getInstance().getApi().customer_set_profile(
                        photo,
                        customer_name,
                        customer_email,
                        customer_phone,
                        customer_address,
                        customer_password,
                        customer_id
                );

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait, Profile is getting updated");
        progressDialog.setIcon(R.drawable.bombill);
        progressDialog.setTitle("Updating Profile");
        progressDialog.show();

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                progressDialog.dismiss();
                Log.d(TAG, "Profile updated successfully");
                editor.putString("customer_name", customer_name);
                editor.putString("customer_email", customer_email);
                editor.putString("customer_phone", customer_phone);
                editor.putString("customer_address", customer_address);
                editor.putString("customer_password", customer_password);
                editor.commit();
                finish();
//                Toast.makeText(Manage_Profile.this, "Done", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Log.d(TAG, "Profile update failed !!!");
            }
        });
    }

}