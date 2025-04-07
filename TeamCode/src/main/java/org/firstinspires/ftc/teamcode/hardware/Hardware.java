package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DashboardTelemetryWrapper;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class Hardware {
    public final Drivetrain drivetrain;

    public final Lift lift;
    public final LiftPivot pivot;
    public final OutWrist outWrist;
    public final OutClaw outClaw;
    public final OutSwivel outSwivel;
    public final OutShoulder outShoulder;

    public final Extendo extend;
    public final InWrist inWrist;
    public final InSwivel inSwivel;
    public final InClaw inClaw;

    public final Odometry odo;
    public final Imu imu;

    public final OpMode opMode;
    public final DashboardTelemetryWrapper dashboardTelemetry;

    public enum InitialConfiguration {
        TELEOP, AUTO, AUTO_PRELOAD;

        public <T> T branch(T teleop, T auto, T autoPreload) {
            switch (this) {
                case TELEOP:
                    return teleop;
                case AUTO:
                    return auto;
                case AUTO_PRELOAD:
                    return autoPreload;
                default:
                    throw new RuntimeException(String.format("unhandled initial configuration '%s'", this.name()));
            }
        }
    }

    public Hardware(OpMode opMode, InitialConfiguration initial) {
        this.opMode = opMode;

        this.dashboardTelemetry = new DashboardTelemetryWrapper(FtcDashboard.getInstance());
        opMode.telemetry = new MultipleTelemetry(opMode.telemetry, this.dashboardTelemetry);

        RawHardware raw = new RawHardware(opMode.hardwareMap);
        this.drivetrain = new Drivetrain(raw);
        this.lift = new Lift(raw, initial);
        this.pivot = new LiftPivot(raw, initial);
        this.outWrist = new OutWrist(raw, initial);
        this.outClaw = new OutClaw(raw, initial);
        this.outSwivel = new OutSwivel(raw, initial);
        this.outShoulder = new OutShoulder(raw, initial);
        this.extend = new Extendo(raw, initial);
        this.inWrist = new InWrist(raw, initial);
        this.inSwivel = new InSwivel(raw, initial);
        this.inClaw = new InClaw(raw, initial);
        this.odo = new Odometry(raw);
        this.imu = new Imu(raw);
    }
}
