package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class RawHardware {
    public final DcMotorImplEx fl, fr, bl, br;
    public final DcMotorImplEx liftL, liftR, liftL2, liftR2;

    public final ServoImplEx extendL, extendR;
    public final ServoImplEx inWristL, inWristR;
    public final ServoImplEx inSwivel;
    public final ServoImplEx inClaw;

    public final ServoImplEx shoulderL, shoulderR;
    public final ServoImplEx outWrist;
    public final ServoImplEx outClaw;

    public final IMU imu;
    public final Rev2mDistanceSensor distL, distR;
    public final RevTouchSensor liftLimitL, liftLimitR;

    public RawHardware(HardwareMap hardwareMap) {
        this.fl = motor(hardwareMap, "fl");
        this.fr = motor(hardwareMap, "fr");
        this.bl = motor(hardwareMap, "bl");
        this.br = motor(hardwareMap, "br");

        this.liftL = motor(hardwareMap, "liftL");
        this.liftR = motor(hardwareMap, "liftR");
        this.liftL2 = motor(hardwareMap, "liftL2");
        this.liftR2 = motor(hardwareMap, "liftR2");

        this.extendL = servo(hardwareMap, "extendL"); // ch5
        this.extendR = servo(hardwareMap, "extendR"); // eh0
        this.inWristL = servo(hardwareMap, "inWristL"); // eh2
        this.inWristR = servo(hardwareMap, "inWristR"); // eh5
        this.inSwivel = servo(hardwareMap, "inSwivel"); // eh4
        this.inClaw = servo(hardwareMap, "inClaw"); // eh3

        this.shoulderL = servo(hardwareMap, "shoulderL"); // ch1
        this.shoulderR = servo(hardwareMap, "shoulderR"); // ch3
        this.outWrist = servo(hardwareMap, "outWrist"); // ch2
        this.outClaw = servo(hardwareMap, "outClaw"); // ch4

        this.imu = hardwareMap.get(IMU.class, "imu");

        this.distL = hardwareMap.get(Rev2mDistanceSensor.class, "distL");
        this.distR = hardwareMap.get(Rev2mDistanceSensor.class, "distR");

        this.liftLimitL = hardwareMap.get(RevTouchSensor.class, "liftLimitL");
        this.liftLimitR = hardwareMap.get(RevTouchSensor.class, "liftLimitR");

        // for now we still do this *always* and have no reason to do otherwise. if that changes, then...?
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    private static DcMotorImplEx motor(HardwareMap hardwareMap, String name) {
        return hardwareMap.get(DcMotorImplEx.class, name);
    }

    private static ServoImplEx servo(HardwareMap hardwareMap, String name) {
        return hardwareMap.get(ServoImplEx.class, name);
    }
}
