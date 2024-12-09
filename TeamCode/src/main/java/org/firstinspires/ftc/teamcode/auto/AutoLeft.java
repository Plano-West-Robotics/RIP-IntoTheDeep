package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Left Auto (preload to basket)", preselectTeleOp = "DDDDDDDDD")
public class AutoLeft extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.LEFT, false);
        hardware.bucket.down();
        hardware.wrist.setPosition(0.8); // HACK

        ControlledLift lift = new ControlledLift(hardware);
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

        hardware.bucket.up();
        sleep(2000);

        hardware.bucket.down();
        sleep(2000);

        dter.poll();
        lift.setTarget(0);
        while (lift.isBusy()) lift.update(dter.poll());
    }
}
