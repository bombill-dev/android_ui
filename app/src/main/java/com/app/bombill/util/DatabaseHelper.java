package com.app.bombill.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by amolmhatre on 9/22/20
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "cart.db";
    public static final String TABLE_NAME = "product_table";
    public static final String COLUMN_1 = "vendor_id";
    public static final String COLUMN_2 = "product_id";
    public static final String COLUMN_3 = "product_name";
    public static final String COLUMN_4 = "product_quantity";
    public static final String COLUMN_5 = "product_price";
    public static final String COLUMN_6 = "product_total";
    public static final String COLUMN_7 = "id";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" +
                COLUMN_1 + " TEXT, " +
                COLUMN_2 + " TEXT UNIQUE, " +
                COLUMN_3 + " TEXT, " +
                COLUMN_4 + " TEXT, " +
                COLUMN_5 + " TEXT, " +
                COLUMN_6 + " TEXT, " +
                COLUMN_7 + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")");
        Log.d(TAG, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d(TAG, "onUpgrade");
        onCreate(sqLiteDatabase);
    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;

    }

    public String getVendorId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            /** Selecting top row, first column from table, which is vendor id*/
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_1 + " FROM " + TABLE_NAME + " LIMIT 1", null);
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (CursorIndexOutOfBoundsException exception) {
            return "0";
        }
    }

    public String getNumberOfItems() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            /** simple count for number of rows */
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(" + COLUMN_2 + ") FROM " + TABLE_NAME + " ;", null);
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (CursorIndexOutOfBoundsException exception) {
            return "0";
        }
    }

    public boolean findProductID(String product_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        /** get 1 count for occurrence of product_id in column and pick top, which will be 0 or 1 */
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE " + COLUMN_2 + " = '" + product_id + "';", null);
        cursor.moveToFirst();
        return cursor.getString(0).equals("1");
    }

    public String addItem(String product_id) {
        /** Select row, increment quantity, update total, put both back in row*/
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_2 + " = '" + product_id + "';", null);
        cursor.moveToFirst();
        int quantity = Integer.parseInt(cursor.getString(3));
        quantity++;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_4, quantity + "");
        contentValues.put(COLUMN_6, quantity * Integer.parseInt(cursor.getString(4)) + "");
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_2 + "=?", new String[]{product_id});
        Log.d(TAG, "result" + quantity);
        return quantity + "";
    }

    public String subtractItem(String product_id) {
        /** Select row, decrement quantity, update total, put both back in row*/
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_2 + " = '" + product_id + "';", null);
        cursor.moveToFirst();
        int quantity = Integer.parseInt(cursor.getString(3));
        quantity--;
        if (quantity==0){
            deleteData(product_id);
            return "0";
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_4, quantity + "");
            contentValues.put(COLUMN_6, quantity * Integer.parseInt(cursor.getString(4)) + "");
            sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_2 + "=?", new String[]{product_id});
            Log.d(TAG, "result" + quantity);
            return quantity + "";
        }
    }

    public String getQuantity(String product_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_4 + " FROM " + TABLE_NAME + " WHERE " + COLUMN_2 + " = '" + product_id + "';", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String getTotalPrice() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            /** Selecting whole column, iterate over it. And sum it up using loop */
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_6 + " FROM " + TABLE_NAME + ";", null);
            int total = 0;
            while (cursor.moveToNext()) {
                total = total + Integer.parseInt(cursor.getString(0));
            }
            return total + "";
        } catch (CursorIndexOutOfBoundsException exception) {
            return "0";
        }
    }

    public boolean insertData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int total = Integer.parseInt(product_quantity)
                * Integer.parseInt(product_price);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, vendor_id);
        contentValues.put(COLUMN_2, product_id);
        contentValues.put(COLUMN_3, product_name);
        contentValues.put(COLUMN_4, product_quantity);
        contentValues.put(COLUMN_5, product_price);
        contentValues.put(COLUMN_6, total);
        long result = 0;
        try {
            result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            Log.d(TAG, "insertData Result:" + result);
        } catch (SQLiteConstraintException exception) {
            Log.d(TAG, "Trying to insert same product id:" + result);
        }
        return result == -1 ? false : true;
    }

    public boolean updateData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int total = Integer.parseInt(product_quantity)
                * Integer.parseInt(product_price);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, vendor_id);
        contentValues.put(COLUMN_2, product_id);
        contentValues.put(COLUMN_3, product_name);
        contentValues.put(COLUMN_4, product_quantity);
        contentValues.put(COLUMN_5, product_price);
        contentValues.put(COLUMN_6, total);
        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_2 + "=?", new String[]{product_id});
        Log.d(TAG, "updateData Result:" + result);
        return result == 0 ? false : true;
    }

    public boolean deleteData(String product_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, COLUMN_2 + "=?", new String[]{product_id});
        Log.d(TAG, "deleteData Result:" + result);
        return result == 0 ? false : true;
    }

    public boolean deleteTable(String vendor_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, COLUMN_1 + "=?", new String[]{vendor_id});
        Log.d(TAG, "deleteTable Result:" + result);
        return result == 0 ? false : true;
    }
}
