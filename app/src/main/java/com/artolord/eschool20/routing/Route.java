package com.artolord.eschool20.routing;


import android.content.SharedPreferences;
import android.util.Log;

import com.artolord.eschool20.routing.Routing_classes.State;

import okhttp3.*;
import org.json.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

public class Route {
    private Retrofit retrofit;
    private MainRoutingInterface Api;
    SharedPreferences sharedPreferences;

    public Route(){
        CookieHandler cookieHandler = new CookieManager();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        Api = retrofit.create(MainRoutingInterface.class);
    }


    public void login(String login, String password, final CallBackInterface callback ){

        Call<ResponseBody> Login = Api.login(new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Constants.username, login)
                .addFormDataPart(Constants.password, password)
                .build());
        Log.e("",password);

        //Login request
        Login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               Headers hed =  response.headers();
               int i = 0;
               List<String> cookies = hed.values("Set-Cookie");
               //SharedPreferences.Editor editor = sharedPreferences.edit();
               String cookieString = "";
               for (String c:cookies) {
                    cookieString+=c.split(";")[0]+"; ";
               }
                Log.e("",cookieString);
               //editor.putString("cookie",cookieString);
               //editor.apply();
               State user = state(cookieString,callback);




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t);

            }




        });

    }
    private State state(String cookies, final CallBackInterface callback){
        final State userState = new State();
        Call<ResponseBody> getstate  = Api.state(cookies);
        getstate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int userId = json.getInt("userId");
                    JSONObject user = json.getJSONObject("user");
                    //Log.e("",user.toString());
                    userState.setPrsFio(user.getJSONObject("currentPosition").getString("prsFio"));
                    userState.setUsername(user.getString("username"));
                    userState.setUserId(userId);
                    callback.callback(userState);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return userState;
    }
}
