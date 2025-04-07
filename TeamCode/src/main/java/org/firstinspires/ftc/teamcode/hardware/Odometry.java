package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.util.Encoder;

public class Odometry {
    private final Encoder backOdo, rightOdo;

    public Odometry(RawHardware raw) {
        this.backOdo = raw.backOdo;
        this.rightOdo = raw.rightOdo;
    }

    public int rightEncoder() {
        return rightOdo.getCurrentPosition();
    }

    public int backEncoder() {
        return backOdo.getCurrentPosition();
    }
}
