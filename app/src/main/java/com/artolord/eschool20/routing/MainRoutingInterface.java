package com.artolord.eschool20.routing;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface MainRoutingInterface {
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody body);

    @GET("state")
    Call<ResponseBody> state(@Header("Cookie") String cookie);
}
