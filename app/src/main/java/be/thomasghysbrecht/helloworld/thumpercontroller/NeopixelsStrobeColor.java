package be.thomasghysbrecht.helloworld.thumpercontroller;

import com.google.gson.annotations.Expose;

/**
 * Created by Thomas on 4/10/2017.
 */

public class NeopixelsStrobeColor {

    @Expose
    private int red;

    @Expose
    private int blue;

    @Expose
    private int green;

    @Expose
    private int delay;

    NeopixelsStrobeColor(int red, int green, int blue, int delay){
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.delay = delay;
    }

    NeopixelsStrobeColor(){
        this(0,0,0,100);
    }
}
