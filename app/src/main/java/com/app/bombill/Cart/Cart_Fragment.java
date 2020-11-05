package com.app.bombill.Cart;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bombill.Activities.Check_Out_Order;
import com.app.bombill.Adapters.CartAdapter;
import com.app.bombill.R;
import com.app.bombill.model.CartModel;
import com.app.bombill.Activities.GuestLoginActivity;
import com.app.bombill.Activities.LoginActivity;
import com.app.bombill.Activities.SignUpActivity;
import com.app.bombill.model.DatabaseModel;
import com.app.bombill.util.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart_Fragment extends Fragment {


    private static final String TAG = "Cart_Fragment";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static DatabaseHelper databaseHelper;
    private static Cursor cursor;
    private CartAdapter cartAdapter;


    private static String customer_id;
    private static boolean saveLogin;
    RecyclerView recyclerView;
    ImageView image_cart;

    float totalprice;
    String productid;
    public Button Checkout;
    Float a;
    int b;
    public TextView Order_Total;
    Snackbar snackbar;
    Snackbar.SnackbarLayout snackbarLayout;
    LayoutInflater inflater;
    Dialog myDialog;

    ArrayList<CartModel> cartModels = new ArrayList<>();
    ArrayList<DatabaseModel> databaseModelList = new ArrayList<>();
    /* public static CartAdapter my;*/
    String LoadUrl;
    Float alltotalprice = Float.valueOf("0");
    String TotalPricefinal;
    public Cart_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(view.getContext());
        cursor = databaseHelper.getData();


        editor.putInt("home_fragment",1).apply();
        customer_id = sharedPreferences.getString("customer_id", null);
        saveLogin = sharedPreferences.getBoolean("saveLogin", false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        image_cart = (ImageView) view.findViewById(R.id.image_cart);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Checkout = view.findViewById(R.id.checkout);
        Order_Total = view.findViewById(R.id.order_total);
        myDialog = new Dialog(getActivity());

        if (cursor.getCount() > 0) {
            image_cart.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                CartModel cartModel = new CartModel(cursor.getString(6),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(3),
                        cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(0));
                cartModels.add(cartModel);
                DatabaseModel databaseModel = new DatabaseModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                databaseModelList.add(databaseModel);
            }

            alltotalprice = Float.parseFloat(databaseHelper.getTotalPrice());
            Order_Total.setText(databaseHelper.getTotalPrice());
            sharedPreferences.edit().putString("product_total",String.valueOf(alltotalprice)).commit();
            Log.d(TAG,TAG+"/product_total: "+alltotalprice);
//            cartAdapter = new CartAdapter(getContext(), databaseModelList);
            cartAdapter = new CartAdapter(databaseModelList, getContext());
//            recyclerView.setAdapter(my);
            recyclerView.setAdapter(cartAdapter);
//            my.notifyDataSetChanged();
            cartAdapter.notifyDataSetChanged();
        }

        if (alltotalprice<1)
        {
            Checkout.setVisibility(View.GONE);
        }else {
            Checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String product_names, product_prices, product_quantities;
                    int total = 0;

                    product_names = cartModels.get(0).getName();
                    product_prices = cartModels.get(0).getPrice();
                    product_quantities = cartModels.get(0).getQuantity();


                    for (int i = 1; i < cartModels.size(); i++) {
                        product_names = product_names + "," + cartModels.get(i).getName();
                        product_prices = product_prices + "," + cartModels.get(i).getPrice();
                        product_quantities = product_quantities + "," + cartModels.get(i).getQuantity();
                        total = total + (Integer.parseInt(cartModels.get(i).getPrice()) * Integer.parseInt(cartModels.get(i).getPrice()));
                    }

                    editor.putString("product_names", product_names);
                    editor.putString("product_prices", product_prices);
                    editor.putString("product_quantities", product_quantities);
//                editor.putString("product_total", total + "").commit();
                    editor.apply();
//                ShowPopup();
                    checkoutOrder();
                }
            });
        }
        return view;
    }

    private void checkoutOrder() {
        if (!saveLogin) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
            alertDialog.setTitle("Not logged in ?");
            alertDialog.setMessage("If you haven't registered with us yet, " +
                    "you are missing out on exciting offers and seafood deals.");
            alertDialog.setIcon(R.drawable.bombill);
            alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("next_activity", "Check_Out_Order");
                    startActivity(intent);
                    dialog.cancel();
                }
            });
            alertDialog.setNeutralButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    intent.putExtra("next_activity", "Check_Out_Order");
                    startActivity(intent);
                    dialog.cancel();
                }
            });
            alertDialog.setNegativeButton("Guest", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), GuestLoginActivity.class);
                    intent.putExtra("next_activity", "Check_Out_Order");
                    startActivity(intent);
                    dialog.cancel();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        } else {
            startActivity(new Intent(getActivity(), Check_Out_Order.class));
        }


    }

}