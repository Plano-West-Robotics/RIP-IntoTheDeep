package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Pose;

public class DriveEncoderLocalizer implements DeltaLocalizer {
    Hardware hardware;

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

    public DriveEncoderLocalizer(Hardware hardware) {
        this.hardware = hardware;

        this.fl = hardware.fl.getCurrentPosition();
        this.fr = hardware.fr.getCurrentPosition();
        this.bl = hardware.bl.getCurrentPosition();
        this.br = hardware.br.getCurrentPosition();
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) { }
        this.imuYaw = Angle.inRadians(hardware.getYaw(AngleUnit.RADIANS));
    }

    public Pose updateWithDelta() {
        int newFl = hardware.fl.getCurrentPosition();
        int newFr = hardware.fr.getCurrentPosition();
        int newBl = hardware.bl.getCurrentPosition();
        int newBr = hardware.br.getCurrentPosition();
        Angle newImuYaw = Angle.inRadians(hardware.getYaw(AngleUnit.RADIANS));

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
