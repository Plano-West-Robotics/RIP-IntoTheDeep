package org.firstinspires.ftc.teamcode.macro;

import com.qualcomm.robotcore.hardware.CRServo;

public class MoveCRServo implements Action {
    private final CRServo servo;
    private final double power;

    public MoveCRServo(CRServo servo, double power) {
        this.servo = servo;
        this.power = power;
    }

    public ControlFlow update() {
        return ControlFlow.BREAK;
    }

    public void end() {
        servo.setPower(power);
    }

    public void run() {
        this.end();
    }
}
