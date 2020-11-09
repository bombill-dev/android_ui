package com.app.bombill.Fragments.Your_order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bombill.Activities.Order_Details;
import com.app.bombill.R;

import java.util.List;

/**
 * Created by amolmhatre on 7/15/20
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PAYMENT_STATUS = "payment_status";
    public static final String KEY_PURCHES_DATE = "purches_date";
    public static final String KEY_ORDERSTATUS = "order_status";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_GRANDTOTAL = "grand_total";
    Context context;

    private List<OrderModel> orderResponseList;

    public OrderAdapter(List<OrderModel> orderResponseList){
        this.orderResponseList = orderResponseList;
    }



    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_order_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context=view.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, final int position) {

/*Order_status order_status=orderStatuses.get(position);
        holder.status.setText(order_status.getOrder_satus());*/

        holder.Vendor_Name.setText("From:      "+orderResponseList.get(position).getVendor_Name());
        holder.vendor_order_id.setText("Order#    "+orderResponseList.get(position).getVendor_order_id());
        holder.Purchase_date.setText("Date:     "+orderResponseList.get(position).getPurchase_date());
        holder.Payment_method.setText("Payment     :"+orderResponseList.get(position).getPayment_method()+
                "/"+orderResponseList.get(position).getPayment_status());

        if (orderResponseList.get(position).getTransaction_id().equals("0")){
            holder.Transaction_id.setText("Transaction Id: N.A.");
        } else {
            holder.Transaction_id.setText("Transaction Id:    "+orderResponseList.get(position).getTransaction_id());
        }
        if (orderResponseList.get(position).getOrder_status().equals("Completed")||orderResponseList.get(position).getOrder_status().equals("Delivered")){
            holder.Order_status.setTextColor(Color.parseColor("#228B22"));
        } else {
            holder.Order_status.setTextColor(Color.parseColor("#df4344"));
        }

        holder.Order_status.setText(orderResponseList.get(position).getOrder_status());
        holder.Grand_total.setText("Grand Total:    "+orderResponseList.get(position).getGrand_total());


        holder.Order_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Your_Order_List e1 = orderResponseList.get(position);
                Intent skipIntent = new Intent(view.getContext(), Order_Details.class);
                skipIntent.putExtra(KEY_ORDER_ID, orderResponseList.get(position).getVendor_order_id());
                skipIntent.putExtra(KEY_PAYMENT_METHOD, orderResponseList.get(position).getPayment_method());
                skipIntent.putExtra(KEY_ORDERSTATUS, orderResponseList.get(position).getOrder_status());
                skipIntent.putExtra(KEY_PURCHES_DATE, orderResponseList.get(position).getPurchase_date());
                skipIntent.putExtra(KEY_GRANDTOTAL, orderResponseList.get(position).getGrand_total());
                view.getContext().startActivity(skipIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // define the View objects

        public ImageView Fish_Image;
        public TextView vendor_order_id,Purchase_date,Payment_status,Payment_method,Transaction_id,
                Order_status,Grand_total, Vendor_Name;
        Button Customer_Feedback;
        Button Order_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize the View objects

            vendor_order_id = itemView.findViewById(R.id.vendor_order_id);
            Purchase_date = itemView.findViewById(R.id.order_date);
//            Payment_status = itemView.findViewById(R.id.payment_status);
            Payment_method=itemView.findViewById(R.id.payment_method);
            Transaction_id=itemView.findViewById(R.id.transaction_id);
            Order_status=itemView.findViewById(R.id.order_status);
            Grand_total=itemView.findViewById(R.id.tvGrandTotal);
            Vendor_Name=itemView.findViewById(R.id.resto_name);
            Order_card=itemView.findViewById(R.id.order_card);

        }
    }
}
