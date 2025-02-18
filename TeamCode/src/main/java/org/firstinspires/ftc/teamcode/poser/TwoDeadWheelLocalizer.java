package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Pose;

public class TwoDeadWheelLocalizer implements DeltaLocalizer {
    Hardware hardware;

    // sensor readings
    int xOdo;
    int yOdo;
    public Angle imuYaw;

    public static final double MM_PER_ENCODER_TICK_OPENODO = (35 * Math.PI) / 8192;
    public static final double MM_PER_ENCODER_TICK_SWINGARM = (48 * Math.PI) / 2000;
    public static final double MM_PER_ENCODER_TICK_4BAR = (32 * Math.PI) / 2000;

    // positive is e.g. along the +Y direction for the X pod (so left for the forward/backward pod)
    public static final Distance X_ODO_LEVER_ARM = Distance.inInches(3 + 15/16.).neg();
    public static final Distance Y_ODO_LEVER_ARM = Distance.inInches(6 + 9/16.).neg();
    // positive is e.g. moving the robot in the +X direction increases the encoder for the X pod
    // NOTE: some buffoon decided to add a thing to the sdk where motor and encoder direction is no
    //       longer dictated by what the motor considers positive but is always clockwise; this
    //       leads to the values reported in DashboardMotorTuner not matching the values read by
    //       Encoder in sign despite the fact that DashboardMotorTuner sets the direction to
    //       FORWARD. beware.
    public static final int X_ODO_DIR = 1;
    public static final int Y_ODO_DIR = 1;

    public static final double X_ODO_MM_PER_TICK = MM_PER_ENCODER_TICK_4BAR;
    public static final double Y_ODO_MM_PER_TICK = MM_PER_ENCODER_TICK_4BAR;

    public TwoDeadWheelLocalizer(Hardware hardware) {
        this.hardware = hardware;

        this.xOdo = hardware.rightOdo.getCurrentPosition();
        this.yOdo = hardware.backOdo.getCurrentPosition();
        try { Thread.sleep(1000); }
        catch (InterruptedException ignored) { }
        this.imuYaw = hardware.imu.getYaw();
    }

    public Pose updateWithDelta() {
        int newXOdo = hardware.rightOdo.getCurrentPosition();
        int newYOdo = hardware.backOdo.getCurrentPosition();
        Angle newImuYaw = hardware.imu.getYaw();

        double xOdoDiff = (newXOdo - this.xOdo) * X_ODO_DIR * X_ODO_MM_PER_TICK;
        double yOdoDiff = (newYOdo - this.yOdo) * Y_ODO_DIR * Y_ODO_MM_PER_TICK;
        Angle imuYawDiff = newImuYaw.sub(this.imuYaw);

        // yawDiff is in rad, rest in mm
        double yawDiff = imuYawDiff.modSigned().valInRadians();
        double relativeXDiff = xOdoDiff + yawDiff * X_ODO_LEVER_ARM.valInMM();
        double relativeYDiff = yOdoDiff - yawDiff * Y_ODO_LEVER_ARM.valInMM();

        Pose delta = DeltaLocalizer.poseExpHelper(relativeXDiff, relativeYDiff, imuYawDiff);

        this.xOdo = newXOdo;
        this.yOdo = newYOdo;
        this.imuYaw = newImuYaw;

        return delta;
    }
}
