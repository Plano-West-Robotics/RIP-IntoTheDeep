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

    public void setup(LeftOrRight location, boolean withSpecimen) {
        this.hardware = new Hardware(this);

        if (withSpecimen) {
            hardware.claw.close();
        } else {
            hardware.claw.open();
        }

        // starting pose calculations

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

        if (withSpecimen) {
            initialPose = initialPose.then(new Pose(Distance2.ZERO, Angle.BACKWARD));
        }

        this.poser = new Poser(hardware, 0.9, false, initialPose);
    }
}
