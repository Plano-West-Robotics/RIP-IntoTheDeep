package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw extends TimedServo {
    public static final double OPEN = 0.0;
    public static final double CLOSED = 1.0;

    public Claw(HardwareMap hardwareMap) {
        // calibrated on 2024-12-06
        // claw: open 0.50 - closed 0.31, port ch2
        super(
                hardwareMap.get(Servo.class, "claw"),
                1, // TODO:
                CLOSED,
                0.50, 0.31
        );
    }

    public void open() {
        this.setPosition(OPEN);
    }

    public void close() {
        this.setPosition(CLOSED);
    }
}
