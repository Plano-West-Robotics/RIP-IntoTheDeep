package org.firstinspires.ftc.teamcode.macro;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * An `Action` that moves a servo to a specific position upon being run.
 *
 * Has no delay.
 */
public class MoveServo implements Action {
    private final Servo servo;
    private final double pos;

    public MoveServo(Servo servo, double pos) {
        this.servo = servo;
        this.pos = pos;
    }

    public ControlFlow update() {
        return ControlFlow.BREAK;
    }

    public void end() {
        servo.setPosition(pos);
    }

    public void run() {
        this.end();
    }
}