package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutShoulder extends TimedServo.Pair {
    public enum State {
        WALL(0.00), TRANSFER(0.08), PRE_TRANSFER(0.18), OUT(1.00);
        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutShoulder(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        //              in - out
        // shoulderL  1.00 - 0.00
        // shoulderR  0.00 - 1.00
        super(
                raw.shoulderL,
                raw.shoulderR,
                (0.92)/(0.84),
                initial.branch(State.WALL.pos, State.TRANSFER.pos, State.WALL.pos),
                1.00, 0.00,
                0.00, 1.00
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
