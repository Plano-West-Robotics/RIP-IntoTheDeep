package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.util.Encoder;

public class Lift {
    private final DcMotorEx liftL, liftR;
    private final RevTouchSensor limit;
    private final Encoder liftEncoder;

    public Lift(RawHardware raw, Hardware.InitialConfiguration _initial) {
        this.liftL = raw.liftL;
        this.liftR = raw.liftR;
        this.limit = raw.liftLimit;
        this.liftEncoder = raw.liftEncoder;

        // N.B. never set to RUN_USING_ENCODER. please.
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

    public boolean isDown() {
        return limit.isPressed();
    }

    public int encoder() {
        return -liftEncoder.getCurrentPosition();
    }
}
