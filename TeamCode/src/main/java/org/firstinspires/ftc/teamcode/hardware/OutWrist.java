package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macro.Action;

public class OutWrist extends TimedServo {
    public enum State {
        TRANSFER(0), BASKET(0.59), WALL(0.67), CHAMBER(1.0);

        public final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public OutWrist(HardwareMap hardwareMap, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-01-16
        // outWrist, port ch4
        // min: 0.16
        // transfer: 0.16
        // basket: 0.60
        // wall: 0.66
        // chamber: 0.91
        // max: 0.91
        // if a tooth skips, +0.1 to all of those
        super(
                hardwareMap.get(Servo.class, "outWrist"),
                1.1,
                initial.branch(State.WALL.pos, State.TRANSFER.pos, State.CHAMBER.pos /* :/ */),
                0.16, 0.91
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
