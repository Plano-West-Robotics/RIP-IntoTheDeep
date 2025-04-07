package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutWrist extends TimedServo {
    public enum State {
        PRE_TRANSFER(0.96),
        TRANSFER(0.86),
        DROP_FRONT(0.38),
        DROP_BACK(0.37),
        BASKET_FRONT(0.7),
        BASKET_BACK(0.44),
        WALL_FRONT(0.38),
        WALL_BACK(0.6),
        CHAMBER_FRONT(0.04),
        CHAMBER_BACK(0.92),
        ;

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutWrist(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-05-01
        // outWrist
        // out: 0.02
        // in: 0.93
        super(
                raw.outWrist,
                (0.75)/(0.72), // TODO:
                initial.branch(State.WALL_FRONT.pos, State.TRANSFER.pos, State.TRANSFER.pos),
                0.02, 0.93
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
