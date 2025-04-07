package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.units.Angle;

public class OutSwivel extends TimedServo {
    public static final double LEFT = 2/3.;
    public static final double MIDDLE = 1/3.;
    public static final double RIGHT = 0;
    public static final double FRONT = 1;
    public static final double BACK = MIDDLE;

    public OutSwivel(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-05-01
        // outSwivel
        // right: 0.00
        // front: 0.94
        super(
                raw.outSwivel,
                1/(0.50), // TODO:
                initial.branch(FRONT, FRONT, FRONT),
                0.00, 0.94
        );
    }

    private double computePosition(Angle angle) {
        angle = angle.mul(2).modSigned().div(2);
        return MIDDLE + angle.div(Angle.inDegrees(90)) * (LEFT - MIDDLE);
    }

    public void setPosition(Angle angle) {
        this.setPosition(computePosition(angle));
    }

    @CheckResult(suggest = "setPosition(Angle)")
    public Action goTo(Angle angle) {
        return this.goTo(computePosition(angle));
    }
}
