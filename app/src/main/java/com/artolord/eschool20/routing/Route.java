package com.artolord.eschool20.routing;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.CookieHandler;
import java.net.CookieManager;

public class Route {
    private Retrofit retrofit;
    private MainRoutingInterface Api;

    public Route(){
        CookieHandler cookieHandler = new CookieManager();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        Api = retrofit.create(MainRoutingInterface.class);
    }


    public void login(String login, String password){
        Call<String> Login = Api.login(login,password);
        Login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
