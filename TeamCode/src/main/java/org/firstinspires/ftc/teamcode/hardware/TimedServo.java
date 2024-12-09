package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.Sequence;

/**
 * A wrapper over a <code>Servo</code> that uses a timer to keep track of the approximate current
 * position of the servo. This allows client code to wait for the servo to reach its target.
 */
public class TimedServo {
    private final Servo inner;
    private final ServoTimer timer;

    public enum ServoBrand {
//        GOBILDA_TORQUE(1.0),
//        GOBILDA_SPEED(1.0),
//        GOBILDA_SUPERSPEED(1.0),
//        ANIMOS_35KG(1.0),
//        SWYFT(1.0),
        ;

        private final double rate;
        ServoBrand(double rate) { this.rate = rate; }
    }

    public TimedServo(Servo inner, ServoBrand kind, double initial) {
        this(inner, kind.rate, initial);
    }

    public TimedServo(Servo inner, double rate, double initial) {
        this(inner, rate, initial, 0, 1);
    }

    public TimedServo(Servo inner, ServoBrand kind, double initial, double min, double max) {
        this(inner, kind.rate, initial, min, max);
    }

    public TimedServo(Servo inner, double rate, double initial, double min, double max) {
        scaleServo(inner, min, max);
        this.inner = inner;
        this.timer = new ServoTimer(rate / Math.abs(max - min), initial);

        this.setPosition(initial);
    }

    private static void scaleServo(Servo servo, double left, double right) {
        if (left <= right) {
            servo.scaleRange(left, right);
            servo.setDirection(Servo.Direction.FORWARD);
        } else {
            servo.scaleRange(right, left);
            servo.setDirection(Servo.Direction.REVERSE);
        }
    }

    public void setPosition(double pos) {
        this.timer.setTarget(pos);
        this.inner.setPosition(pos);
    }

    public double getPosition() {
        return this.timer.getCurrent();
    }

    @CheckResult(suggest = "setPosition(double)")
    public Action goTo(double pos) {
        return Sequence.of(
                Action.fromFn(() -> this.setPosition(pos)),
                this.timer.await()
        );
    }

    public void update(double dt) {
        this.timer.update(dt);
    }

    public boolean isBusy() {
        return this.timer.isBusy();
    }

    public static class Pair {
        private final Servo left, right;
        private final ServoTimer timer;

        public Pair(
                Servo left,
                Servo right,
                TimedServo.ServoBrand kind,
                double initial,
                double minL, double maxL,
                double minR, double maxR
        ) {
            this(left, right, kind.rate, initial, minL, maxL, minR, maxR);
        }

        public Pair(
                Servo left,
                Servo right,
                double rate,
                double initial,
                double minL, double maxL,
                double minR, double maxR
        ) {
            assert Math.abs(maxL - minL) == Math.abs(maxR - minR);
            scaleServo(left, minL, maxL);
            scaleServo(right, minR, maxR);
            this.left = left;
            this.right = right;
            this.timer = new ServoTimer(rate / Math.abs(maxL - minL), initial);

            this.setPosition(initial);
        }

        public void setPosition(double pos) {
            this.timer.setTarget(pos);
            this.left.setPosition(pos);
            this.right.setPosition(pos);
        }

        public double getPosition() {
            return this.timer.getCurrent();
        }

        @CheckResult(suggest = "setPosition(double)")
        public Action goTo(double pos) {
            return Sequence.of(
                    Action.fromFn(() -> this.setPosition(pos)),
                    this.timer.await()
            );
        }

        public void update(double dt) {
            this.timer.update(dt);
        }

        public boolean isBusy() {
            return this.timer.isBusy();
        }
    }
}
