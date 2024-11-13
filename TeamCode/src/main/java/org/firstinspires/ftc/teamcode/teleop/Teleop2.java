package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "DDDDDDDDD 2")
public class Teleop2 extends OpModeWrapper {
    TeleDrive drive;
    DeltaTimer time;
    Intake intake;
    Lift lift;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, 0.7);
        drive.setFieldOriented(false);
        time = new DeltaTimer(false);
        intake = new Intake(hardware);
        lift = new Lift(hardware);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        double dt = time.poll();

        double x = gamepads.getAnalogValue(Controls.STRAIGHT);
        double y = -gamepads.getAnalogValue(Controls.STRAFE);
        double turn = -gamepads.getAnalogValue(Controls.TURN);
        drive.drive(new Vector2(x, y), turn);

        telemetry.addData("Drive speed", String.format("%.2f", drive.getSpeed()));
        telemetry.addData("Field oriented enabled", drive.getFieldOriented());
        telemetry.addData("Extendo State", intake.getState());

        if (gamepads.justPressed(Gamepads.Button.GP2_CROSS)) intake.toggleDown();
        intake.setPower(gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y));
        if (gamepads.isPressed(Gamepads.Button.GP2_LEFT_STICK_BUTTON)) {
            if (gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y) > 0) {
                intake.setTarget(1.0);
            } else if (gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y) < 0) {
                intake.setTarget(-1);
            }
        }

        if (gamepads.isPressed(Gamepads.Button.GP2_TRIANGLE)) intake.closeIntake();
        else if (gamepads.isPressed(Gamepads.Button.GP2_CIRCLE)) intake.openIntake();

        if (gamepads.isPressed(Gamepads.Button.GP1_SQUARE)) hardware.bucketL.setPosition(1);
        else hardware.bucketL.setPosition(0);

        if (gamepads.justPressed(Gamepads.Button.GP2_DPAD_DOWN)) lift.setTarget(Lift.MIN_TICKS);
        if (gamepads.justPressed(Gamepads.Button.GP2_DPAD_UP)) lift.setTarget(Lift.MAX_TICKS);
        if (gamepads.justPressed(Gamepads.Button.GP2_DPAD_LEFT)) lift.setTarget(2200);
        if (gamepads.justPressed(Gamepads.Button.GP2_DPAD_RIGHT)) lift.setTarget(4200);
        lift.setPower(gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_RIGHT_STICK_Y));
        lift.setOverride(gamepads.isPressed(Gamepads.Button.GP2_RIGHT_STICK_BUTTON));

        intake.update(dt);
        lift.update(dt);
    }
}
