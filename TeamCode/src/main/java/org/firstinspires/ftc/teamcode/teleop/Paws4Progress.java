package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.units.Vector2;

@TeleOp(name = "Paws 4 Progress")
@Disabled
public class Paws4Progress extends OpModeWrapper {
    TeleDrive drive;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, 0.4);
        drive.setFieldOriented(false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        double x = gamepads.getAnalogValue(Controls.STRAIGHT);
        double y = -gamepads.getAnalogValue(Controls.STRAFE);
        double turn = -gamepads.getAnalogValue(Controls.TURN);
        drive.drive(new Vector2(x, y), turn);

        telemetry.addData("Drive speed", String.format("%.2f", drive.getSpeed()));
        telemetry.addData("Field oriented enabled", drive.getFieldOriented());
    }
}
