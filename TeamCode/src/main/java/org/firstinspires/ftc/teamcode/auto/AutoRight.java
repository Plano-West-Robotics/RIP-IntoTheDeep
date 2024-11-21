package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(preselectTeleOp = "DDDDDDDDD")
@Disabled
public class AutoRight extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        // NOTE: doesn't work rn because i removed the HACK in AutoBase

        super.setup(LeftOrRight.RIGHT);
        hardware.claw.setPosition(1);

        DeltaTimer dter = new DeltaTimer();

        waitForStart();

        dter.poll();
        lift.setTarget(2000);
        while (lift.isBusy()) lift.update(dter.poll());

        poser.goTo(Distance.inTiles(0), Distance.inTiles(-1.5)).run();

        dter.poll();
        lift.setTarget(1800);
        while (lift.isBusy()) lift.update(dter.poll());

        hardware.claw.setPosition(0);
        sleep(500);

        dter.poll();
        lift.setTarget(0);
        while (lift.isBusy()) lift.update(dter.poll());

        poser.goTo(
                Distance.inTiles(2),
                Distance.inTiles(-2.5)
        ).run();
    }
}
