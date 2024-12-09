package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class IntakeClaw extends TimedServo {
    public static final double OPEN = 1.0;
    public static final double CLOSED = 0.0;

    public IntakeClaw(HardwareMap hardwareMap) {
        // calibrated on 2024-12-06
        // intake: closed 0.32 - open 0.60, port eh4
        super(
                hardwareMap.get(Servo.class, "intake"),
                1.3,
                CLOSED,
                0.32, 0.60
        );
    }

    public void open() {
        this.setPosition(OPEN);
    }

    public void close() {
        this.setPosition(CLOSED);
    }
}
