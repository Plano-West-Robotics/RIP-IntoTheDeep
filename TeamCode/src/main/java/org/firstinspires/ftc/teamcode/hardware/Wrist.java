package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;

//public class Wrist extends TimedServo.Pair {
public class Wrist extends TimedServo {
    public enum State {
        BUCKET(1.0), UP(0.42), DOWN(0.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public Wrist(HardwareMap hardwareMap) {
        // calibrated on 2024-12-06
        //         down - up
        // wristL  0.05 - 1.00, port eh3
        // wristR  0.95 - 0.00, port eh1
        super(
                hardwareMap.get(Servo.class, "wristL"),
//                hardwareMap.get(Servo.class, "wristR"),
                1.1,
                State.BUCKET.pos,
                0.05, 1.00//,
//                0.95, 0.00
        );
    }

    public void setPosition(State pos) {
        this.setPosition(pos.pos);
    }

    @CheckResult(suggest = "setPosition(State)")
    public Action goTo(State pos) {
        return this.goTo(pos.pos);
    }
}
