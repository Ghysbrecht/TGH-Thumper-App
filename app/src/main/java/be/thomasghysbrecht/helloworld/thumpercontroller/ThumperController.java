package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thomas on 11/10/2017.
 */


public class ThumperController {

    private int MAX_SPEED = 50;

    private Retrofit retrofit;
    private ThumperService thumperService;

    public ThumperController(String address){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        thumperService = retrofit.create(ThumperService.class);
    }

    public void sendSpeed(int left, int right){
        Call<ThumperResponse> response = thumperService.setSpeed(new ThumperSpeed(left, right));
        response.enqueue(new Callback<ThumperResponse>() {
            public void onResponse(Call<ThumperResponse> call, retrofit2.Response<ThumperResponse> response) {
                if(response.body() != null){
                    Log.e("THUMP","It did work, here is the info: " + response.body().getBatteryVoltage() );
                }
                else Log.e("THUMP", "No body :(");
            }

            @Override
            public void onFailure(Call<ThumperResponse> call, Throwable t) {
                Log.e("THUMP", "Network error?");
            }
        });
    }

    public void goLeft(){
        sendSpeed(-MAX_SPEED, MAX_SPEED);
    }

    public void goRight(){
        sendSpeed(MAX_SPEED, -MAX_SPEED);
    }

    public void driveForward(){
        sendSpeed(MAX_SPEED, MAX_SPEED);
    }

    public void driveBackward(){
        sendSpeed(MAX_SPEED, MAX_SPEED);
    }
    public void stop(){
        sendSpeed(0,0);
    }
}
