package org.firstinspires.ftc.teamcode.poser;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;

@TeleOp
public class PoserTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        Poser poser = new Poser(hardware, 0.5, false, new Pose(Distance2.ZERO, Angle.ZERO));

        while (!isStarted()) {
            poser.localizer.update();
            hardware.dashboardTelemetry.drawRobot(poser.localizer.getPoseEstimate());
            telemetry.update();
        }

        final double[] dest = {0, 0, 0};
        final Poser.Motion[] motion = {poser.goTo(new Pose(
                Distance2.inInches(dest[0], dest[1]),
                Angle.inDegrees(dest[2])
        ))};
        FtcDashboard.getInstance().addConfigVariable(this.getClass().getSimpleName(), "x", new ValueProvider<Double>() {
            @Override
            public Double get() {
                return dest[0];
            }

            @Override
            public void set(Double value) {
                dest[0] = value;
                motion[0] = poser.goTo(new Pose(
                        Distance2.inInches(dest[0], dest[1]),
                        Angle.inDegrees(dest[2])
                ));
            }
        }, true);
        FtcDashboard.getInstance().addConfigVariable(this.getClass().getSimpleName(), "y", new ValueProvider<Double>() {
            @Override
            public Double get() {
                return dest[1];
            }

            @Override
            public void set(Double value) {
                dest[1] = value;
                motion[0] = poser.goTo(new Pose(
                        Distance2.inInches(dest[0], dest[1]),
                        Angle.inDegrees(dest[2])
                ));
            }
        }, true);
        FtcDashboard.getInstance().addConfigVariable(this.getClass().getSimpleName(), "yaw", new ValueProvider<Double>() {
            @Override
            public Double get() {
                return dest[2];
            }

            @Override
            public void set(Double value) {
                dest[2] = value;
                motion[0] = poser.goTo(new Pose(
                        Distance2.inInches(dest[0], dest[1]),
                        Angle.inDegrees(dest[2])
                ));
            }
        }, true);

        while (opModeIsActive()) {
            motion[0].update();
            hardware.dashboardTelemetry.drawRobot(poser.localizer.getPoseEstimate());
            telemetry.update();
        }

//        while (opModeIsActive()) {
//            poser.goTo(new Pose(
//                    Distance2.inTiles(0, 1),
//                    Angle.inDegrees(-30)
//            )).run();
//            poser.goTo(new Pose(
//                    Distance2.inTiles(0, 0),
//                    Angle.ZERO
//            )).run();
//        }
    }
}
