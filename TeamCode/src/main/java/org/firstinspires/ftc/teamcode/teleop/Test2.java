//package org.firstinspires.ftc.teamcode.teleop;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.OpModeWrapper;
//import org.firstinspires.ftc.teamcode.subsystems.FieldOrienter;
//import org.firstinspires.ftc.teamcode.subsystems.TeleDrive;
//import org.firstinspires.ftc.teamcode.units.Vector2;
//import org.firstinspires.ftc.teamcode.util.DeltaTimer;
//
//@TeleOp(name = "This is the real test")
//@Config
//public class Test2 extends OpModeWrapper {
//    TeleDrive drive;
//    FieldOrienter fieldOrienter;
//    TeleRobotElectricBoogaloo robot;
//    DeltaTimer dter;
//
////    public static double GP1_LEFT_STICK_X = 0;
////    public static double GP1_LEFT_STICK_Y = 0;
////    public static double GP1_RIGHT_STICK_X = 0;
////    public static double GP1_RIGHT_STICK_Y = 0;
////    public static boolean GP1_CROSS = false;
////
////    public static double GP2_LEFT_STICK_Y = 0;
////    public static boolean GP2_RIGHT_TRIGGER = false;
////    public static boolean GP2_CROSS = false;
////    public static boolean GP2_SQUARE = false;
////    public static boolean GP2_LEFT_TRIGGER = false;
////    public static boolean GP2_LEFT_BUMPER = false;
//
//    @Override
//    public void setup() {
//        drive = new TeleDrive(hardware, 0.7);
//        fieldOrienter = new FieldOrienter(hardware.imu);
//        robot = new TeleRobotElectricBoogaloo(hardware);
//        dter = new DeltaTimer(true);
//    }
//
//    @Override
//    public void run() {
//        fieldOrienter.update();
//
//        double dt = dter.poll();
//
//        drive.drive(
//                fieldOrienter.fieldToRobot(new Vector2(
//                        gamepads.getAnalogValue(Gamepads.AnalogInput.GP1_LEFT_STICK_X),
//                        gamepads.getAnalogValue(Gamepads.AnalogInput.GP1_LEFT_STICK_Y)
//                )),
//                gamepads.getAnalogValue(Gamepads.AnalogInput.GP1_RIGHT_STICK_X)
//        );
//
//        boolean extendTrigger = gamepads.isPressed(Gamepads.Button.GP2_RIGHT_TRIGGER);
//        double extendPow = gamepads.getAnalogValue(Gamepads.AnalogInput.GP2_LEFT_STICK_Y);
//        boolean extendCancelButton = gamepads.isPressed(Gamepads.Button.GP2_CROSS);
//        boolean outtakeButton = gamepads.justPressed(Gamepads.Button.GP1_CROSS);
//        int directionHint = (int)Math.signum(gamepads.getAnalogValue(Gamepads.AnalogInput.GP1_RIGHT_STICK_Y));
//
//        if (gamepads.justPressed(Gamepads.Button.GP2_LEFT_TRIGGER)) robot.swivelBumpLeft();
//        if (gamepads.justPressed(Gamepads.Button.GP2_LEFT_BUMPER)) robot.swivelBumpRight();
//        if (gamepads.justPressed(Gamepads.Button.GP2_SQUARE)) robot.grabberToggleDown();
//
//        drive.update(dt);
//        robot.update(dt,
//                extendTrigger,
//                extendPow,
//                extendCancelButton,
//                fieldOrienter.facingPosY(),
//                outtakeButton,
//                directionHint
//        );
//    }
//}
