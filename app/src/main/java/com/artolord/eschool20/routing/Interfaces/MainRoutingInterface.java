package com.artolord.eschool20.routing.Interfaces;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface MainRoutingInterface {
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody body);

    @GET("state")
    Call<ResponseBody> state(@Header("Cookie") String cookie);

    @GET("dict/periods2")
    Call<ResponseBody> getPeriod(@Header("Cookie") String cookie,@Query("year")int year);

    @GET("student/getDiaryUnits/?")
    //https://app.eschool.center/ec-server/student/getDiaryUnits/?userId=62447&eiId=97927
    Call<ResponseBody> getMarcs(@Header("Cookie") String cookie,@Query("userId")int userId,@Query("eiId")int eiId);
}
