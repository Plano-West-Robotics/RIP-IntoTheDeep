package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.util.Encoder;

public class RawHardware {
    public final DcMotorImplEx fl, fr, bl, br;
    public final DcMotorImplEx liftL, liftR;
    public final DcMotorImplEx pivotL, pivotR;
    public final Encoder liftEncoder;

    public final ServoImplEx extendL, extendR;
    public final ServoImplEx inWristL, inWristR;
    public final ServoImplEx inSwivel;
    public final ServoImplEx inClaw;

    public final ServoImplEx shoulderL, shoulderR;
    public final ServoImplEx outWrist;
    public final ServoImplEx outSwivel;
    public final ServoImplEx outClaw;

    public final Encoder backOdo, rightOdo;
    public final IMU imu;
    public final RevTouchSensor liftLimit;

    public RawHardware(HardwareMap hardwareMap) {
        this.fl = motor(hardwareMap, "fl"); // ch0
        this.fr = motor(hardwareMap, "fr"); // eh0
        this.bl = motor(hardwareMap, "bl"); // ch2
        this.br = motor(hardwareMap, "br"); // eh2

        this.liftL = motor(hardwareMap, "liftL"); // ch1
        this.liftR = motor(hardwareMap, "liftR"); // eh1

        this.pivotL = motor(hardwareMap, "pivotL"); // ch3
        this.pivotR = motor(hardwareMap, "pivotR"); // eh3

        this.liftEncoder = new Encoder(this.liftR); // eh1

        this.extendL = servo(hardwareMap, "extendL"); // ch0
        this.extendR = servo(hardwareMap, "extendR"); // eh5
        this.inWristL = servo(hardwareMap, "inWristL"); // eh1
        this.inWristR = servo(hardwareMap, "inWristR"); // eh2
        this.inSwivel = servo(hardwareMap, "inSwivel"); // eh3
        this.inClaw = servo(hardwareMap, "inClaw"); // eh4

        this.shoulderL = servo(hardwareMap, "shoulderL"); // ch1
        this.shoulderR = servo(hardwareMap, "shoulderR"); // ch5
        this.outWrist = servo(hardwareMap, "outWrist"); // ch4
        this.outSwivel = servo(hardwareMap, "outSwivel"); // ch3
        this.outClaw = servo(hardwareMap, "outClaw"); // ch2

        this.backOdo = new Encoder(this.fl); // ch0
        this.rightOdo = new Encoder(this.fr); // eh0

        this.imu = hardwareMap.get(IMU.class, "imu");

        this.liftLimit = hardwareMap.get(RevTouchSensor.class, "liftLimit");

        // for now we still do this *always* and have no reason to do otherwise. if that changes, then...?
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    private static DcMotorImplEx motor(HardwareMap hardwareMap, String name) {
        return hardwareMap.get(DcMotorImplEx.class, name);
    }

    private static ServoImplEx servo(HardwareMap hardwareMap, String name) {
        ServoImplEx s = hardwareMap.get(ServoImplEx.class, name);
        s.setPwmRange(new PwmControl.PwmRange(500, 2500));
        return s;
    }
}
