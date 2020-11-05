package com.app.bombill.PayUMoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.app.bombill.Activities.Check_Out_Order;
import com.app.bombill.Activities.Home_Activity;
import com.app.bombill.R;
import com.app.bombill.network.RetrofitClient;
import com.app.bombill.util.DatabaseHelper;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayUMoneyActivity extends BaseActivity{
    public static final String TAG = "PayUMoneyActivity";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static DatabaseHelper databaseHelper;

    private boolean isDisableExitConfirmation = false;

    private AppPreference mAppPreference;

    private static String customer_name, customer_email,
            customer_phone, paymentAmount, paymentTitle, paymentStatus,
            txnid,vendor_order_id,hash;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        mAppPreference = new AppPreference();
        databaseHelper = new DatabaseHelper(this);


        /**Collect Data through SharedPref */
        customer_name = sharedPreferences.getString("customer_name",null);
        customer_email = sharedPreferences.getString("customer_email",null);
        customer_phone = sharedPreferences.getString("customer_phone",null);
        paymentAmount = sharedPreferences.getString("paymentAmount",null);
        paymentTitle = sharedPreferences.getString("paymentTitle",null);
        paymentStatus = sharedPreferences.getString("paymentStatus",null);

        vendor_order_id = sharedPreferences.getString("vendor_order_id",null);
        txnid = sharedPreferences.getString("txnid",null);
        hash = sharedPreferences.getString("hash",null);

        /** Select Environment */
//        selectSandBoxEnv();
        selectProdEnv();

        /** make payment */
//        launchPayUMoneyFlow();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    /**
     * This function sets the mode to PRODUCTION in Shared Preference
     */
    private void selectProdEnv() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                editor = sharedPreferences.edit();
                editor.putBoolean("is_prod_env", true);
                editor.apply();
            }
        }, AppPreference.MENU_DELAY);
        launchPayUMoneyFlow();

    }

    /**
     * This function sets the mode to SANDBOX in Shared Preference
     */
    private void selectSandBoxEnv() {
        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        editor = sharedPreferences.edit();
        editor.putBoolean("is_prod_env", false);
        editor.apply();
    }


    /**
     * This function sets the layout for activity
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_payumoney;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("PayUMoneyActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                Log.d(TAG,"payuResponse"+payuResponse);
                Log.d(TAG,"merchantResponse"+merchantResponse);
                showOrderDetails();
//                new AlertDialog.Builder(this)
//                        .setCancelable(false)
//                        .setMessage("Your order has been placed successfully !!\n" +
//                                "Order Id: "+vendor_order_id)
//                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                updateOrderStatus();
//                                dialog.dismiss();
//                            }
//                        }).show();
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        } else {
            /** Payment failed, go to checkout screen*/
//            String vendor_id = sharedPreferences.getString("vendor_id","0");
//            editor.putInt("home_fragment", 0).apply();
            startActivity(new Intent(PayUMoneyActivity.this, Check_Out_Order.class));
            finish();
        }
    }
    private void showOrderDetails() {
        String order_details = productDetails();
        new AlertDialog.Builder(PayUMoneyActivity.this)
                .setCancelable(false)
                .setTitle("Thanks for ordering with Bombill")
                .setMessage(order_details)
                .setCancelable(false)
                .setNeutralButton("Proceed to Homepage", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do your task
//                        startActivity(new Intent(PayUMoneyActivity.this, Home_Activity.class));
//                        finish();
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.bombill)
                .show();
        updateOrderStatus();
        databaseHelper.deleteTable(sharedPreferences.getString("vendor_id", null));

    }

    private String productDetails(){
        /** Extract product data from database */
        Cursor cursor = databaseHelper.getData();
        /**cursor is just pointer to read table*/
        /**cursor.getString(0) denotes zeroth column*/
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            stringBuffer.append("\n"+cursor.getString(2)+" x ")
                    .append(cursor.getString(3)+"\t")
                    .append("₹"+cursor.getString(5));
        }

        Log.d(TAG,stringBuffer.toString());
        return stringBuffer.toString();
    }

    private void updateOrderStatus(){
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().
                customer_update_payumoney_status(vendor_order_id,txnid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                /**clear table here*/
//                databaseHelper.deleteTable(sharedPreferences.getString("vendor_id", null));
                editor.putInt("home_fragment", 0);
                editor.apply();
                Intent intent = new Intent(PayUMoneyActivity.this, Home_Activity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText(paymentTitle);
        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle(paymentStatus);
        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        String productName = mAppPreference.getProductInfo();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        builder.setAmount(paymentAmount)
                .setTxnId(txnid)
                .setPhone(customer_phone)
                .setProductName(productName)
                .setFirstName(customer_name)
                .setEmail(customer_email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoneyActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoneyActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }
        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /** Thus function calculates the hash for transaction
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);
        return paymentParam;
    }
}
