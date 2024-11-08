package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class Grabber {
    private final SimpleGrabber inner;
    private SimpleGrabber.State target;
    private double current;
    private boolean busy;
    private final double RATE = 1.1; // servo units per second

    public Grabber(Hardware hardware) {
        this(hardware, SimpleGrabber.State.BUCKET);
    }

    public Grabber(Hardware hardware, SimpleGrabber.State initial) {
        this.inner = new SimpleGrabber(hardware);
        this.inner.goTo(initial);
        this.target = initial;
        this.current = initial.pos;
        this.busy = false;
    }

    public void goTo(SimpleGrabber.State state) {
        this.busy = true;
        this.target = state;
        this.inner.goTo(state);
    }

    public void update(double dt) {
        if (!this.busy) return;
        double error = this.target.pos - this.current;
        if (Math.abs(error) <= dt * RATE) {
            busy = false;
            this.current = this.target.pos;
        } else {
            this.current += Math.signum(error) * dt * RATE;
        }
    }

    public boolean isBusy() {
        return this.busy;
    }
}
