package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;

@TeleOp(name = "DDDDDDDDD")
public class Teleop extends OpModeWrapper {
    TeleDrive drive;

    @Override
    public void setup() {
        hardware.fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hardware.fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hardware.bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hardware.br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        drive = new TeleDrive(hardware, 0.7);
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
