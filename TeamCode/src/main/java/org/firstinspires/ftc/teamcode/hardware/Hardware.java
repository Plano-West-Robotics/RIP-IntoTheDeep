package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.DashboardTelemetryWrapper;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class Hardware {
    public final Drivetrain drivetrain;
    public final Lift lift;
    public final Claw claw;
    public final Extendo extend;
    public final Wrist wrist;
    public final Swivel swivel;
    public final IntakeClaw intake;
    public final Bucket bucket;
    public final DistanceSensors dist;
    public final Imu imu;
    public final Encoder backOdo, rightOdo;

    public final OpMode opMode;
    public final DashboardTelemetryWrapper dashboardTelemetry;

    // TODO: possible initial configuration parameter? it's Annoying to implement as
    //       an enum and right now there's only one initial configuration anyway
    //  2024-12-08: realising there is a second initial config - claw closed for preload
    public Hardware(OpMode opMode) {
        this.opMode = opMode;

        this.dashboardTelemetry = new DashboardTelemetryWrapper(FtcDashboard.getInstance());
        opMode.telemetry = new MultipleTelemetry(opMode.telemetry, this.dashboardTelemetry);

        HardwareMap hardwareMap = opMode.hardwareMap;
        this.drivetrain = new Drivetrain(hardwareMap);
        this.lift = new Lift(hardwareMap);
        this.claw = new Claw(hardwareMap);
        this.extend = new Extendo(hardwareMap);
        this.wrist = new Wrist(hardwareMap);
        this.swivel = new Swivel(hardwareMap);
        this.intake = new IntakeClaw(hardwareMap);
        this.bucket = new Bucket(hardwareMap);
        this.dist = new DistanceSensors(hardwareMap);
        this.imu = new Imu(hardwareMap);

        backOdo = new Encoder(hardwareMap.get(DcMotor.class, "backOdo"));
        rightOdo = new Encoder(hardwareMap.get(DcMotor.class, "rightOdo"));

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }
}
