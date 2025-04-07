package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Imu;
import org.firstinspires.ftc.teamcode.hardware.InClaw;
import org.firstinspires.ftc.teamcode.hardware.InSwivel;
import org.firstinspires.ftc.teamcode.hardware.InWrist;
import org.firstinspires.ftc.teamcode.hardware.Lift;
import org.firstinspires.ftc.teamcode.hardware.LiftPivot;
import org.firstinspires.ftc.teamcode.hardware.RawHardware;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.subsystems.Grabber;
import org.firstinspires.ftc.teamcode.subsystems.PivotControl;
import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "This is the test")
@Config
public class Test extends OpMode {
    RawHardware raw;
    PivotControl pivot;
    Drivetrain drivetrain;
    TeleDrive drive;
    DeltaTimer dter;
    Grabber grabber;
    Extendo extend;
    Imu imu;
    ControlledLift lift;

    Gamepads gamepads;

    double p = PivotControl.MIN_TICKS;
    double x;
    boolean abc;

    public static int FOO = 0;

    @Override
    public void init() {
        raw = new RawHardware(hardwareMap);
//        pivot = new PivotControl(new LiftPivot(raw, Hardware.InitialConfiguration.TELEOP));
//        imu = new Imu(raw);
//        drivetrain = new Drivetrain(raw);
//        drive = new TeleDrive(drivetrain, imu, 1.0);
//        grabber = new Grabber(
//                new InWrist(raw, Hardware.InitialConfiguration.TELEOP),
//                new InSwivel(raw, Hardware.InitialConfiguration.TELEOP),
//                new InClaw(raw, Hardware.InitialConfiguration.TELEOP),
//                Hardware.InitialConfiguration.TELEOP
//        );
//        extend = new Extendo(raw, Hardware.InitialConfiguration.TELEOP);
        dter = new DeltaTimer(true);
        lift = new ControlledLift(new Lift(raw, Hardware.InitialConfiguration.TELEOP));

        gamepads = new Gamepads(gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        gamepads.update(gamepad1, gamepad2);
        double dt = dter.poll();

        lift.setPower(-gamepad1.left_stick_y);
        lift.setOverride(gamepad1.left_stick_button);
        if (gamepads.justReleased(Gamepads.Button.GP1_CROSS)) {
            lift.setTarget(FOO);
        }

        telemetry.addData("foo", lift.getCurrentPos());

        lift.update(dt);

//        p += dt * 6000 * -gamepad2.left_stick_y;
//        p = Range.clip(p, PivotControl.MIN_TICKS, PivotControl.MAX_TICKS);
//        telemetry.addData("p", (int)p);
//        pivot.setTarget((int)p);
//        pivot.update(dt);

//        drive.drive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
//        drive.update(dt);

//        if (gamepads.justPressed(Gamepads.Button.GP2_RIGHT_TRIGGER)) {
//            grabber.toUp();
//            abc = false;
//        } else if (gamepads.justReleased(Gamepads.Button.GP2_RIGHT_TRIGGER)) {
//            grabber.toTransfer();
//        }
//
//        if (gamepads.isPressed(Gamepads.Button.GP2_RIGHT_TRIGGER)) {
//            x += 2 * dt * -gamepad2.left_stick_y;
//            x = Range.clip(x, 0.2, 1);
//
//            if (gamepads.justPressed(Gamepads.Button.GP2_LEFT_BUMPER)) {
//                grabber.swivelBumpLeft();
//            } else if (gamepads.justPressed(Gamepads.Button.GP2_LEFT_TRIGGER)) {
//                grabber.swivelBumpRight();
//            }
//
//            if (gamepads.justPressed(Gamepads.Button.GP2_CROSS)) {
//                if (abc) {
//                    grabber.toUp();
//                    abc = false;
//                } else {
//                    grabber.toDown();
//                    abc = true;
//                }
//            }
//        } else {
//            x = 0;
//        }
//        extend.setPosition(x);
//
//        extend.update(dt);
//        grabber.update(dt);
    }
}
