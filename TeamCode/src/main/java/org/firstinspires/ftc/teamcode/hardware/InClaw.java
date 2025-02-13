package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class InClaw extends TimedServo {
    public static final double OPEN = 1.0;
    public static final double CLOSED = 0.0;

    public InClaw(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        // inClaw: closed 0.31 - open 0.60, port eh3
        super(
                hardwareMap.get(Servo.class, "inClaw"),
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
}
