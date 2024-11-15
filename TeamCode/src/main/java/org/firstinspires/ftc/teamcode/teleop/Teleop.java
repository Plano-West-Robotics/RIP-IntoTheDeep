package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.SimpleGrabber;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "DDDDDDDDD old")
@Disabled
public class Teleop extends OpModeWrapper {
    TeleDrive drive;
    DeltaTimer time;
    SimpleGrabber grabber;

    double v;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, 0.7);
        drive.setFieldOriented(false);
        time = new DeltaTimer(false);
        grabber = new SimpleGrabber(hardware);
        hardware.intake.setPosition(1);
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

        if (!gamepads.isPressed(Gamepads.Button.GP2_LEFT_STICK_BUTTON)) {
            v += 0.5 * dt * gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y);
            v = Range.clip(v, 0, 1);
        } else {
            v = Range.scale(gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y), -1, 1, 0, 1);
        }
        hardware.extendL.setPosition(v);
        hardware.extendR.setPosition(v);
        telemetry.addData("Extendo Position", String.format("%.2f", v));

        if (gamepads.justPressed(Gamepads.Button.GP2_SQUARE)) grabber.goTo(SimpleGrabber.State.DOWN);
        if (gamepads.justPressed(Gamepads.Button.GP2_TRIANGLE)) grabber.goTo(SimpleGrabber.State.UP);
        if (gamepads.justPressed(Gamepads.Button.GP2_CIRCLE)) grabber.goTo(SimpleGrabber.State.BUCKET);

        if (gamepads.isPressed(Gamepads.Button.GP2_CROSS)) hardware.bucketL.setPosition(1);
        else hardware.bucketL.setPosition(0);

        if (gamepads.isPressed(Gamepads.Button.GP1_CROSS)) hardware.intake.setPosition(0);
        else if (gamepads.isPressed(Gamepads.Button.GP1_CIRCLE)) hardware.intake.setPosition(1);
    }
}
