package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;

public class InClaw extends TimedServo {
    public static final double OPEN = 1.0;
    public static final double CLOSED = 0.0;

    public InClaw(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        // inClaw: closed 0.31 - open 0.60
        super(
                raw.inClaw,
                0.29/(0.19),
                initial.branch(OPEN, OPEN, OPEN),
                0.31, 0.60
        );
    }

    public void open() {
        this.setPosition(OPEN);
    }

    public void close() {
        this.setPosition(CLOSED);
    }

    @CheckResult(suggest = "open()")
    public Action toOpen() {
        return this.goTo(OPEN);
    }

    @CheckResult(suggest = "close()")
    public Action toClosed() {
        return this.goTo(CLOSED);
    }
}
