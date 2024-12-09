package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Swivel extends TimedServo {
    public static final double LEFT = 2/3.;
    public static final double MIDDLE = 1/3.;
    public static final double RIGHT = 0;
    public static final double BUCKET = 1;

    public Swivel(HardwareMap hardwareMap) {
        // calibrated on 2024-12-06
        // swivel, port eh5
        // right: 0.0
        // mid: 0.333
        // left: 0.667
        // bucket: 1.0
        super(
                hardwareMap.get(Servo.class, "swivel"),
                1.0, // TODO:
                BUCKET,
                0.00, 1.00
        );
    }
}
