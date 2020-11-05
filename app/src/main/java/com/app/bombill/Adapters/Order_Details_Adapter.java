package com.app.bombill.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.bombill.model.OrderDetailResponse;
import com.bumptech.glide.request.RequestOptions;
import com.app.bombill.R;

import java.util.List;

public class Order_Details_Adapter  extends RecyclerView.Adapter<Order_Details_Adapter .ViewHolder> {
    private List<OrderDetailResponse> order_detailResponses;
    private Context context;
    RequestOptions options;


    public Order_Details_Adapter(List<OrderDetailResponse> developersLists, Context context) {

        // generate constructors to initialise the List and Context objects
        this.order_detailResponses = developersLists;
        this.context = context;
//        options = new RequestOptions().centerCrop().placeholder(R.drawable.bombillogo5).error(R.drawable.bombillogo5);
    }


    @Override
    public Order_Details_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_list, parent, false);
        return new Order_Details_Adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(Order_Details_Adapter.ViewHolder holder, final int position) {
        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
        final OrderDetailResponse orderDetailsList = order_detailResponses.get(position);
        holder.Name_Of_Product.setText(orderDetailsList.getNameOfProduct());
        holder.Product_quantity.setText("X "+orderDetailsList.getQuantity());
        holder.Price.setText("price : "+orderDetailsList.getPrice());
        // currrently getTotal_price is returning zero
//        holder.Total_Price.setText("Total price : "+orderDetailsList.getTotal_price());
        holder.Total_Price.setText("Total price : "+Float.valueOf(orderDetailsList.getQuantity())*Float.valueOf(orderDetailsList.getPrice()));
    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return order_detailResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public ImageView Fish_Image;
        public TextView Name_Of_Product,Product_quantity,Price,Total_Price;
        Button Customer_Feedback;


        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects

            Name_Of_Product = itemView.findViewById(R.id.name_of_product);
            Product_quantity = itemView.findViewById(R.id.tvProductQuantity);
            Price = itemView.findViewById(R.id.tvProductPrice);
            Total_Price=itemView.findViewById(R.id.tvProductTotal);



        }
    }
}