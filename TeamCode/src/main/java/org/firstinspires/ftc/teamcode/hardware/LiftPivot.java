package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class LiftPivot {
    public static final int RANGE = 1450;

    private final DcMotorEx pivotL, pivotR;

    public LiftPivot(RawHardware raw, Hardware.InitialConfiguration _initial) {
        this.pivotL = raw.pivotL;
        this.pivotR = raw.pivotR;

        pivotL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pivotR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pivotL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivotR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pivotL.setDirection(DcMotorSimple.Direction.FORWARD);
        pivotR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power) {
        pivotL.setPower(power);
        pivotR.setPower(power);
    }

    public void setPowers(double powL, double powR) {
        pivotL.setPower(powL);
        pivotR.setPower(powR);
    }

    public int encoder() {
        return pivotL.getCurrentPosition();
    }
}
