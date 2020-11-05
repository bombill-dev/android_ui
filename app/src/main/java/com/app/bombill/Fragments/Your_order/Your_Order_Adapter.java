package com.app.bombill.Fragments.Your_order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.RequestOptions;
import com.app.bombill.Activities.Order_Details;
import com.app.bombill.R;

import java.util.List;


public class Your_Order_Adapter extends RecyclerView.Adapter<Your_Order_Adapter.ViewHolder> {
    private List<Your_Order_List> developersLists;
    private Context context;
    RequestOptions options;

    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PAYMENT_STATUS = "payment_status";
    public static final String KEY_PURCHES_DATE = "purches_date";
    public static final String KEY_PAYMENT_METHOD = "payment_method";

    public Your_Order_Adapter(List<Your_Order_List> developersLists, Context context) {

        // generate constructors to initialise the List and Context objects
        this.developersLists = developersLists;
        this.context = context;
//        options = new RequestOptions().centerCrop().placeholder(R.drawable.bombillogo5).error(R.drawable.bombillogo5);
    }


    @Override
    public Your_Order_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_order_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(Your_Order_Adapter.ViewHolder holder, final int position) {
        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
        final Your_Order_List developersList = developersLists.get(position);
        holder.Vendor_Name.setText(developersList.getVendor_Name());
        holder.vendor_order_id.setText(developersList.getVendor_Order_Id());
        holder.Purchase_date.setText(developersList.getOrder_date());
        holder.Payment_status.setText(developersList.getPayment_status());
        holder.Payment_method.setText(developersList.getPayment_method());
        holder.Transaction_id.setText(developersList.getTransaction_id());
        holder.Order_status.setText(developersList.getOrder_status());
        holder.Grand_total.setText(developersList.getGrand_total());
       /* Glide.with(context).load(developersLists.get(position).getFishImage()).apply(options)
                .into(holder.Fish_Image);
*/
holder.Order_card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Your_Order_List e1 = developersLists.get(position);
        Intent skipIntent = new Intent(view.getContext(), Order_Details.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //  skipIntent.putExtra(KEY_VENDORNAME, e1.getName());
        skipIntent.putExtra(KEY_ORDER_ID, e1.getVendor_Order_Id());
        skipIntent.putExtra(KEY_PAYMENT_METHOD, e1.getPayment_method());
        skipIntent.putExtra(KEY_PAYMENT_STATUS, e1.getOrder_status());
        skipIntent.putExtra(KEY_PURCHES_DATE, e1.getOrder_date());
        view.getContext().startActivity(skipIntent);
    }
});
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Your_Order_List e1 = developersLists.get(position);
                Intent skipIntent = new Intent(view.getContext(), Order_Details.class);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                //  skipIntent.putExtra(KEY_VENDORNAME, e1.getName());
                skipIntent.putExtra(KEY_ORDER_ID, e1.getVendor_Order_Id());
                skipIntent.putExtra(KEY_PAYMENT_METHOD, e1.getPayment_method());
                skipIntent.putExtra(KEY_PAYMENT_STATUS, e1.getOrder_status());
                skipIntent.putExtra(KEY_PURCHES_DATE, e1.getOrder_date());
                view.getContext().startActivity(skipIntent);
            }
        });
    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return developersLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public ImageView Fish_Image;
        public TextView vendor_order_id,Purchase_date,Payment_status,Payment_method,Transaction_id,
                Order_status,Grand_total, Vendor_Name;
        Button Customer_Feedback;
        CardView Order_card;
CardView cardView;

        public ViewHolder(View itemView) {
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
            cardView=itemView.findViewById(R.id.order_card);

            //  Fish_Image = itemView.findViewById(R.id.fish_image);
        }
    }
}