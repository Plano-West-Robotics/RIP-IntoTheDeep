package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutShoulder extends TimedServo.Pair {
    public enum State {
        PRE_TRANSFER(0.22),
        TRANSFER(0.14),
        DROP_FRONT(0.22),
        DROP_BACK(0.95),
        BASKET_FRONT(0.5),
        BASKET_BACK(0.86),
        WALL_FRONT(0.13),
        WALL_BACK(1),
        CHAMBER_FRONT(0.37),
        CHAMBER_BACK(0.8),
        ;

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutShoulder(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-05-01
        //              in - out
        // shoulderL  0.82 - 0.34
        // shoulderR  0.18 - 0.66
        // 0.03 to center from in bound and the arm barely hits the hardstop
        // 0.06 away from center from in bound and the arm is against the hardstop with no wiggle
        super(
                raw.shoulderL,
                raw.shoulderR,
                (0.92)/(0.84), // TODO:
                initial.branch(State.WALL_FRONT.pos, State.TRANSFER.pos, State.TRANSFER.pos),
                0.82, 0.34,
                0.18, 0.66
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
