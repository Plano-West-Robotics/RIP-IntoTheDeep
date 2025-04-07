package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.LiftPivot;
import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.macro.Sequence;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

public class PivotControl {
    public static final Angle MIN = Angle.inDegrees(-9.4);
    public static final Angle MAX = Angle.inDegrees(40.5);

    public static final int MIN_TICKS = 0;
    public static final int MAX_TICKS = LiftPivot.RANGE;
    private static final Angle ANGLE_PER_TICK = MAX.sub(MIN).div(LiftPivot.RANGE);

    private static final int THRESHOLD = 40;
    private static final int DELTA = 150;

    private final LiftPivot inner;
    public int current;
    private int target;
    private boolean atTarget;

    private int encoder;

    public PivotControl(Hardware hardware) {
        this(hardware.pivot);
    }

    public PivotControl(Hardware hardware, Angle initial) {
        this(hardware.pivot, initial);
    }

    public PivotControl(LiftPivot inner) {
        this(inner, MIN);
    }

    public PivotControl(LiftPivot inner, Angle initial) {
        this.inner = inner;

        this.current = this.target = angleToTicks(initial);
        this.atTarget = true;

        this.encoder = this.inner.encoder();
    }

    private int angleToTicks(Angle angle) {
        return (int)angle.sub(MIN).div(ANGLE_PER_TICK);
    }

//    private Angle ticksToAngle(int ticks) {
//        return ANGLE_PER_TICK.mul(ticks).add(MIN);
//    }

    public void setTarget(int target) {
        target = Range.clip(target, MIN_TICKS, MAX_TICKS); // safety precaution

        this.target = target;
        if (Math.abs(this.target - this.current) > THRESHOLD) this.atTarget = false;
    }

    public void setTarget(Angle target) {
        this.setTarget(angleToTicks(target));
    }

    public Action goTo(int pos) {
        return Sequence.of(
                Action.fromFn(() -> this.setTarget(target)),
                new Action() {
                    DeltaTimer dter = new DeltaTimer(true);

                    public ControlFlow update() {
                        PivotControl.this.update(dter.poll());
                        return ControlFlow.continueIf(PivotControl.this.isBusy());
                    }

                    public void end() {
                        PivotControl.this.stop();
                    }
                }
        );
    }

    public void update(double dt) {
        int newEncoder = this.inner.encoder();
        int dx = newEncoder - this.encoder;
        this.encoder = newEncoder;

        this.current += dx;

        int error = this.target - this.current;
        double pow = Math.tanh((double)error / DELTA);

        if (Math.abs(error) <= THRESHOLD) this.atTarget = true;

        this.inner.setPower(pow);
    }

    public void stop() {
        this.inner.setPower(0);
    }

    public boolean isBusy() {
        return !this.atTarget;
    }
}
