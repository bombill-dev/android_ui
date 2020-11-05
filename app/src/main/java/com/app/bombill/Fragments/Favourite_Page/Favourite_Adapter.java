package com.app.bombill.Fragments.Favourite_Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.bombill.R;

import java.util.List;

public class Favourite_Adapter extends RecyclerView.Adapter<Favourite_Adapter.ViewHolder> {
    private List<Favourite_List> developersLists;
    private Context context;
    RequestOptions options;

    public static final String KEY_VENDORNAME = "vendorname";
    public static final String KEY_VENDORID = "vendordid";

    public Favourite_Adapter(List<Favourite_List> developersLists, Context context) {

        // generate constructors to initialise the List and Context objects

        this.developersLists = developersLists;
        this.context = context;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.bombill).error(R.drawable.bombill);
    }

    @Override
    public Favourite_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(Favourite_Adapter.ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views

        final Favourite_List developersList = developersLists.get(position);
        holder.Vendor_Producr_id.setText(developersList.getVendorid_Product_Id());
        holder.Fish_Name.setText(developersList.getFishName());
        holder.Fish_Price.setText(developersList.getFishPrice());
        holder.Unit.setText(developersList.getFishUnit());
        holder.Description.setText(developersList.getDescription());


        Glide.with(context).load(developersLists.get(position).getFishImage()).apply(options)
                .into(holder.Fish_Image);


    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return developersLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // define the View objects

        public ImageView Fish_Image;
        public TextView Fish_Name, Vendor_Producr_id, Fish_Price, Unit, Description;
        Button Vendor_Product_Cart;


        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects

            Vendor_Producr_id = itemView.findViewById(R.id.vendor_id);
            Fish_Name = itemView.findViewById(R.id.fish_name);
            Fish_Price = itemView.findViewById(R.id.fish_price);
            Unit=itemView.findViewById(R.id.unit);
            Fish_Image = itemView.findViewById(R.id.fish_image);
            Description = itemView.findViewById(R.id.product_description);


        }
    }
}