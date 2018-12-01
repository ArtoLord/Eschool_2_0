package com.artolord.eschool20.routing;


import android.util.Log;

import com.artolord.eschool20.routing.Interfaces.GetMarksCallback;
import com.artolord.eschool20.routing.Interfaces.GetPeriodCallback;
import com.artolord.eschool20.routing.Interfaces.LoginCallback;
import com.artolord.eschool20.routing.Interfaces.MainRoutingInterface;
import com.artolord.eschool20.routing.Routing_classes.Period;
import com.artolord.eschool20.routing.Routing_classes.State;
import com.artolord.eschool20.routing.Routing_classes.Unit;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.json.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private Retrofit retrofit;
    private MainRoutingInterface Api;
    private String cookie;
    private State UserState;

    public Route(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.url) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        Api = retrofit.create(MainRoutingInterface.class);
    }


    public void login(String login, String password, final LoginCallback callback ){

        Call<ResponseBody> Login = Api.login(new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Constants.username, login)
                .addFormDataPart(Constants.password, password)
                .build());
        //Log.e("",password);

        //Login request
        Login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               Headers hed =  response.headers();
               int i = 0;
               List<String> cookies = hed.values("Set-Cookie");
               String cookieString = "";
               for (String c:cookies) {
                    cookieString+=c.split(";")[0]+"; ";
               }
                //Log.e("",cookieString);
               cookie = cookieString;
               state(cookie,callback);




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //System.out.println(t);

            }




        });

    }
    private void state(String cookies, final LoginCallback callback){
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
    }
    public void getPeriods(int year, final GetPeriodCallback callback){
        Call<ResponseBody> getperiod  = Api.getPeriod(cookie,year);

        getperiod.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ArrayList<Period> callbacklist= new ArrayList<>();
                try {
                    JSONArray json = new JSONArray(response.body().string());
                    json = json.getJSONObject(0).getJSONArray("items");
                    //Log.e("",json.toString());
                    for (int i = 0;i<json.length();i++){
                        Period period = new Period();
                        period.periodId = json.getJSONObject(i).getInt("id");
                        period.isStudy = json.getJSONObject(i).getBoolean("study");
                        period.periodName = json.getJSONObject(i).getString("name");
                        callbacklist.add(period);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.callback(callbacklist);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getMarks(int userid, int periodId, final GetMarksCallback callback){
        Call<ResponseBody> getperiod  = Api.getMarcs(cookie,userid,periodId);
        getperiod.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ArrayList<Unit> list  = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.e("",jsonObject.toString());
                    JSONArray array = jsonObject.getJSONArray("result");
                    for(int i = 0;i<array.length();i++){
                        Unit unit = new Unit();
                        unit.overMark = array.getJSONObject(i).getDouble("overMark");
                        unit.rating = array.getJSONObject(i).getString("rating");
                        unit.unitName = array.getJSONObject(i).getString("unitName");
                        unit.totalmark = array.getJSONObject(i).getString("totalMark");
                        list.add(unit);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.callback(list);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
