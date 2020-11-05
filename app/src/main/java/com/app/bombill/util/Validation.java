package com.app.bombill.util;

import android.text.TextUtils;
import android.util.Log;

import com.app.bombill.PayUMoney.AppPreference;
import com.app.bombill.R;

import java.util.regex.Pattern;

/**
 * Created by amolmhatre on 8/2/20
 */

public class Validation {
    private static final String TAG = "Validation";

    public static boolean isValidEmail1(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    /**
     * This fucntion checks if email and mobile number are valid or not.
     * @param email  email id entered in edit text
     * @return boolean value
     */

    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            Log.d(TAG,R.string.err_email_empty+"");
            return false;
        } else if(!(email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Log.d(TAG,R.string.email_not_valid+"");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This fucntion checks if email and mobile number are valid or not.
     * @param mobile mobile number entered in edit text
     * @return boolean value
     */
    public static boolean isValidPhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            Log.d(TAG,R.string.err_phone_empty+"");
            return false;
        } else if(!(Pattern.compile(AppPreference.PHONE_PATTERN).matcher(mobile).matches())){
            Log.d(TAG,R.string.err_phone_not_valid+"");
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNumber(String number){
        if (!number.matches("[0-9]+")) {
            Log.d(TAG, "Invalid number: "+number);
            return false;
        } else
            return true;
    }

    public static boolean isCharacter(String name){
        if (!name.matches("^[a-zA-Z_ ]*$")) {//[a-zA-Z_]+
            Log.d(TAG, "Invalid Name: "+name);
            return false;
        } else
            return true;
    }


}
