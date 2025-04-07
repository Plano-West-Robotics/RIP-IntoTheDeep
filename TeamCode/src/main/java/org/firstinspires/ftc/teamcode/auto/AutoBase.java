package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;

public abstract class AutoBase extends LinearOpMode {
    Hardware hardware;
    Poser poser;

    public enum LeftOrRight {
        LEFT, RIGHT
    }

    public static Pose computeInitialPose(LeftOrRight location, boolean withPreload) {
        // starting tile
        double startingX = 0;
        switch (location) {
            case LEFT:
                startingX = -1.5;
                break;
            case RIGHT:
                startingX = 0.5;
                break;
            default:
                throw new RuntimeException();
        }
        Pose initialTile = new Pose(
                Distance2.inTiles(startingX, -2.5),
                Angle.LEFT
        );

        // position within that tile
        Pose initialPose = initialTile.then(new Pose(
                new Distance2(
                        // in the ROBOT's coordinate scheme (b/c .then)
                        Distance.ONE_TILE_WITHOUT_BORDER.sub(Distance.ROBOT_LENGTH).neg(),
                        Distance.ONE_TILE_WITHOUT_BORDER.sub(Distance.ROBOT_WIDTH)
                ).div(2),
                Angle.ZERO
        ));

        if (withPreload && location.equals(LeftOrRight.RIGHT)) {
            initialPose = initialPose.then(new Pose(Distance2.ZERO, Angle.BACKWARD));
        }

        return initialPose;
    }

    public void setup(LeftOrRight location, boolean withPreload) {
        this.hardware = new Hardware(
                this,
                withPreload ? Hardware.InitialConfiguration.AUTO_PRELOAD : Hardware.InitialConfiguration.AUTO
        );

        this.poser = new Poser(hardware, 1.0, false, computeInitialPose(location, withPreload));
    }

    public void waitForStartWithClaw() {
        while (!this.isStarted() && !Thread.currentThread().isInterrupted()) {
            if (gamepad1.a || gamepad2.a) hardware.outClaw.open();
            else hardware.outClaw.close();

            Thread.yield();
        }
    }
}
