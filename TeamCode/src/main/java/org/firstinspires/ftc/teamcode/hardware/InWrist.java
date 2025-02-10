package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;

//public class InWrist extends TimedServo.Pair {
public class InWrist extends TimedServo {
    public enum State {
        IN(1.0), TRANSFER(0.81), UP(0.42), DOWN(0.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public InWrist(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-12-06
        //           down - up
        // inWristL  0.05 - 1.00, port eh3
        // inWristR  0.95 - 0.00, port eh1
        super(
                hardwareMap.get(Servo.class, "inWristL"),
//                hardwareMap.get(Servo.class, "inWristR"),
                1/(1.20),
                initial.branch(State.IN.pos, State.TRANSFER.pos, State.IN.pos),
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
