package be.thomasghysbrecht.helloworld.thumpercontroller;

/**
 * Created by Thomas on 10/10/2017.
 */

public class ThumperSpeed {
    private int left_speed;
    private int right_speed;

    public ThumperSpeed() {
        this(0, 0);
    }

    public ThumperSpeed(int left_speed, int right_speed) {
        this.left_speed = left_speed;
        this.right_speed = right_speed;
    }
}