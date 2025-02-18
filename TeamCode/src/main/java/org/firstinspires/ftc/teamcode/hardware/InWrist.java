package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class InWrist extends TimedServo.Pair {
    public enum State {
        IN(1.0), TRANSFER(0.79), UP(0.42), DOWN(0.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public InWrist(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        //           down - up
        // inWristL  0.05 - 1.00
        // inWristR  0.95 - 0.00
        super(
                raw.inWristL,
                raw.inWristR,
                0.4/(0.40),
                initial.branch(State.IN.pos, State.TRANSFER.pos, State.IN.pos),
                0.05, 1.00,
                0.95, 0.00
        );
    }

    public void setPosition(State pos) {
        this.setPosition(pos.pos);
    }

    @CheckResult(suggest = "setPosition(State)")
    public Action goTo(State pos) {
        return this.goTo(pos.pos);
    }
}
