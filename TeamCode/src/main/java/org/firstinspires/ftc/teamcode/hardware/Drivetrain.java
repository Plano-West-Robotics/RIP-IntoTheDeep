package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.units.Vector2;

public class Drivetrain {
    private final DcMotorEx fl, fr, bl, br;

    public Drivetrain(HardwareMap hardwareMap) {
        this.fl = hardwareMap.get(DcMotorEx.class, "fl");
        this.fr = hardwareMap.get(DcMotorEx.class, "fr");
        this.bl = hardwareMap.get(DcMotorEx.class, "bl");
        this.br = hardwareMap.get(DcMotorEx.class, "br");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param x forward is positive
     * @param y left is positive
     * @param turn ccw is positive
     */
    public void drive(double x, double y, double turn) {
        this.drive(new Vector2(x, y), turn);
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param pow forward is positive x, left is positive y
     * @param turn ccw is positive
     */
    public void drive(Vector2 pow, double turn) {
        fl.setPower(pow.x - pow.y - turn);
        fr.setPower(pow.x + pow.y + turn);
        bl.setPower(pow.x + pow.y - turn);
        br.setPower(pow.x - pow.y + turn);
    }

    public void stop() {
        drive(0, 0, 0);
    }

    public int flEncoder() {
        return fl.getCurrentPosition();
    }

    public int frEncoder() {
        return fr.getCurrentPosition();
    }

    public int blEncoder() {
        return bl.getCurrentPosition();
    }

    public int brEncoder() {
        return br.getCurrentPosition();
    }
}
