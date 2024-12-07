package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class Encoder {
    private final DcMotorController controller;
    private final int port;

    public Encoder(DcMotor dcMotor) {
        this.controller = dcMotor.getController();
        this.port = dcMotor.getPortNumber();
    }

    public int getCurrentPosition() {
        return this.controller.getMotorCurrentPosition(port);
    }
}
