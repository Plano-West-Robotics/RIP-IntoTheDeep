package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Lift;

public class ControlledLift {
    public static final int MIN_TICKS = 0;
    public static final int MAX_TICKS = 3000;

    public static final int HIGH_CHAMBER = 1075;
    public static final int LOW_BASKET = 900;
    public static final int HIGH_BASKET = 2350;
    public static final int LOW_RUNG = 1200;
    public static final int HIGH_RUNG = 2500;

    private static final double GRAVITY_FEEDFORWARD = 0.07;
    private static final int DELTA = 300;

    private final Lift inner;
    private int current;
    private int leftEncoder;
    private int rightEncoder;
    private boolean downLastUpdate;

    private double power;
    private boolean override;
    private boolean isGoingToTarget;
    private int target;

    public ControlledLift(Hardware hardware) {
        this(hardware.lift);
    }

    public ControlledLift(Hardware hardware, int initial) {
        this(hardware.lift, initial);
    }

    public ControlledLift(Lift inner) {
        this(inner, 0);
    }

    public ControlledLift(Lift inner, int initial) {
        this.inner = inner;
        this.current = initial;
        this.leftEncoder = this.inner.leftEncoder();
        this.rightEncoder = this.inner.rightEncoder();
        this.downLastUpdate = this.inner.isLeftDown() || this.inner.isRightDown();

        this.power = 0;
        this.override = false;
        this.isGoingToTarget = false;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public void setTarget(int target) {
        this.isGoingToTarget = true;
        this.target = target;
    }

    public void update(double dt) {
        int newLeftEncoder = this.inner.leftEncoder();
        int newRightEncoder = this.inner.rightEncoder();
        int dl = newLeftEncoder - this.leftEncoder;
        int dr = newRightEncoder - this.rightEncoder;
        this.leftEncoder = newLeftEncoder;
        this.rightEncoder = newRightEncoder;

        boolean down = this.inner.isLeftDown() || this.inner.isRightDown();
        if (down && !this.downLastUpdate) this.current = MIN_TICKS + 20;
        if (!down && this.downLastUpdate) this.current = MIN_TICKS + 30;
        this.downLastUpdate = down;

        this.current += (dl + dr) / 2;
        this.current = Math.max(this.current, 0);
        if (down) {
            this.current = Math.min(this.current, 30);
        } else {
            this.current = Math.max(this.current, 30);
        }

        double outPower;
        if (override) {
            isGoingToTarget = false;
            outPower = power;
        } else {
            if (this.isGoingToTarget) {
                int error = target - this.current;
                outPower = sigmoidCtrl(error);
                if (this.target == MIN_TICKS) {
                    outPower = Math.min(outPower, -0.32);
                }

                if (Math.abs(error) <= 25) {
                    isGoingToTarget = false;
                    outPower = power;
                }
            } else {
                outPower = Range.clip(
                        power,
                        sigmoidCtrl(MIN_TICKS - this.current),
                        sigmoidCtrl(MAX_TICKS - this.current)
                );

                if (power <= 0.0 && this.current <= 30) {
                    outPower = Math.min(outPower, -0.32);
                }
            }
        }

        this.inner.setPower(outPower + GRAVITY_FEEDFORWARD);
    }

    private double sigmoidCtrl(double error) {
        return Math.tanh(error / DELTA);
//        error /= DELTA;
//        double exp = Math.exp(2 * error);
//        return (exp - 1) * (exp + 1);
    }

    public int getCurrentPos() {
        return this.current;
    }

    public boolean isBusy() {
        return this.isGoingToTarget;
    }
}
