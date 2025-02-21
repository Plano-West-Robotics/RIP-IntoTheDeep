package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.units.Distance;

@Autonomous(name = "Right Auto (park)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight1 extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, false);

        waitForStart();

        poser.goTo(Distance.inTiles(2), Distance.inTiles(-2.5)).run();
    }
}
