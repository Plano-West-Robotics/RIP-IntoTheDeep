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
    public final OutWrist outWrist;
    public final OutClaw outClaw;
    public final OutShoulder outShoulder;

    public final Extendo extend;
    public final InWrist inWrist;
    public final InSwivel inSwivel;
    public final InClaw inClaw;

    public final DistanceSensors dist;
    public final Imu imu;
    public final Encoder backOdo, rightOdo;

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

        HardwareMap hardwareMap = opMode.hardwareMap;
        this.drivetrain = new Drivetrain(hardwareMap);
        this.lift = new Lift(hardwareMap, initial);
        this.outWrist = new OutWrist(hardwareMap, initial);
        this.outClaw = new OutClaw(hardwareMap, initial);
        this.outShoulder = new OutShoulder(hardwareMap, initial);
        this.extend = new Extendo(hardwareMap, initial);
        this.inWrist = new InWrist(hardwareMap, initial);
        this.inSwivel = new InSwivel(hardwareMap, initial);
        this.inClaw = new InClaw(hardwareMap, initial);
        this.dist = new DistanceSensors(hardwareMap);
        this.imu = new Imu(hardwareMap);

        backOdo = new Encoder(hardwareMap.get(DcMotor.class, "backOdo"));
        rightOdo = new Encoder(hardwareMap.get(DcMotor.class, "rightOdo"));

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }
}
