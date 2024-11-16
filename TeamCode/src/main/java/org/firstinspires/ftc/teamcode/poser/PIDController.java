package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

public class PIDController {
    public double kp;
    public double ki;
    public double kd;

    private double integral;
    private double lastError;
    private double lastUpdate;
    private boolean firstIteration;

    public PIDController(double p, double i, double d) {
        this.kp = p;
        this.ki = i;
        this.kd = d;

        this.reset();
    }

    public void reset() {
        this.firstIteration = true;
    }

    public Distance update(Distance error) {
        return Distance.inDefaultUnits(this.update(error.valInDefaultUnits()));
    }

    public Angle update(Angle error) {
        return Angle.inDefaultUnits(this.update(error.valInDefaultUnits()));
    }

    public double update(double error) {
        double now = System.nanoTime() / (1000 * 1000 * 1000.);

        double p = this.kp * error;
        double i, d;

        if (this.firstIteration) {
            this.firstIteration = false;
            this.integral = 0;

            i = 0;
            d = 0;
        } else {
            double dt = now - this.lastUpdate;
            this.integral += error * dt;
            i = this.ki * this.integral;
            d = this.kd * (error - this.lastError) / dt;
        }

        this.lastUpdate = now;
        this.lastError = error;

        return p + i + d;
    }
}
