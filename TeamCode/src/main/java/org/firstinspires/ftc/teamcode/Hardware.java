package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.List;

public class Hardware {
    public DcMotorEx fl, fr, bl, br;
    public DcMotorEx liftL, liftR;
    public Servo claw;
    public Servo extendL, extendR;
    public Servo wristL, wristR;
    public Servo swivel;
    public Servo intake;
    public Servo bucketL, bucketR;
    public RevTouchSensor liftLimitL, liftLimitR;
    public DistanceSensor distL, distR;
    public Encoder backOdo, rightOdo;
    public IMU imu;

    public OpMode opMode;
    public DashboardTelemetryWrapper dashboardTelemetry;

    public Hardware(OpMode opMode) {
        this.opMode = opMode;

        this.dashboardTelemetry = new DashboardTelemetryWrapper(FtcDashboard.getInstance());
        opMode.telemetry = new MultipleTelemetry(opMode.telemetry, this.dashboardTelemetry);

        HardwareMap hardwareMap = opMode.hardwareMap;

        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftL = hardwareMap.get(DcMotorEx.class, "liftL");
        liftR = hardwareMap.get(DcMotorEx.class, "liftR");
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftL.setDirection(DcMotorSimple.Direction.REVERSE);
        liftR.setDirection(DcMotorSimple.Direction.FORWARD);

        claw = hardwareMap.get(Servo.class, "claw");
        extendL = hardwareMap.get(Servo.class, "extendL");
        extendR = hardwareMap.get(Servo.class, "extendR");
        wristL = hardwareMap.get(Servo.class, "wristL");
        wristR = hardwareMap.get(Servo.class, "wristR");
        swivel = hardwareMap.get(Servo.class, "swivel");
        intake = hardwareMap.get(Servo.class, "intake");
        bucketL = hardwareMap.get(Servo.class, "bucketL");
//        bucketR = hardwareMap.get(Servo.class, "bucketR");

        // calibrated on 2024-12-06

        // claw: open 0.50 - closed 0.31, port ch2
        claw.scaleRange(0.31, 0.50);
        claw.setDirection(Servo.Direction.REVERSE);

        //            in - out
        // extendL  0.00 - 0.90, port ch5
        // extendR  0.97 - 0.07, port eh0
        extendL.scaleRange(0.00, 0.73);
        extendL.setDirection(Servo.Direction.FORWARD);
        extendR.scaleRange(0.25, 0.98);
        extendR.setDirection(Servo.Direction.REVERSE);

        //         down - up
        // wristL  0.05 - 1.00, port eh3
        // wristR  0.95 - 0.00, port eh1
        wristL.scaleRange(0.05, 1.00);
        wristL.setDirection(Servo.Direction.FORWARD);
        wristR.scaleRange(0.0, 0.95);
        wristR.setDirection(Servo.Direction.REVERSE);

        //          closed - open
        // intake - 0.32   - 0.60, port eh4
        intake.scaleRange(0.32, 0.60);

        // swivel, port eh5
        // right: 0.0
        // mid: 0.333
        // left: 0.667
        // bucket: 1.0
        swivel.scaleRange(0.00, 1.00);

        //          down - up
        // bucketL  0.10 - 0.67, port ch3
        // bucketR             , port ch4
        bucketL.scaleRange(0.10, 0.67);
        bucketL.setDirection(Servo.Direction.FORWARD);

        liftLimitL = hardwareMap.get(RevTouchSensor.class, "liftLimitL");
        liftLimitR = hardwareMap.get(RevTouchSensor.class, "liftLimitR");

        distL = hardwareMap.get(DistanceSensor.class, "distL");
        distR = hardwareMap.get(DistanceSensor.class, "distR");

        backOdo = new Encoder(hardwareMap.get(DcMotor.class, "backOdo"));
        rightOdo = new Encoder(hardwareMap.get(DcMotor.class, "rightOdo"));

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        ));

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    /**
     * Get yaw in specified unit. Counterclockwise is positive. The zero position is defined by a call to <code>resetYaw</code>
     *
     * @param angleUnit Unit of the returned angle
     * @return yaw in specified unit
     */
    public double getYaw(AngleUnit angleUnit) {
        return imu.getRobotYawPitchRollAngles().getYaw(angleUnit);
    }

    /**
     * Get yaw in radians. Counterclockwise is positive. The zero position is defined by a call to <code>resetYaw</code>.
     *
     * @return yaw in radians
     */
    public Angle getYaw() {
        return Angle.inRadians(getYaw(AngleUnit.RADIANS));
    }

    /**
     * Reset yaw reading to 0.
     */
    public void resetYaw() {
        imu.resetYaw();
    }

//    @OnCreateEventLoop
//    public static void attachEventLoop(Context _context, FtcEventLoop eventLoop) {
//        OpModeManagerImpl mngr = eventLoop.getOpModeManager();
//        mngr.registerListener(new OpModeManagerNotifier.Notifications() {
//            List<LynxModule> hubs;
//
//            @Override
//            public void onOpModePreInit(OpMode opMode) {
//                if (!mngr.getActiveOpModeName().equals(OpModeManagerImpl.DEFAULT_OP_MODE_NAME)) {
//                    hubs = opMode.hardwareMap.getAll(LynxModule.class);
//                    for (LynxModule hub : hubs) {
//                        hub.setConstant(0xff_3f_3f);
//                    }
//                } else {
//                    hubs = null;
//                }
//            }
//
//            @Override
//            public void onOpModePreStart(OpMode opMode) {}
//
//            @Override
//            public void onOpModePostStop(OpMode opMode) {
//                if (hubs != null) {
//                    for (LynxModule hub : hubs) {
//                        hub.setPattern(LynxModule.blinkerPolicy.getIdlePattern(hub));
//                    }
//                }
//            }
//        });
//    }
}
