package com.app.bombill.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bombill.model.HomePageVendorList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.bombill.R;
import com.app.bombill.Activities.Vendor_Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomePageVendorAdapter  extends RecyclerView.Adapter<HomePageVendorAdapter.ViewHolder> implements Filterable {
    private List<HomePageVendorList> developersLists;
    private List<HomePageVendorList> homePageVendorFullLists;
    private Context context;
    RequestOptions options;

    public static final String KEY_VENDORNAME = "vendorname";
    public static final String KEY_VENDORID = "vendordid";


    public HomePageVendorAdapter(List<HomePageVendorList> developersLists, Context context) {

        // generate constructors to initialise the List and Context objects

        this.developersLists = developersLists;
        this.context = context;
        this.homePageVendorFullLists=new ArrayList<>(developersLists);
        options=new RequestOptions().centerCrop().placeholder(R.drawable.bombill).error(R.drawable.bombill);
    }

    @Override
    public HomePageVendorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method will be called whenever our ViewHolder is created
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page_vendor_list, parent, false);
        return new HomePageVendorAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final HomePageVendorAdapter.ViewHolder holder, final int position) {

        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
        int time;
        final HomePageVendorList developersList = developersLists.get(position);
        holder.Vendor_Name.setText(developersList.getName());
        holder.Vendor_id.setText(developersList.getVendorId());
        holder.Delivery_distance.setText(developersList.getDeliverydistance());
        holder.Min_amount.setText("Minimum Order ₹"+developersList.getMin_amount());
//        holder.Distance_from_user.setText(developersList.getDeliverydistancefromuser()+" km");
         time = developersList.getDeliverydistancefromuser();
        time = (time*60)/10+15;
        holder.Distance_from_user.setText(time+" min");
        Glide.with(context).load(developersLists.get(position).getImage()).apply(options)
                .into(holder.Vendor_Image);

        final int finalTime = time;
        holder.cardVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageVendorList e1 = developersLists.get(position);



               /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putString("gpsLocation", latitude + "," + longitude);
                prefs.edit().putString("address",userAddress).commit();*/

                SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("vendor_name",e1.getName()).commit();

                SharedPreferences sharedPreferences1 =context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("vendor_id",e1.getVendorId()).commit();

                SharedPreferences sharedPreferences2 =context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("delivery_distance",e1.getDeliverydistance()).commit();

                SharedPreferences sharedPreferences3 =context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                editor3.putString("time", String.valueOf(finalTime)).commit();

                SharedPreferences sharedPreferences4 =context.getSharedPreferences(String.valueOf(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                editor3.putString("min_amount",e1.getMin_amount()).commit();



                Intent skipIntent = new Intent(v.getContext(), Vendor_Product.class);

                //  skipIntent.putExtra(KEY_VENDORNAME, e1.getName());
                skipIntent.putExtra(KEY_VENDORID, e1.getVendorId());
                skipIntent.putExtra(KEY_VENDORNAME, e1.getName());
                v.getContext().startActivity(skipIntent);

            }
        });

    }

    @Override

    //return the size of the listItems (developersList)

    public int getItemCount() {
        return developersLists.size();
    }

    Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<HomePageVendorList> filterlist = new ArrayList<>();
            if (constraint.toString().isEmpty()){

                filterlist.addAll(homePageVendorFullLists);
            }else {
              //  String pattrn=constraint.toString().toLowerCase().trim();
                for ( HomePageVendorList item : homePageVendorFullLists){
                  //  if (item.getName().toLowerCase().contains(pattrn)){
                    if (item.toString().contains(constraint.toString().toLowerCase())){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults Results) {
           developersLists.clear();
           developersLists.addAll((Collection<? extends HomePageVendorList>) Results.values);
           notifyDataSetChanged();
        }
    };
    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        // define the View objects

        public ImageView Vendor_Image;
        public TextView Vendor_Name,Vendor_id,Delivery_distance,Distance_from_user,Min_amount;
        CardView cardVendor;
        String id;


        public ViewHolder(View itemView) {
            super(itemView);

            // initialize the View objects

            Vendor_Image = (ImageView) itemView.findViewById(R.id.vendor_image);
            Vendor_Name = (TextView) itemView.findViewById(R.id.vendor_name);
            Vendor_id = (TextView) itemView.findViewById(R.id.vendor_id);
            Delivery_distance = (TextView) itemView.findViewById(R.id.delivery_distance);
            Min_amount = (TextView) itemView.findViewById(R.id.min_amount);
            Distance_from_user = (TextView) itemView.findViewById(R.id.Distance_from_user);
            cardVendor=itemView.findViewById(R.id.cardVendorProduct);



        }
    }
}
