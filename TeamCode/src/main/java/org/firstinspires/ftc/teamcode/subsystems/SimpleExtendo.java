package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class SimpleExtendo {
    private final Hardware hardware;

    public SimpleExtendo(Hardware hardware) {
        this.hardware = hardware;
    }

    /**
     * Move the extendo to the given position.
     * @param x 0.0 is in, 1.0 is out
     */
    public void goTo(double x) {
        this.hardware.extendL.setPosition(x);
        this.hardware.extendR.setPosition(x);
    }
}
