package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//public class Bucket extends TimedServo.Pair {
public class Bucket extends TimedServo {
    public static final double DOWN = 0.0;
    public static final double UP = 1.0;

    public Bucket(HardwareMap hardwareMap) {
        // calibrated on 2024-12-06
        //          down - up
        // bucketL  0.10 - 0.67, port ch3
        // bucketR             , port ch4
        super(
                hardwareMap.get(Servo.class, "bucketL"),
//                hardwareMap.get(Servo.class, "bucketR"),
                0.6,
                DOWN,
                0.10, 0.67//,
//                0.97, 0.07
        );
    }

    public void down() {
        this.setPosition(DOWN);
    }

    public void up() {
        this.setPosition(UP);
    }
}
