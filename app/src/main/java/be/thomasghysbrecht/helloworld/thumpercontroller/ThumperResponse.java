package be.thomasghysbrecht.helloworld.thumpercontroller;

import com.google.gson.annotations.Expose;

/**
 * Created by Thomas on 11/10/2017.
 */

public class ThumperResponse {
    @Expose
    private float battery_voltage;

    public float getBatteryVoltage() {
        return battery_voltage;
    }
}
