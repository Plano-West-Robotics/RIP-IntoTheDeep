package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.macro.Wait;
import org.firstinspires.ftc.teamcode.subsystems.DistanceSensors;
import org.firstinspires.ftc.teamcode.subsystems.SimpleGrabber2;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Right Auto (preload specimen + drop 2)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, true);
        hardware.claw.setPosition(1);
        hardware.bucketL.setPosition(0);

        DistanceSensors distanceSensors = new DistanceSensors(hardware);
        SimpleGrabber2 grabber = new SimpleGrabber2(hardware);
        DeltaTimer dter = new DeltaTimer();

        waitForStart();

        sleep(4000); // for tech turb (comment in case i accidentally commit this)

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
            distanceSensors.doI2cRead();
            avg = avg.add(distanceSensors.distanceFromTarget());
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

        grabber.swivelTo(1/3.);
        grabber.wristTo(SimpleGrabber2.WristState.DOWN);
        grabber.openClaw();
        sleep(1000);
        grabber.closeClaw();
        sleep(250);
        grabber.swivelTo(1);
        grabber.wristTo(SimpleGrabber2.WristState.BUCKET);
        sleep(1000);
        grabber.openClaw();
        sleep(500);
        grabber.wristTo(SimpleGrabber2.WristState.UP);

        poser.moveBy(
                Distance.inInches(-15),
                Distance.ZERO
        ).run();

        hardware.bucketL.setPosition(1);
        sleep(1500);
        hardware.bucketL.setPosition(0);

        // start grabbing second sample
        poser.goToX(Distance.inTiles(2.5)).run();

        poser.moveBy(
                Distance.inInches(15),
                Distance.ZERO
        ).run();

        grabber.wristTo(SimpleGrabber2.WristState.DOWN);
        sleep(1000);
        grabber.closeClaw();
        sleep(250);
        grabber.swivelTo(1);
        grabber.wristTo(SimpleGrabber2.WristState.BUCKET);
        sleep(1000);
        grabber.openClaw();
        sleep(500);
        grabber.wristTo(SimpleGrabber2.WristState.UP);

        poser.moveBy(
                Distance.inInches(-15),
                Distance.ZERO
        ).run();

        hardware.bucketL.setPosition(1);
        sleep(1500);
        hardware.bucketL.setPosition(0);
    }
}
