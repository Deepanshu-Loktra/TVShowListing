package com.loktra.tvshowapp.repository.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Generic server response
 */
public class GenericResponse<T> {

    @SerializedName("status")
    public int status;

    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public T data;

}
