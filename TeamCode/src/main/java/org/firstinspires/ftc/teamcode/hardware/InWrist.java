package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class InWrist extends TimedServo.Pair {
    public enum State {
        IN(1.0), TRANSFER(0.84), UP(0.39), DOWN(0.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public InWrist(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-04-22
        //          inWristL/inWristR
        // down:        0.04/0.90
        // up:          0.35/0.59
        // in:          0.84/0.10
        super(
                raw.inWristL,
                raw.inWristR,
                0.4/(0.40), // TODO:
                initial.branch(State.IN.pos, State.IN.pos, State.IN.pos),
                0.04, 0.84,
                0.90, 0.10
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
