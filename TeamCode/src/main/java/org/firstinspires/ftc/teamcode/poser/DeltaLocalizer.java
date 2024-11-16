package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;

public interface DeltaLocalizer {
    Pose updateWithDelta();

    static Pose poseExpHelper(double xDiff, double yDiff, double yawDiff) {
        return poseExpHelper(xDiff, yDiff, Angle.inRadians(yawDiff));
    }

    static Pose poseExpHelper(double xDiff, double yDiff, Angle yawDiff) {
        double poseExponentiationX = yawDiff.cosc();
        double poseExponentiationY = yawDiff.sinc();

        Distance2 posDiff = Distance2.inMM(
                xDiff * poseExponentiationY - yDiff * poseExponentiationX,
                xDiff * poseExponentiationX + yDiff * poseExponentiationY
        );

        return new Pose(posDiff, yawDiff);
    }
}
