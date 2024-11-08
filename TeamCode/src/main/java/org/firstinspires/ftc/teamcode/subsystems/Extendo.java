package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class Extendo {
    private final SimpleExtendo inner;
    private double target;
    private double current;
    private boolean busy;
    private final double RATE = 1.0; // servo units per second

    public Extendo(Hardware hardware) {
        this(hardware, 0.0);
    }

    public Extendo(Hardware hardware, double initial) {
        this.inner = new SimpleExtendo(hardware);
        this.inner.goTo(initial);
        this.target = initial;
        this.current = initial;
        this.busy = false;
    }

    public void goTo(double x) {
        this.busy = true;
        this.target = x;
        this.inner.goTo(x);
    }

    public void update(double dt) {
        if (!this.busy) return;
        double error = this.target - this.current;
        if (Math.abs(error) <= dt * RATE) {
            busy = false;
            this.current = this.target;
        } else {
            this.current += Math.signum(error) * dt * RATE;
        }
    }

    public boolean isBusy() {
        return this.busy;
    }
}
