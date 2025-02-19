package org.firstinspires.ftc.teamcode.hardware;

public class OutClaw extends TimedServo {
    public static final double OPEN = 0.0;
    public static final double CLOSED = 1.0;

    public OutClaw(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2025-02-19
        // outClaw: open 0.65 - closed 0.98
        super(
                raw.outClaw,
                (0.33)/(0.33),
                initial.branch(OPEN, OPEN, CLOSED),
                0.65, 0.98
        );
    }

    public void open() {
        this.setPosition(OPEN);
    }

    public void close() {
        this.setPosition(CLOSED);
    }
}
