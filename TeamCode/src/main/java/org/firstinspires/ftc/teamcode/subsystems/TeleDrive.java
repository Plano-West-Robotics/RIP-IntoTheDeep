package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class TeleDrive {
    Hardware hardware;
    Drive drive;
    private double speed;
    private boolean fieldOriented = false;
    private Angle yawOffset = Angle.ZERO;

    public TeleDrive(Hardware hw, double speed) {
        this.hardware = hw;
        this.drive = new Drive(hw);
        this.speed = speed;
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setFieldOriented(boolean newFO) {
        fieldOriented = newFO;
    }

    public boolean getFieldOriented() {
        return fieldOriented;
    }

    public void toggleFieldOriented() {
        setFieldOriented(!getFieldOriented());
    }

    public void resetYaw() {
        yawOffset = hardware.getYaw();
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param x forward is positive
     * @param y left is positive
     * @param turn ccw is positive
     */
    public void drive(double x, double y, double turn) {
        drive.drive(new Vector2(x, y), turn);
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param pow forward is positive x, left is positive y
     * @param turn ccw is positive
     */
    public void drive(Vector2 pow, double turn) {
        if (getFieldOriented()) {
            pow = pow.rot(hardware.getYaw().sub(yawOffset).neg());
        }

        drive.drive(pow, turn);
    }

    public void stop() {
        drive.stop();
    }
}
