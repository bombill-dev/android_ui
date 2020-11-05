package com.app.bombill.model;

/**
 * Created by amolmhatre on 8/11/20
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerProfile {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cust_profile")
    @Expose
    private String custProfile;

    @SerializedName("password")
    @Expose
    private String Password;

    /**
     * No args constructor for use in serialization
     *
     */
    public CustomerProfile() {
    }

    /**
     *
     * @param address
     * @param name
     * @param custProfile
     * @param email
     * @param contactNo
     */
    public CustomerProfile(String name, String email, String contactNo, String address, String custProfile,String password) {
        super();
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.address = address;
        this.custProfile = custProfile;
        this.Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustProfile() {
        return custProfile;
    }

    public void setCustProfile(String custProfile) {
        this.custProfile = custProfile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
