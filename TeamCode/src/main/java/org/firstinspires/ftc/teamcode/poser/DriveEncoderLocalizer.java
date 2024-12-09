package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Imu;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Pose;

public class DriveEncoderLocalizer implements DeltaLocalizer {
    Drivetrain drivetrain;
    Imu imu;

    // sensor readings
    int fl;
    int fr;
    int bl;
    int br;
    Angle imuYaw;

    public static final double MM_PER_ENCODER_TICK = (96 * Math.PI) / 384.5;
    public static final double TRACK_WIDTH = 355;
    public static final double DEGREES_PER_MM = 360 / (TRACK_WIDTH * Math.PI);

    private static final double X_AXIS_CALIB = 1.0390625;
    private static final double Y_AXIS_CALIB = 1.2734375;

    public DriveEncoderLocalizer(Drivetrain drivetrain, Imu imu) {
        this.drivetrain = drivetrain;
        this.imu = imu;

        this.fl = drivetrain.flEncoder();
        this.fr = drivetrain.frEncoder();
        this.bl = drivetrain.blEncoder();
        this.br = drivetrain.brEncoder();
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) { }
        this.imuYaw = imu.getYaw();
    }

    public Pose updateWithDelta() {
        int newFl = drivetrain.flEncoder();
        int newFr = drivetrain.frEncoder();
        int newBl = drivetrain.blEncoder();
        int newBr = drivetrain.brEncoder();
        Angle newImuYaw = imu.getYaw();

        int flDiff = newFl - this.fl;
        int frDiff = newFr - this.fr;
        int blDiff = newBl - this.bl;
        int brDiff = newBr - this.br;
        Angle imuYawDiff = newImuYaw.sub(this.imuYaw);

        // fl = powerY + powerX + turn + noop
        // fr = powerY - powerX - turn + noop
        // bl = powerY - powerX + turn - noop
        // br = powerY + powerX - turn - noop

        double relativeXDiff = ((flDiff + frDiff + blDiff + brDiff) / 4.) * MM_PER_ENCODER_TICK / X_AXIS_CALIB;
        double relativeYDiff = ((-flDiff + frDiff + blDiff - brDiff) / 4.) * MM_PER_ENCODER_TICK / Y_AXIS_CALIB;
        // unused
        // double yawDiff = ((flDiff - frDiff + blDiff - brDiff) / 4.) * MM_PER_ENCODER_TICK * DEGREES_PER_MM;
        // double noopDiff = (flDiff + frDiff - blDiff - brDiff) / 4.;

        Pose delta = DeltaLocalizer.poseExpHelper(relativeXDiff, relativeYDiff, imuYawDiff);

        this.fl = newFl;
        this.fr = newFr;
        this.bl = newBl;
        this.br = newBr;
        this.imuYaw = newImuYaw;

        return delta;
    }
}
