package com.app.bombill.model;

/**
 * Created by amolmhatre on 8/11/20
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public GenericResponse() {
    }

    /**
     * @param error
     * @param message
     */
    public GenericResponse(String error, String message) {
        super();
        this.error = error;
        this.message = message;
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

}