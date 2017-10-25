package be.thomasghysbrecht.helloworld.thumpercontroller;


public class ThumperNoot {
    private String action;

    public ThumperNoot() {
        this("off");
    }

    public ThumperNoot(String action) {
        this.action = action;
    }
}