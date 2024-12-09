package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "DDDDDDDDD")
public class Teleop extends OpModeWrapper {
    TeleDrive drive;
    DeltaTimer time;
    Intake intake;
    ControlledLift lift;

    private static final double MID_SPEED = 0.7;
    private static final double LOW_SPEED = 0.3;

    private static final double SLOW_LIFT = 0.3;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, MID_SPEED);
        drive.setFieldOriented(true);
        time = new DeltaTimer(false);
        intake = new Intake(hardware);
        lift = new ControlledLift(hardware);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        telemetry.addData("lift pos", lift.getCurrentPos());

        double dt = time.poll();

        if (gamepads.isPressed(Controls.SLOW_MODE)) {
            drive.setSpeed(LOW_SPEED);
            telemetry.addData("Speed", "Slow");
        } else {
            drive.setSpeed(MID_SPEED);
            telemetry.addData("Speed", "Normal");
        }

        double x = gamepads.getAnalogValue(Controls.STRAIGHT);
        double y = -gamepads.getAnalogValue(Controls.STRAFE);
        Vector2 pow = new Vector2(x, y);
        double turn = -gamepads.getAnalogValue(Controls.TURN);
        drive.drive(pow, turn);

        if (gamepads.justPressed(Controls.YAW_RESET)) drive.resetYaw();

        telemetry.addData("Field oriented enabled", drive.getFieldOriented());
        telemetry.addData("Yaw", hardware.imu.getYaw().valInDegrees());
        telemetry.addData("Extendo State", intake.getState());

        if (gamepads.justPressed(Controls.INTAKE_TOGGLE_DOWN)) intake.toggleDown();
        double intakePow = gamepads.getAnalogValue(Controls.EXTENDO);
        intake.setPower(intakePow);
        if (gamepads.isPressed(Controls.EXTENDO_GOTO)) {
            if (intakePow > 0) intake.setTarget(1.0);
            else if (intakePow < 0) intake.setTarget(-1);
        }

        if (gamepads.justPressed(Controls.INTAKE_BUMP_LEFT)) {
            intake.swivelBumpLeft();
        }
        if (gamepads.justPressed(Controls.INTAKE_BUMP_RIGHT)) {
            intake.swivelBumpRight();
        }

        if (gamepads.isPressed(Controls.BUCKET_DROP)) hardware.bucket.up();
        else hardware.bucket.down();

        if (gamepads.isPressed(Controls.CLAW_GRAB)) hardware.claw.setPosition(1);
        else hardware.claw.setPosition(0);

        if (gamepads.justPressed(Controls.LIFT_TO_DOWN)) lift.setTarget(ControlledLift.MIN_TICKS);
        if (gamepads.justPressed(Controls.LIFT_TO_BASKET1)) lift.setTarget(2200);
        if (gamepads.justPressed(Controls.LIFT_TO_BASKET2)) lift.setTarget(4200);
        if (gamepads.justPressed(Controls.LIFT_TO_SPECIMEN)) lift.setTarget(200);
        lift.setPower(gamepads.isPressed(Gamepads.Button.GP2_LEFT_BUMPER) ?
                gamepads.getAnalogValue(Controls.LIFT) * SLOW_LIFT : gamepads.getAnalogValue(Controls.LIFT));
        lift.setOverride(gamepads.isPressed(Controls.LIFT_OVERRIDE));

        intake.update(dt);
        lift.update(dt);
    }
}
