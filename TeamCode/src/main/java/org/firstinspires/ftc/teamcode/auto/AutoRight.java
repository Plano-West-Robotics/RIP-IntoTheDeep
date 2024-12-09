package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Swivel;
import org.firstinspires.ftc.teamcode.hardware.Wrist;
import org.firstinspires.ftc.teamcode.macro.Wait;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Right Auto (preload specimen + drop 2)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, true);
        hardware.bucket.down();

        ControlledLift lift = new ControlledLift(hardware);
        DeltaTimer dter = new DeltaTimer();

        waitForStart();

        dter.poll();
        lift.setTarget(2200);
        while (lift.isBusy()) lift.update(dter.poll());

        poser.goTo(
                Distance.ZERO,
                Distance.inTiles(-1.5).add(Distance.inInches(2))
        ).run();
        Wait wait = Wait.seconds(0.5);
        Distance avg = Distance.ZERO;
        int count = 0;
        while (wait.update().shouldContinue()) {
            hardware.dist.doI2cRead();
            avg = avg.add(hardware.dist.distanceFromTarget());
            count++;
        }
        avg = avg.div(count);
        final Distance TARGET = Distance.inMM(75);
        Distance error = TARGET.sub(avg);
        telemetry.addData("error", error);
        telemetry.addData("count", count);
        telemetry.addData("avg", avg);
        telemetry.update();
        poser.moveBy(error, Distance.ZERO).run();

        dter.poll();
        lift.setTarget(1550);
        wait = Wait.seconds(2);
        while (lift.isBusy() && wait.update().shouldContinue()) lift.update(dter.poll());
        wait.end();

        hardware.claw.setPosition(0);
        sleep(500);

        poser.moveBy(
                Distance.inInches(5),
                Distance.ZERO
        ).run();

        dter.poll();
        lift.setTarget(0);
        while (lift.isBusy()) lift.update(dter.poll());

        poser.goTo(
                Distance.inTiles(2).add(Distance.inInches(1.5)),
                Distance.inTiles(-1.5).sub(Distance.inInches(2)),
                Angle.LEFT
        ).turningCw().run();
        poser.moveBy(
                Distance.inInches(6.4),
                Distance.ZERO
        ).run();

        hardware.swivel.setPosition(Swivel.MIDDLE);
        hardware.wrist.setPosition(Wrist.State.DOWN);
        hardware.intake.open();
        sleep(1000);
        hardware.intake.close();
        sleep(250);
        hardware.swivel.setPosition(Swivel.BUCKET);
        hardware.wrist.setPosition(Wrist.State.BUCKET);
        sleep(1000);
        hardware.intake.open();
        sleep(500);
        hardware.wrist.setPosition(Wrist.State.UP);

        poser.moveBy(
                Distance.inInches(-15),
                Distance.ZERO
        ).run();

        hardware.bucket.up();
        sleep(1500);
        hardware.bucket.down();

        // start grabbing second sample
        poser.goToX(Distance.inTiles(2.5)).run();

        poser.moveBy(
                Distance.inInches(15),
                Distance.ZERO
        ).run();

        hardware.wrist.setPosition(Wrist.State.DOWN);
        sleep(1000);
        hardware.intake.close();
        sleep(250);
        hardware.swivel.setPosition(Swivel.BUCKET);
        hardware.wrist.setPosition(Wrist.State.BUCKET);
        sleep(1000);
        hardware.intake.open();
        sleep(500);
        hardware.wrist.setPosition(Wrist.State.UP);

        poser.moveBy(
                Distance.inInches(-15),
                Distance.ZERO
        ).run();

        hardware.bucket.up();
        sleep(1500);
        hardware.bucket.down();
    }
}
