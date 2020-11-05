package com.app.bombill.Adapters;

/**
 * Created by amolmhatre on 9/24/20
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bombill.R;
import com.app.bombill.model.DatabaseModel;
import com.app.bombill.util.DatabaseHelper;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static DatabaseHelper databaseHelper;
    private static ArrayList<DatabaseModel> databaseModelList;
    private static Context context;

    public CartAdapter(ArrayList<DatabaseModel> databaseModelList, Context context) {
        this.databaseModelList = databaseModelList;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_of_addtocart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        final DatabaseModel databaseModel = databaseModelList.get(position);
        holder.tvProductName.setText(databaseModel.getProduct_name());
        holder.tvProductQuantity.setText(databaseModel.getProduct_quantity());
        holder.tvProductPrice.setText(" × ₹" + databaseModel.getProduct_price());
        holder.tvItemQuantity.setText(databaseModel.getProduct_quantity());
        holder.tvProductTotal.setText("₹ " + databaseModel.getProduct_total());

        /** + button **/
        holder.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addItem(databaseModel.getProduct_id());
                updateUI();
            }
        });
        /** - button **/
        holder.btnSubtractItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseModel.getProduct_quantity().equals("1")) {
                    showActionDialog(position);
                } else {
                    databaseHelper.subtractItem(databaseModel.getProduct_id());
                    updateUI();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return databaseModelList.size();
    }

    private void updateUI(){
        context.startActivity(new Intent(context, context.getClass()));
        ((Activity) context).finish();
    }

    //This shown when customer click on particuler item it will show dialogbox
    private void showActionDialog(final int position) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Wanna delete "+databaseModelList.get(position).getProduct_name() +" for sure?");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("\uD83D\uDDD1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.subtractItem(databaseModelList.get(position).getProduct_id());
                context.startActivity(new Intent(context, context.getClass()));
                ((Activity) context).finish();
            }
        });
        alertDialog.setPositiveButton("❌", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    /** ################ ViewHolder Class ################## **/
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductQuantity;
        private TextView tvProductTotal;
        private TextView tvItemQuantity;
        private Button btnAddItem;
        private Button btnSubtractItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductTotal = itemView.findViewById(R.id.tvProductTotal);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            btnAddItem = itemView.findViewById(R.id.btnAddItem);
            btnSubtractItem = itemView.findViewById(R.id.btnSubtractItem);
        }
    }
}
