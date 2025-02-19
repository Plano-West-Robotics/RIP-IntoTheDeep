package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutWrist extends TimedServo {
    public enum State {
        TRANSFER(0.88), BASKET(0.43), WALL(0.49), CHAMBER(0.00);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutWrist(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        // outWrist
        // min: 0.25
        // chamber: 0.25
        // basket: 0.57
        // wall: 0.62
        // transfer: 0.91
        // max: 1.00
        super(
                raw.outWrist,
                (0.75)/(0.72),
                initial.branch(State.WALL.pos, State.TRANSFER.pos, State.CHAMBER.pos /* :/ */),
                0.25, 1.00
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
