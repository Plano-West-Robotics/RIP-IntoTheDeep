package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Extendo extends TimedServo.Pair {
    public Extendo(HardwareMap hardwareMap, Hardware.InitialConfiguration _initial) {
        // calibrated on 2024-12-06
        //            in - out
        // extendL  0.00 - 0.77, port ch5
        // extendR  0.97 - 0.20, port eh0
        super(
                hardwareMap.get(Servo.class, "extendL"),
                hardwareMap.get(Servo.class, "extendR"),
                1.0, // TODO:
                0.0,
                0.00, 0.77,
                0.97, 0.20
        );
    }
}
