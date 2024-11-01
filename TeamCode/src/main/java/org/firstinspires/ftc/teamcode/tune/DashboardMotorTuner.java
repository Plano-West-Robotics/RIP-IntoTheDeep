package org.firstinspires.ftc.teamcode.tune;

import android.util.Pair;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;

@TeleOp(group = "tune")
public class DashboardMotorTuner extends OpMode {
    ArrayList<Pair<String, DcMotorEx>> motors = new ArrayList<>();

    @Override
    public void init() {
        FtcDashboard db = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, db.getTelemetry());

        for (DcMotorEx m : hardwareMap.getAll(DcMotorEx.class)) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setDirection(DcMotorSimple.Direction.FORWARD);
            String name = hardwareMap.getNamesOf(m).iterator().next();
            if (name == null) continue;

            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            m.setMotorDisable();
            this.motors.add(new Pair<>(name, m));
            db.addConfigVariable(this.getClass().getSimpleName(), name, new ValueProvider<String>() {
                final DcMotorEx motor = m;
                double power = 0.0;
                String verbatimInput = "";

                @Override
                public String get() {
                    return verbatimInput;
                }

                @Override
                public void set(String value) {
                    if (value.equals("")) {
                        this.motor.setMotorDisable();
                    } else {
                        try {
                            this.power = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            return;
                        }

                        this.motor.setMotorEnable();
                        this.motor.setPower(this.power);
                    }

                    this.verbatimInput = value;
                }
            }, true);
        }
    }

    @Override
    public void loop() {
        for (Pair<String, DcMotorEx> pair : this.motors) {
            telemetry.addData(pair.first, pair.second.getCurrentPosition());
        }
        telemetry.update();
    }
}
