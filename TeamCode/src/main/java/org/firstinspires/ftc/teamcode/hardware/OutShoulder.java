package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutShoulder extends TimedServo.Pair {
    public enum State {
        WALL(0.04), TRANSFER(0.25), PRE_TRANSFER(0.33), OUT(1.00);
// transfer was 0.23
        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutShoulder(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-01-16
        //              in - out
        // shoulderL  1.00 - 0.00, port ch3
        // shoulderR  0.00 - 1.00, port ch0
        super(
                hardwareMap.get(Servo.class, "shoulderL"),
                hardwareMap.get(Servo.class, "shoulderR"),
                (0.96)/(1.05),
                initial.branch(State.WALL.pos, State.TRANSFER.pos, State.WALL.pos),
                1.00, 0.00,
                0.00, 1.00
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
