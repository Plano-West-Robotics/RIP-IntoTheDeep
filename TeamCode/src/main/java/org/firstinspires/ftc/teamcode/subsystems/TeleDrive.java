package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Imu;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class TeleDrive {
    private final Drivetrain drive;
    private final Imu imu;

    private double speed;
    private Angle targetYaw;
    private double turnAssistTime;

    private Vector2 pow;
    private double turn;

    public TeleDrive(Hardware hardware, double speed) {
        this(hardware.drivetrain, hardware.imu, speed);
    }

    public TeleDrive(Drivetrain drive, Imu imu, double speed) {
        this.drive = drive;
        this.imu = imu;
        this.speed = speed;
        this.targetYaw = imu.getYaw();
        this.turnAssistTime = 0.0;

        this.pow = Vector2.ZERO;
        this.turn = 0.0;
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public double getSpeed() {
        return speed;
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
        this.pow = pow;
        this.turn = turn;
    }

    public void update(double dt) {
        Angle currYaw = imu.getYaw();

        if (turn == 0.0) {
            turnAssistTime -= dt;
            turnAssistTime = Math.max(turnAssistTime, 0.0);
        } else {
            turnAssistTime = 0.5; // secs until turn assist kicks in
        }

        Vector2 localPow = pow.mul(speed);

        double localTurn;
        if (turnAssistTime == 0.0) {
            localTurn = Math.tanh(targetYaw.sub(currYaw).modSigned().div(Angle.inDegrees(25)));
        } else {
            localTurn = turn * speed;
            targetYaw = currYaw;
        }

        drive.drive(localPow, localTurn);
    }
}
