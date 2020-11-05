package com.app.bombill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amolmhatre on 7/9/20
 */

public class LoginResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginResponse() {
    }

    /**
     *
     * @param error
     * @param message
     * @param user
     */
    public LoginResponse(String error, String message, User user) {
        super();
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

