package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Pose;

public class TwoDeadWheelLocalizer implements DeltaLocalizer {
    Hardware hardware;

    // sensor readings
    int backOdo;
    int rightOdo;
    public Angle imuYaw;

    public static final double MM_PER_ENCODER_TICK = (35 * Math.PI) / 8192; // open odo
    public static final double MM_PER_ENCODER_TICK_2 = (48 * Math.PI) / 2000; // gobilda swing-arm

    public static final Distance BACK_ODO_LEVER_ARM = Distance.inInches(6 + 9/16.);
    public static final Distance RIGHT_ODO_LEVER_ARM = Distance.inInches(3 + 15/16.);
    public static int BACK_ODO_DIR = -1;
    public static int RIGHT_ODO_DIR = -1;

    public TwoDeadWheelLocalizer(Hardware hardware) {
        this.hardware = hardware;

        this.backOdo = hardware.backOdo.getCurrentPosition();
        this.rightOdo = hardware.rightOdo.getCurrentPosition();
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) { }
        this.imuYaw = hardware.imu.getYaw();
    }

    public Pose updateWithDelta() {
        int newBackOdo = hardware.backOdo.getCurrentPosition();
        int newRightOdo = hardware.rightOdo.getCurrentPosition();
        Angle newImuYaw = hardware.imu.getYaw();

        double backOdoDiff = (newBackOdo - this.backOdo) * BACK_ODO_DIR * MM_PER_ENCODER_TICK;
        double rightOdoDiff = (newRightOdo - this.rightOdo) * RIGHT_ODO_DIR * MM_PER_ENCODER_TICK_2;
        Angle imuYawDiff = newImuYaw.sub(this.imuYaw);

        // yawDiff is in rad, rest in mm
        double yawDiff = imuYawDiff.modSigned().valInRadians();
        double relativeXDiff = rightOdoDiff - yawDiff * RIGHT_ODO_LEVER_ARM.valInMM();
        double relativeYDiff = yawDiff * BACK_ODO_LEVER_ARM.valInMM() - backOdoDiff;

        Pose delta = DeltaLocalizer.poseExpHelper(relativeXDiff, relativeYDiff, imuYawDiff);

        this.backOdo = newBackOdo;
        this.rightOdo = newRightOdo;
        this.imuYaw = newImuYaw;

        return delta;
    }
}
