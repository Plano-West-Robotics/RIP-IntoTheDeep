package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.units.Angle;

public class Hardware {
    public DcMotorEx fl, fr, bl, br;
    public DcMotorEx liftL, liftR;
    public Servo claw;
    public Servo extendL, extendR;
    public Servo wristL, wristR;
    public Servo intake;
    public Servo bucketL, bucketR;
    public RevTouchSensor liftLimitL, liftLimitR;
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
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // TODO: should be RUN_USING_ENCODER once both encoders are connected
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftL.setDirection(DcMotorSimple.Direction.REVERSE);
        liftR.setDirection(DcMotorSimple.Direction.FORWARD);

        claw = hardwareMap.get(Servo.class, "claw");
        extendL = hardwareMap.get(Servo.class, "extendL");
        extendR = hardwareMap.get(Servo.class, "extendR");
        wristL = hardwareMap.get(Servo.class, "wristL");
//        wristR = hardwareMap.get(Servo.class, "wristR");
        intake = hardwareMap.get(Servo.class, "intake");
        bucketL = hardwareMap.get(Servo.class, "bucketL");
//        bucketR = hardwareMap.get(Servo.class, "bucketR");

        // calibrated on 2024-11-12

        // claw: open 0.86 - closed 0.55, port ch2
        claw.scaleRange(0.55, 0.83);
        claw.setDirection(Servo.Direction.REVERSE);

        //            in - out
        // extendL  0.20 - 0.00, port ch5
        // extendR  0.12 - 0.32, port eh0
        extendL.scaleRange(0.00, 0.20);
        extendL.setDirection(Servo.Direction.REVERSE);
        extendR.scaleRange(0.12, 0.32);
        extendR.setDirection(Servo.Direction.FORWARD);

        //         down - up
        // wristL  0.00 - 0.83, port eh3
        // wristR
        wristL.scaleRange(0.0, 0.83);

        //          closed - open
        // intake - 0.35   - 0.65, port eh5
        intake.scaleRange(0.35, 0.65);

        //          down - up
        // bucketL  0.10 - 0.65, port ch3
        // bucketR
        bucketL.scaleRange(0.10, 0.65);
        bucketL.setDirection(Servo.Direction.FORWARD);

        liftLimitL = hardwareMap.get(RevTouchSensor.class, "liftLimitL");
        liftLimitR = hardwareMap.get(RevTouchSensor.class, "liftLimitR");

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
}
