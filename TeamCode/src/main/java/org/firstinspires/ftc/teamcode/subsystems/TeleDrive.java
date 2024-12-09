package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Imu;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class TeleDrive {
    private Drivetrain drive;
    private Imu imu;
    private double speed;
    private boolean fieldOriented = false;
    private Angle yawOffset = Angle.ZERO;

    public TeleDrive(Hardware hardware, double speed) {
        this(hardware.drivetrain, hardware.imu, speed);
    }

    public TeleDrive(Drivetrain drive, Imu imu, double speed) {
        this.drive = drive;
        this.imu = imu;
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
        yawOffset = imu.getYaw();
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
            pow = pow.rot(imu.getYaw().sub(yawOffset).neg());
        }

        drive.drive(pow.mul(speed), turn * speed);
    }

    public void stop() {
        drive.stop();
    }
}
