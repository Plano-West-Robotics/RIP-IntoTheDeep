package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class SimpleGrabber {
    private final Hardware hardware;

    public enum State {
        BUCKET(1.0), UP(0.3), DOWN(0.0);

        final double pos;
        State(double pos) {
            this.pos = pos;
        }
    }

    public SimpleGrabber(Hardware hardware) {
        this.hardware = hardware;
    }

    public void goTo(State pos) {
        hardware.wristL.setPosition(pos.pos);
//        hardware.wristR.setPosition(pos.pos);
    }
}
