package org.firstinspires.ftc.teamcode.util;

public class ServoTimer {
    private double current;
    private double target;
    private final double rate;

    public ServoTimer(double current, double rate) {
        this.current = current;
        this.target = this.current;
        this.rate = rate;
    }

    public void goTo(double target) {
        this.target = target;
    }

    public void update(double dt) {
        double dx = dt * this.rate;
        double error = this.target - this.current;
        if (Math.abs(error) <= dx) {
            this.current = this.target;
        } else {
            this.current += dx * Math.signum(error);
        }
    }

    public boolean isBusy() {
        return this.current != this.target;
    }
}
