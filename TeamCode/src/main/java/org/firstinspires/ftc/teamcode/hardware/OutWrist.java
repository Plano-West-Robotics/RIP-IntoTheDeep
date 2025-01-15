package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutWrist extends TimedServo {
    public enum State {
        TRANSFER(0), BASKET(0.68), WALL(0.75), CHAMBER(1.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutWrist(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-01-09
        // outWrist, port ch4
        // min: 0.16
        // transfer: 0.16
        // basket: 0.62
        // wall: 0.67
        // chamber: 0.84
        // max: 0.84
        // if a tooth skips, +0.1 to all of those
        super(
                hardwareMap.get(Servo.class, "outWrist"),
                1.1,
                initial.branch(State.WALL.pos, State.TRANSFER.pos, State.CHAMBER.pos /* :/ */),
                0.16, 0.84
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
