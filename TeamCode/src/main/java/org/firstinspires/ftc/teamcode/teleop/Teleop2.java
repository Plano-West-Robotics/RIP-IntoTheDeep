package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.SimpleGrabber;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "DDDDDDDDD 2")
public class Teleop2 extends OpModeWrapper {
    TeleDrive drive;
    DeltaTimer time;
    Intake intake;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, 0.7);
        drive.setFieldOriented(false);
        time = new DeltaTimer(false);
        intake = new Intake(hardware);
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

        if (gamepads.justPressed(Gamepads.Button.GP2_CROSS)) intake.toggleDown();
        intake.setPower(gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y));
        if (gamepads.isPressed(Gamepads.Button.GP2_LEFT_STICK_BUTTON)) {
            if (gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y) > 0) {
                intake.setTarget(1.0);
            } else if (gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y) < 0) {
                intake.setTarget(-1);
            }
        }

        if (gamepads.isPressed(Gamepads.Button.GP2_LEFT_BUMPER)) hardware.intake.setPower(1);
        else if (gamepads.isPressed(Gamepads.Button.GP2_RIGHT_BUMPER)) hardware.intake.setPower(-1);
        else hardware.intake.setPower(0);

        if (gamepads.isPressed(Gamepads.Button.GP1_CROSS)) hardware.bucketL.setPosition(1);
        else hardware.bucketL.setPosition(0);

        intake.update(dt);
    }
}
