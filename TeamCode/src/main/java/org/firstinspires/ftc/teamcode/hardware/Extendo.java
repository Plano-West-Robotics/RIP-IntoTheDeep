package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.CheckResult;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.units.Distance;

public class Extendo extends TimedServo.Pair {
    public static final double IN = 0.0;
    public static final double OUT_GRABBER_UP = 0.74; // horizontal expansion limit
    public static final double OUT_GRABBER_DOWN = 1.0;

    public Extendo(RawHardware raw, Hardware.InitialConfiguration _initial) {
        // calibrated on 2024-02-18
        //            in - out
        // extendL  0.05 - 0.82
        // extendR  0.95 - 0.18
        super(
                raw.extendL,
                raw.extendR,
                1.0, // TODO:
                0.0,
                0.00, 0.82,
                1.00, 0.18
        );
    }

    static final Distance C = Distance.inMM(180);
    static final Distance K1 = Distance.inMM(377.33166); // 436.767
    static final double K2 = 1.50773; // 1.13112
    private double posFromClawX(Distance x) {
        double x2 = x.sub(C).div(K1);
        if (x2 < 0) return 0;
        if (x2 > 1) return 1;
        return Math.asin(x2) / K2;
    }

    public void setClawX(Distance x) {
        this.setPosition(posFromClawX(x));
    }

    @CheckResult(suggest = "setClawX(Distance)")
    public Action toClawX(Distance x) {
        return this.goTo(posFromClawX(x));
    }
}
