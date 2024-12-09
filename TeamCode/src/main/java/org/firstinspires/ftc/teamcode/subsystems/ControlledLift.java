package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Lift;

public class ControlledLift {
    // 0 -> 6.5 in
    // 2172 -> 25.75 in
    // 4139 -> 43 in
    // 4675 -> highest

//    public static final Distance MIN_HEIGHT = Distance.inInches(6.5);
    public static final int MIN_TICKS = 0;
//    public static final Distance MAX_HEIGHT = Distance.inInches(45.3);
    public static final int MAX_TICKS = 4200;
//    public static final Distance DIST_PER_TICK = (MAX_HEIGHT.sub(MIN_HEIGHT)).div(MAX_TICKS - MIN_TICKS);

    private static final double GRAVITY_FEEDFORWARD = 0.09;
    private static final int DELTA = 300;

    private final Lift inner;
    private int current;
    private int leftEncoder;
    private int rightEncoder;

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

        this.current += (dl + dr) / 2;

        if (this.inner.isLeftDown() || this.inner.isRightDown()) {
            this.current = MIN_TICKS + 30;
        }

        double outPower;
        if (override) {
            isGoingToTarget = false;
            outPower = power;
        } else {
            if (this.isGoingToTarget) {
                int error = target - this.current;
                outPower = sigmoidCtrl(error);
                if (Math.abs(error) <= 30) { // TODO: i want this to be 25 but that breaks with the 30 above
                    isGoingToTarget = false;
                    outPower = power;
                }
            } else {
                outPower = power;
            }

            outPower = Range.clip(
                    outPower,
                    sigmoidCtrl(MIN_TICKS - this.current),
                    sigmoidCtrl(MAX_TICKS - this.current)
            );
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
