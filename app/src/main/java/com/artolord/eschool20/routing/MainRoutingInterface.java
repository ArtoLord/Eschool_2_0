package com.artolord.eschool20.routing;


import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MainRoutingInterface {

    @Multipart
    @POST("login")
    Call<String> login(@Part(Constants.username) String username, @Part(Constants.password) String password);
}
