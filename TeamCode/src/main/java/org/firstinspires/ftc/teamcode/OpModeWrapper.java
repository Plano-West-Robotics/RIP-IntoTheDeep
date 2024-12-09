package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.teleop.Gamepads;

public abstract class OpModeWrapper extends OpMode {
    protected Hardware hardware;
    protected Gamepads gamepads;

    @Override
    public final void init() {
        this.hardware = new Hardware(this);
        this.gamepads = new Gamepads(this.gamepad1, this.gamepad2);

        this.setup();
    }

    public void setup() {}

    @Override
    public final void loop() {
        this.gamepads.update(this.gamepad1, this.gamepad2);

        this.run();
    }

    public void run() {}
}
