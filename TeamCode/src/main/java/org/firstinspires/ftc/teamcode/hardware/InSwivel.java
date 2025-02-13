package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.units.Angle;

public class InSwivel extends TimedServo {
    public static final double LEFT = 2/3.;
    public static final double MIDDLE = 1/3.;
    public static final double RIGHT = 0;
    public static final double TRANSFER = 1;

    public InSwivel(RawHardware raw, Hardware.InitialConfiguration initial) {
        // calibrated on 2024-02-13
        // inSwivel
        // right: 0.0
        // mid: 0.333
        // left: 0.667
        // bucket: 1.0
        super(
                raw.inSwivel,
                1/(0.50),
                initial.branch(TRANSFER, TRANSFER, TRANSFER),
                0.00, 1.00
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
