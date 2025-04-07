package org.firstinspires.ftc.teamcode.tune;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.InClaw;
import org.firstinspires.ftc.teamcode.hardware.InSwivel;
import org.firstinspires.ftc.teamcode.hardware.InWrist;
import org.firstinspires.ftc.teamcode.hardware.Lift;
import org.firstinspires.ftc.teamcode.hardware.LiftPivot;
import org.firstinspires.ftc.teamcode.hardware.OutClaw;
import org.firstinspires.ftc.teamcode.hardware.OutShoulder;
import org.firstinspires.ftc.teamcode.hardware.OutSwivel;
import org.firstinspires.ftc.teamcode.hardware.OutWrist;
import org.firstinspires.ftc.teamcode.hardware.RawHardware;
import org.firstinspires.ftc.teamcode.hardware.TimedServo;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.subsystems.PivotControl;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

import java.util.Objects;

@TeleOp(group = "tune")
public class KillMeNow extends OpMode {
    RawHardware raw;
    ControlledLift lift;
    PivotControl pivot;
    DeltaTimer dter = new DeltaTimer(true);

    @Override
    public void init() {
        raw = new RawHardware(hardwareMap);

        FtcDashboard db = FtcDashboard.getInstance();

        Object[] servos = new Object[] {
                new OutShoulder(raw, Hardware.InitialConfiguration.TELEOP),
                new OutWrist(raw, Hardware.InitialConfiguration.TELEOP),
                new OutSwivel(raw, Hardware.InitialConfiguration.TELEOP),
                new OutClaw(raw, Hardware.InitialConfiguration.TELEOP),
                new Extendo(raw, Hardware.InitialConfiguration.TELEOP),
                new InWrist(raw, Hardware.InitialConfiguration.TELEOP),
                new InSwivel(raw, Hardware.InitialConfiguration.TELEOP),
                new InClaw(raw, Hardware.InitialConfiguration.TELEOP),
        };

        for (Object o : servos) {
            double initalPos = (o instanceof TimedServo) ? ((TimedServo)o).getPosition() : ((TimedServo.Pair)o).getPosition();

            db.addConfigVariable(this.getClass().getSimpleName(), o.getClass().getSimpleName(), new ValueProvider<Double>() {
                double pos = initalPos;

                @Override
                public Double get() {
                    return pos;
                }

                @Override
                public void set(Double value) {
                    this.pos = value;
                    if (o instanceof TimedServo) {
                        ((TimedServo) o).setPosition(pos);
                    } else {
                        ((TimedServo.Pair) o).setPosition(pos);
                    }
                }
            }, true);
        }

        lift = new ControlledLift(new Lift(raw, Hardware.InitialConfiguration.TELEOP), 0);
        db.addConfigVariable(this.getClass().getSimpleName(), "Lift", new ValueProvider<Integer>() {
            int pos = 0;

            @Override
            public Integer get() {
                return pos;
            }

            @Override
            public void set(Integer value) {
                this.pos = value;
                lift.setTarget(pos);
            }
        }, true);

        pivot = new PivotControl(new LiftPivot(raw, Hardware.InitialConfiguration.TELEOP));
        db.addConfigVariable(this.getClass().getSimpleName(), "Pivot", new ValueProvider<Integer>() {
            int pos = 0;

            @Override
            public Integer get() {
                return pos;
            }

            @Override
            public void set(Integer value) {
                this.pos = value;
                pivot.setTarget(pos);
            }
        }, true);
    }

    @Override
    public void loop() {
        double dt = dter.poll();
        telemetry.addData("lift", lift.getCurrentPos());
        lift.update(dt);
        pivot.update(dt);
    }
}
