package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

public class ServoTimer {
    private final double rate;
    private double current;
    private double target;

    public ServoTimer(double rate, double initial) {
        this.rate = rate;
        this.target = this.current = initial;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getCurrent() {
        return current;
    }

    public Action await() {
        return new Action() {
            // TODO: thread dt through Action
            final DeltaTimer dter = new DeltaTimer();

            public ControlFlow update() {
                ServoTimer self = ServoTimer.this;
                self.update(dter.poll());
                return ControlFlow.continueIf(self.isBusy());
            }

            @Override
            public void end() {}
        };
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
