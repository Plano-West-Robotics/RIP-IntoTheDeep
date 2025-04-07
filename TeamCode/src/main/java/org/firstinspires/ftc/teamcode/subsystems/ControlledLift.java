package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Lift;
import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.macro.Sequence;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

public class ControlledLift {
    public static final int MIN_TICKS = 0;
    public static final int MAX_TICKS = 51000;

    public static final int CHAMBER_BACK = 11000; // TOOD:
    public static final int CHAMBER_FRONT = 11000;
    public static final int BASKET_BACK = 51000;
    public static final int BASKET_FRONT = 51000;

    private static final double GRAVITY_FEEDFORWARD = 0.10;
    private static final int DELTA = 2900;

    private final Lift inner;
    private int current;
    private int encoder;
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
        this.encoder = this.inner.encoder();
        this.downLastUpdate = this.inner.isDown();

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

    public void clearTarget() {
        this.isGoingToTarget = false;
    }

    public Action goTo(int target) {
        return Sequence.of(
                Action.fromFn(() -> this.setTarget(target)),
                new Action() {
                    DeltaTimer dter = new DeltaTimer(true);

                    public ControlFlow update() {
                        ControlledLift.this.update(dter.poll());
                        return ControlFlow.continueIf(ControlledLift.this.isBusy());
                    }

                    public void end() {}
                }
        );
    }

    public void update(double dt) {
        int newEncoder = this.inner.encoder();
        int dx = newEncoder - this.encoder;
        this.encoder = newEncoder;

        boolean down = this.inner.isDown();
        if (down && !this.downLastUpdate) this.current = MIN_TICKS + 600;
        if (!down && this.downLastUpdate) this.current = MIN_TICKS + 800;
        this.downLastUpdate = down;

        this.current += dx;
        this.current = Math.max(this.current, 0);
        if (down) {
            this.current = Math.min(this.current, 800);
        } else {
            this.current = Math.max(this.current, 800);
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
                    outPower = Math.min(outPower, -0.45);
                    if (this.current <= 3000 && !down) outPower = -1;
                }

                if (Math.abs(error) <= 250 || (target <= 800 && down)) { // TODO: second condition is hacky
                    isGoingToTarget = false;
                    outPower = power;
                }
            } else {
                outPower = Range.clip(
                        power,
                        sigmoidCtrl(MIN_TICKS - this.current),
                        sigmoidCtrl(MAX_TICKS - this.current)
                );

                if (power <= 0.0 && this.current <= 3000) {
                    if (!down) outPower = -1;
                    else outPower = Math.min(outPower, -0.3);
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
