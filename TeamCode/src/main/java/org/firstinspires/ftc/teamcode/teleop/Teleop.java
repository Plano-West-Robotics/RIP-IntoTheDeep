package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.subsystems.TeleRobot;
import org.firstinspires.ftc.teamcode.units.Vector2;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "DDDDDDDDD")
public class Teleop extends OpModeWrapper {
    TeleDrive drive;
    TeleRobot robot;
    DeltaTimer time;

    private static final double MID_SPEED = 0.7;
    private static final double LOW_SPEED = 0.3;

    private static final double SLOW_SLIDES = 0.3;

    @Override
    public void setup() {
        drive = new TeleDrive(hardware, MID_SPEED);
        drive.setFieldOriented(true);
        robot = new TeleRobot(hardware);
        time = new DeltaTimer(false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
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

        double slidePow = gamepads.getAnalogValue(Controls.SLIDES);
        robot.setSlidePow(slidePow * (gamepads.isPressed(Controls.SLIDES_SLOW_MODE) ? SLOW_SLIDES : 1));
        robot.setExtendTrigger(gamepads.isPressed(Controls.EXTEND));

        if (gamepads.justPressed(Controls.INTAKE_TOGGLE_DOWN)) robot.grabberToggleDown();
        if (gamepads.justPressed(Controls.INTAKE_BUMP_LEFT)) {
            robot.swivelBumpLeft();
        }
        if (gamepads.justPressed(Controls.INTAKE_BUMP_RIGHT)) {
            robot.swivelBumpRight();
        }

        if (gamepads.justPressed(Controls.DROP_SAMPLE)) robot.pressSampleDropButton();

        if (gamepads.justPressed(Controls.SPECIMEN_CYCLE)) robot.pressSpecimenCycleButton();

        drive.update(dt);
        robot.update(dt);
    }
}
