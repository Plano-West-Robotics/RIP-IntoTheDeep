package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OutClaw extends TimedServo {
    public static final double OPEN = 0.0;
    public static final double CLOSED = 1.0;

    public OutClaw(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-01-07
        // claw: open 0.25 - closed 0.58, port ch1
        super(
                hardwareMap.get(Servo.class, "outClaw"),
                1, // TODO:
                initial.branch(OPEN, OPEN, CLOSED),
                0.25, 0.58
        );
    }

    public void open() {
        this.setPosition(OPEN);
    }

    public void close() {
        this.setPosition(CLOSED);
    }
}
