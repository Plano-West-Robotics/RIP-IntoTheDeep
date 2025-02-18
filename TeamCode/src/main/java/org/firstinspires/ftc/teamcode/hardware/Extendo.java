package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Extendo extends TimedServo.Pair {
    public static final double IN = 0.0;
    public static final double OUT_GRABBER_UP = 0.74; // horizontal expansion limit
    public static final double OUT_GRABBER_DOWN = 1.0;

    public Extendo(HardwareMap hardwareMap, Hardware.InitialConfiguration _initial) {
        // calibrated on 2024-02-18
        //            in - out
        // extendL  0.05 - 0.82, port ch5
        // extendR  0.95 - 0.18, port eh0
        super(
                hardwareMap.get(Servo.class, "extendL"),
                hardwareMap.get(Servo.class, "extendR"),
                1.0, // TODO:
                0.0,
                0.05, 0.82,
                0.95, 0.18
        );
    }
}
