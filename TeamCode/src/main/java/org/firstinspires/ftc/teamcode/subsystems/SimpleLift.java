package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;

public class SimpleLift {
    private final Hardware hardware;

    public SimpleLift(Hardware hardware) {
        this.hardware = hardware;
    }

    public void setPower(double power) {
        hardware.liftL.setPower(power);
        hardware.liftR.setPower(power);
    }

    public void setPowers(double powL, double powR) {
        hardware.liftL.setPower(powL);
        hardware.liftR.setPower(powR);
    }

    public boolean isLeftDown() {
        return hardware.liftLimitL.isPressed();
    }

    public boolean isRightDown() {
        return hardware.liftLimitR.isPressed();
    }

    public int leftEncoder() {
        return hardware.liftL.getCurrentPosition();
    }

    public int rightEncoder() {
        return hardware.liftR.getCurrentPosition();
    }
}
