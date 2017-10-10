package be.thomasghysbrecht.helloworld.thumpercontroller;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface NeopixelService {
    @POST("neopixels/strings/{id}")
    Call<Response> sendBasicColor(@Path("id") String id, @Body NeopixelsBasicColor pixels);

    @POST("neopixels/effects/strobe/{id}")
    Call<Response> sendStrobeColor(@Path("id") String id, @Body NeopixelsStrobeColor pixels);

}
