package be.thomasghysbrecht.helloworld.thumpercontroller;

import com.google.gson.annotations.Expose;

/**
 * Created by Thomas on 4/10/2017.
 */

public class Response {

    @Expose
    private String status;

    public String getStatus(){
        return status;
    }

}
