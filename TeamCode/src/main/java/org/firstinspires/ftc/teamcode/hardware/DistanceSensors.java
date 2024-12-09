package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

public class DistanceSensors {
    // NOTE: left is the *robot*'s left
    private final DistanceSensor distL, distR;

    private double readingL;
    private double readingR;

    private static final Distance DIST_BETWEEN_SENSORS = Distance.inInches(6 + 13/16.);

    public DistanceSensors(HardwareMap hardwareMap) {
        this.distL = hardwareMap.get(DistanceSensor.class, "distL");
        this.distR = hardwareMap.get(DistanceSensor.class, "distR");
    }

    public void doI2cRead() {
        this.readingL = this.distL.getDistance(DistanceUnit.MM);
        this.readingR = this.distR.getDistance(DistanceUnit.MM);
    }

    public Distance getDistL() {
        return Distance.inMM(this.readingL);
    }

    public Distance getDistR() {
        return Distance.inMM(this.readingR);
    }

    public boolean areValuesSane() {
        // at the very least, this method must check that the distances are not 65535 or Infinity (both used as placeholder values)
        // we also check that it is within 1 metre as a heuristic to see if it might be too far away to be precise
        return this.readingL < 1000 && this.readingR < 1000;
    }

    /**
     * zero means directly facing the object, positive means the robot is turned positively relative to that
     */
    public Angle angleAwayFromTarget() {
        Distance distDiff = this.getDistR().sub(this.getDistL());
        double tan = distDiff.div(DIST_BETWEEN_SENSORS);
        return Angle.inRadians(Math.atan(tan));
    }

    /**
     * perpendicular distance from the midpoint between the sensors to the plane of the target
     */
    public Distance distanceFromTarget() {
        Distance l = this.getDistL();
        Distance r = this.getDistR();
        Distance mid = l.add(r).div(2);
//        return mid.mul(this.angleAwayFromTarget().cos());
        return mid.div(Math.sqrt( Math.pow(r.sub(l).div(DIST_BETWEEN_SENSORS), 2) + 1 ));
    }
}
