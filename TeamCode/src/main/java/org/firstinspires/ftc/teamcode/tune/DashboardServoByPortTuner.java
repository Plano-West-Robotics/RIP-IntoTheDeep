package org.firstinspires.ftc.teamcode.tune;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

@TeleOp(group = "tune")
public class DashboardServoByPortTuner extends OpMode {
    @Override
    public void init() {
        FtcDashboard db = FtcDashboard.getInstance();

        for (ServoControllerEx sc : hardwareMap.getAll(ServoControllerEx.class)) {
            String scName = hardwareMap.getNamesOf(sc).iterator().next();
            if (scName == null) continue;
            
            for (int i = 0; i < 6; i++) {
                ServoImplEx s = new ServoImplEx(sc, i, ServoConfigurationType.getStandardServoType());
                String name = scName + " Port " + i;

                db.addConfigVariable(this.getClass().getSimpleName(), name, new ValueProvider<String>() {
                    final ServoImplEx servo = s;
                    double pos = 0.5;
                    String verbatimInput = "";

                    @Override
                    public String get() {
                        return verbatimInput;
                    }

                    @Override
                    public void set(String value) {
                        if (value.equals("")) {
                            this.servo.setPwmDisable();
                        } else {
                            try {
                                this.pos = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                return;
                            }

                            this.servo.setPwmEnable();
                            this.servo.setPosition(this.pos);
                        }

                        this.verbatimInput = value;
                    }
                }, true);
            }
        }
    }

    @Override
    public void loop() {}
}
