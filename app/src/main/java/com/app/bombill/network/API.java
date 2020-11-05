package com.app.bombill.network;


import com.app.bombill.model.GenericResponse;
import com.app.bombill.model.OrderDetailResponse;
import com.app.bombill.model.OrderResponse;
import com.app.bombill.model.CustomerProfile;
import com.app.bombill.model.LoginResponse;
import com.app.bombill.model.TokenModel;
import com.app.bombill.model.VendorProductResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by amolmhatre on 7/9/20
 */
public interface API {

    @FormUrlEncoded
    @POST("customer_login.php")
    Call<LoginResponse> customer_login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("customer_guest_login.php")
    Call<GenericResponse> customer_guest_login(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("token") String token);

    @GET("customer_get_id.php")
    Call<LoginResponse> customer_get_id(
            @Query("email") String email);

    @FormUrlEncoded
    @POST("customer_register.php")
    Call<GenericResponse> customer_register(
            @Field("firstname") String firstname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("password") String password);

    @GET("customer_get_profile.php")
    Call<CustomerProfile> customer_get_profile(
            @Query("customer_id") String customer_id);

    @GET("customer_get_vendor_product.php")
    Call<List<VendorProductResponse>> customer_get_vendor_product(
            @Query("vendor_id") String vendor_id);

    @GET("customer_get_vendor_product1.php")
    Call<List<VendorProductResponse>> customer_get_vendor_product1(
            @Query("vendor_id") String vendor_id,
            @Query("c_id") String category_id);

    @FormUrlEncoded
    @POST("customer_set_profile.php")
    Call<GenericResponse> customer_set_profile(
            @Field("photo") String photo,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("password") String password,
            @Field("customer_id") String customer_id);

    @GET("customer_get_orders_details.php")
    Call<List<OrderDetailResponse>> customer_get_orders_details(
            @Query("order_id") String order_id);

    @FormUrlEncoded
    @POST("customer_create_order.php")
    Call<OrderResponse> customer_create_order(
            @Field("vendor_id") String vendor_id,
            @Field("customer_id") String customer_id,
            @Field("name_of_product") String name_of_product,
            @Field("vendor_product_id") String vendor_product_id,
            @Field("quantity") String quantity,
            @Field("price") String price,
            @Field("total_price") String total_price,
            @Field("service_charge") String service_charge,
            @Field("delivery_charge") String delivery_charge,
            @Field("final_total") String final_total,
            @Field("payment_method") String payment_method,
            @Field("transaction_id") String transaction_id,
            @Field("customer_address") String customer_address,
            @Field("contact") String contact,
            @Field("customer_lat_lon") String customer_lat_lon,
            @Field("email") String email,
            @Field("username") String username,
            @Field("special_instruction") String special_instruction);

    @FormUrlEncoded
    @POST("customer_update_payumoney_status.php")
    Call<ResponseBody> customer_update_payumoney_status(
            @Field("order_id") String order_id,
            @Field("transaction_id") String transaction_id);

    @FormUrlEncoded
    @POST("demo_notification.php")
    Call<TokenModel> sendToken(
            @Field("token") String token);

    @GET("invoice.php")
    Call<ResponseBody> downloadInvoice(
            @Query("id") String id);
}
