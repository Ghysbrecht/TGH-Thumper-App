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

    private int MAX_SPEED = 100;
    private int MIN_SPEED = 30; //NOT YET USED

    private Retrofit retrofit;
    private ThumperService thumperService;

    private float thumpVoltage;

    public ThumperController(String address, int max){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MAX_SPEED = max;
        thumperService = retrofit.create(ThumperService.class);
    }

    public void sendSpeed(int left, int right){
        Call<ThumperResponse> response = thumperService.setSpeed(new ThumperSpeed(right, left));
        response.enqueue(new Callback<ThumperResponse>() {
            public void onResponse(Call<ThumperResponse> call, retrofit2.Response<ThumperResponse> response) {
                if(response.body() != null){
                    Log.e("THUMP","It did work, here is the info: " + response.body().getBatteryVoltage() );
                    if(response.body().getBatteryVoltage() != 0.0) thumpVoltage = response.body().getBatteryVoltage();
                }
                else Log.e("THUMP", "No body :(");
            }

            @Override
            public void onFailure(Call<ThumperResponse> call, Throwable t) {
                Log.e("THUMP", "Network error?");
            }
        });
    }

    public void sendNoot(String action){
        Call<Response> response = thumperService.setBuzzer(new ThumperNoot(action));
        response.enqueue(new Callback<Response>() {
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

    public void goLeft(){
        sendSpeed(-MAX_SPEED, MAX_SPEED);
    }

    public void goRight(){
        sendSpeed(MAX_SPEED, -MAX_SPEED);
    }

    public void driveForward(){
        sendSpeed(MAX_SPEED, MAX_SPEED);
    }

    public void driveBackward(){ sendSpeed(-MAX_SPEED, -MAX_SPEED);
    }
    public void stop(){
        sendSpeed(0,0);
    }

    public float getThumpVoltage(){return thumpVoltage;}

    public void buzzerOn(){ sendNoot("on");}
    public void buzzerOff(){ sendNoot("off");}
    public void buzzerToggle(){ sendNoot("toggle");}

    public void drivePercentages(float left, float right){
        int intleft = (int) (left * MAX_SPEED);
        int intright = (int) (right * MAX_SPEED);
        sendSpeed(intleft, intright);
    }
}
