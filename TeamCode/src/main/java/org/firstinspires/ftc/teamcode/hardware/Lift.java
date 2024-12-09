package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift {
    private final DcMotorEx liftL, liftR;
    private final RevTouchSensor limitL, limitR;

    public Lift(HardwareMap hardwareMap) {
        this.liftL = hardwareMap.get(DcMotorEx.class, "liftL");
        this.liftR = hardwareMap.get(DcMotorEx.class, "liftR");
        this.limitL = hardwareMap.get(RevTouchSensor.class, "liftLimitL");
        this.limitR = hardwareMap.get(RevTouchSensor.class, "liftLimitR");

        // TODO: should be RUN_USING_ENCODER once both encoders are connected
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftL.setDirection(DcMotorSimple.Direction.REVERSE);
        liftR.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPower(double power) {
        liftL.setPower(power);
        liftR.setPower(power);
    }

    public void setPowers(double powL, double powR) {
        liftL.setPower(powL);
        liftR.setPower(powR);
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
