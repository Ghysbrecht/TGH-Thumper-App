package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thomas on 4/10/2017.
 */

public class NeopixelsController {

    private Retrofit retrofit;
    private NeopixelService neopixelService;

    public NeopixelsController(String address){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        neopixelService = retrofit.create(NeopixelService.class);
    }

    public void setColor(int red, int green, int blue, String id){
        Call<Response> response = neopixelService.sendBasicColor(id, new NeopixelsBasicColor(red,green,blue));
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.body() != null){
                    Log.e("THUMP","It did work, here is the info: " + response.body().getStatus() );

                }
                else Log.e("THUMP", "No body :(");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("THUMP", "Network error?");
            }
        });
    }

    public void setStrobeColor(int red, int green, int blue, int delay,  String id){
        Call<Response> response = neopixelService.sendStrobeColor(id, new NeopixelsStrobeColor(red,green,blue,delay));
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.body() != null){
                    Log.e("THUMP","It did work, here is the info: " + response.body().getStatus() );

                }
                else Log.e("THUMP", "No body :(");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("THUMP", "Network error?");
            }
        });
    }



}
