package be.thomasghysbrecht.helloworld.thumpercontroller;

import com.google.gson.annotations.Expose;

/**
 * Created by Thomas on 4/10/2017.
 */

public class NeopixelsBasicColor {

    @Expose
    private int red;

    @Expose
    private int blue;

    @Expose
    private int green;


    NeopixelsBasicColor(int red, int green, int blue){
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    NeopixelsBasicColor(){
        this(0,0,0);
    }

}
