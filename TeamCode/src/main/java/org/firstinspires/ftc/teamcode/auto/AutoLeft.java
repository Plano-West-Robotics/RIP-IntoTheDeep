package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Left Auto (preload to basket)", preselectTeleOp = "DDDDDDDDD")
public class AutoLeft extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.LEFT, false);
        hardware.bucketL.setPosition(0);
        hardware.wristL.setPosition(0.8); // HACK

        DeltaTimer dter = new DeltaTimer();

        waitForStart();

        poser.goTo(
                Distance.inTiles(-2.5).add(Distance.inInches(4)),
                Distance.inTiles(-2.5).add(Distance.inInches(5))
        ).run();
        poser.moveBy(Angle.inDegrees(-45)).run();

        dter.poll();
        lift.setTarget(4200);
        while (lift.isBusy()) lift.update(dter.poll());

        hardware.bucketL.setPosition(1);
        sleep(2000);

        hardware.bucketL.setPosition(0);
        sleep(2000);

        dter.poll();
        lift.setTarget(0);
        while (lift.isBusy()) lift.update(dter.poll());
    }
}
