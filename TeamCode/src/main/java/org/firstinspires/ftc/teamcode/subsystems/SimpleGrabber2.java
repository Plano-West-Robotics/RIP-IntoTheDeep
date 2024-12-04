package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class SimpleGrabber2 {
    private final Hardware hardware;

    public enum WristState {
        BUCKET(1.0), UP(0.3), DOWN(0.0);

        final double pos;
        WristState(double pos) {
            this.pos = pos;
        }
    }

    public SimpleGrabber2(Hardware hardware) {
        this.hardware = hardware;
    }

    public void wristTo(WristState pos) {
        hardware.wristL.setPosition(pos.pos);
//        hardware.wristR.setPosition(pos.pos);
    }

    public void swivelTo(double pos) {
        hardware.swivel.setPosition(pos);
    }

    public void closeClaw() {
        hardware.intake.setPosition(0);
    }

    public void openClaw() {
        hardware.intake.setPosition(1);
    }
}
