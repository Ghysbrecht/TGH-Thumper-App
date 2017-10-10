package be.thomasghysbrecht.helloworld.thumpercontroller;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Thomas on 11/10/2017.
 */

public interface ThumperService {
    @POST("speed")
    Call<ThumperResponse> setSpeed(@Body ThumperSpeed speed);
}
