package org.firstinspires.ftc.teamcode.tune;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(group = "tune")
public class DashboardServoTuner extends OpMode {
    @Override
    public void init() {
        FtcDashboard db = FtcDashboard.getInstance();

        for (ServoImplEx s : hardwareMap.getAll(ServoImplEx.class)) {
            s.scaleRange(0, 1);
            s.setDirection(Servo.Direction.FORWARD);
            String name = hardwareMap.getNamesOf(s).iterator().next();
            if (name == null) continue;

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

    @Override
    public void loop() {}
}
