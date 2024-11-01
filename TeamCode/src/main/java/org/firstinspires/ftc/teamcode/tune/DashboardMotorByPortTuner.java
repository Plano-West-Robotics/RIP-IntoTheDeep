package org.firstinspires.ftc.teamcode.tune;

import android.util.Pair;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

import java.util.ArrayList;

@TeleOp(group = "tune")
public class DashboardMotorByPortTuner extends OpMode {
    ArrayList<Pair<String, DcMotorEx>> motors = new ArrayList<>();

    @Override
    public void init() {
        FtcDashboard db = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, db.getTelemetry());

        for (DcMotorControllerEx mc : hardwareMap.getAll(DcMotorControllerEx.class)) {
            String mcName = hardwareMap.getNamesOf(mc).iterator().next();
            if (mcName == null) continue;

            for (int i = 0; i < 4; i++) {
                DcMotorEx m = new DcMotorImplEx(mc, i);
                String name = mcName + " Port " + i;

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
    }

    @Override
    public void loop() {
        for (Pair<String, DcMotorEx> pair : this.motors) {
            telemetry.addData(pair.first, pair.second.getCurrentPosition());
        }
        telemetry.update();
    }
}
