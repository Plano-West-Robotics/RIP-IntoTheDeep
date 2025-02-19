package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Lift {
    private final DcMotorEx liftL, liftR, liftL2, liftR2;
    private final RevTouchSensor limitL, limitR;

    public Lift(RawHardware raw, Hardware.InitialConfiguration _initial) {
        this.liftL = raw.liftL;
        this.liftR = raw.liftR;
        this.liftL2 = raw.liftL2;
        this.liftR2 = raw.liftR2;
        this.limitL = raw.liftLimitL;
        this.limitR = raw.liftLimitR;

        // N.B. never set to RUN_USING_ENCODER. please.
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftL2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftL2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftL.setDirection(DcMotorSimple.Direction.REVERSE);
        liftL2.setDirection(DcMotorSimple.Direction.REVERSE);
        liftR.setDirection(DcMotorSimple.Direction.FORWARD);
        liftR2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPower(double power) {
        liftL.setPower(power);
        liftL2.setPower(power);
        liftR.setPower(power);
        liftR2.setPower(power);
    }

    public void setPowers(double powL, double powR) {
        liftL.setPower(powL);
        liftL2.setPower(powL);
        liftR.setPower(powR);
        liftR2.setPower(powR);
    }

    public boolean isLeftDown() {
        return limitL.isPressed();
    }

    public boolean isRightDown() {
        return limitR.isPressed();
    }

    public int leftEncoder() {
        return liftL.getCurrentPosition();
    }

    public int rightEncoder() {
        return liftR.getCurrentPosition();
    }
}
