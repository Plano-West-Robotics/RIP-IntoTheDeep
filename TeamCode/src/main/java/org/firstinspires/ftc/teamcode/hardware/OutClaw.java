package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OutClaw extends TimedServo {
    public static final double OPEN = 0.0;
    public static final double CLOSED = 1.0;

    public OutClaw(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2025-02-19
        // outClaw: open 0.65 - closed 0.98, port ch4
        super(
                hardwareMap.get(Servo.class, "outClaw"),
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
