package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ConcurrentSet;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.macro.Sequence;
import org.firstinspires.ftc.teamcode.macro.Wait;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.subsystems.DistanceSensors;
import org.firstinspires.ftc.teamcode.subsystems.SimpleGrabber2;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Right Auto (optimized 1+1)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight3 extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, true);
        hardware.claw.setPosition(1);
        hardware.bucketL.setPosition(0);

        DistanceSensors distanceSensors = new DistanceSensors(hardware);
        SimpleGrabber2 grabber = new SimpleGrabber2(hardware);
        DeltaTimer dter = new DeltaTimer();

        Action liftUpdaterAction = new Action() {
            public ControlFlow update() {
                lift.update(dter.poll());
                return ControlFlow.continueIf(lift.isBusy());
            }

            public void end() {}
        };

        waitForStart();

        sleep(3000);

        // lift up
        dter.poll();
        lift.setTarget(2200);

        ConcurrentSet.of(
                liftUpdaterAction,
                // move to chambers
                Sequence.of(
                        poser.goTo(
                                Distance.ZERO,
                                Distance.inTiles(-1.5).add(Distance.inInches(2.5))
                        ),
                        alignUsingDistSensors(distanceSensors)
                )
        ).run();

        // lift down
        dter.poll();
        lift.setTarget(1550);
        Wait wait = Wait.seconds(1.5);
        while (lift.isBusy() && wait.update().shouldContinue()) lift.update(dter.poll());
        wait.end();

        // let go of preload
        hardware.claw.setPosition(0);
        sleep(500);

        // step back
        poser.moveBy(
                Distance.inInches(5),
                Distance.ZERO
        ).run();

        // lower lift
        dter.poll();
        lift.setTarget(0);
//        while (lift.isBusy()) lift.update(dter.poll());

        ConcurrentSet.of(
                // (lowering the lift while we do this)
                liftUpdaterAction,

                // move over to the first spike mark
                Sequence.of(
                        poser.goTo(
                                Distance.inTiles(2).add(Distance.inInches(1.5)),
                                Distance.inTiles(-1.5).sub(Distance.inInches(2)),
                                Angle.LEFT
                        ).turningCw(),
                        poser.moveBy(
                                Distance.inInches(6.4),
                                Distance.ZERO
                        )
                ),
                // and meanwhile prepare the grabber
                Sequence.of(
                        Action.fromFn(() -> {
                            grabber.swivelTo(1 / 3.);
                            grabber.wristTo(SimpleGrabber2.WristState.DOWN);
                            grabber.openClaw();
                        }),
                        Wait.seconds(1)
                )
        ).run();

        // grab the sample
        grabber.closeClaw();
        sleep(250);

        ConcurrentSet.of(
                // drop the sample into the bucket
                Sequence.of(
                        Action.fromFn(() -> {
                            grabber.swivelTo(1);
                            grabber.wristTo(SimpleGrabber2.WristState.BUCKET);
                        }),
                        Wait.seconds(1),
                        Action.fromFn(grabber::openClaw),
                        Wait.seconds(0.25),
                        Action.fromFn(() -> {
                            grabber.wristTo(SimpleGrabber2.WristState.UP);
                        })
                ),
                // and meanwhile move to the observation zone
                poser.moveBy(
                        Distance.inInches(-15),
                        Distance.ZERO
                )
        ).run();

        // drop the sample into the observation zone
        hardware.bucketL.setPosition(1);
        sleep(1000);
        hardware.bucketL.setPosition(0);

        sleep(5000);

        grabber.wristTo(SimpleGrabber2.WristState.BUCKET);

        dter.poll();
        lift.setTarget(200);

        ConcurrentSet.of(
                liftUpdaterAction,
                poser.goToY(
                        Distance.inTiles(-2.5).sub(Distance.inInches(1.5))
                ).withStuckCheck()
        ).run();

        hardware.claw.setPosition(1);
        sleep(500);

        dter.poll();
        lift.setTarget(2200);
        while (lift.getCurrentPos() < 500) lift.update(dter.poll());

        ConcurrentSet.of(
                liftUpdaterAction,
                Sequence.of(
                        poser.goTo(
                                Distance.inInches(8),
                                Distance.inTiles(-1.5).add(Distance.inInches(2.5)),
                                Angle.RIGHT
                        ).turningCcw(),
                        alignUsingDistSensors(distanceSensors)
                )
        ).run();

        // lift down
        dter.poll();
        lift.setTarget(1550);
        wait = Wait.seconds(1.5);
        while (lift.isBusy() && wait.update().shouldContinue()) lift.update(dter.poll());
        wait.end();

        // let go of specimen
        hardware.claw.setPosition(0);
        sleep(500);

        // step back
        poser.moveBy(
                Distance.inInches(5),
                Distance.ZERO
        ).run();

        // lower lift
        dter.poll();
        lift.setTarget(0);
//        while (lift.isBusy()) lift.update(dter.poll());

        ConcurrentSet.of(
                liftUpdaterAction,
                poser.goTo(
                        Distance.inTiles(1.8),
                        Distance.inTiles(-2.5),
                        Angle.LEFT
                ).turningCw()
        ).run();
    }

    private Action alignUsingDistSensors(DistanceSensors distanceSensors) {
        return new Action() {
            boolean doneWaiting = false;
            final Wait waiter = Wait.seconds(0.5);
            Poser.Motion mover;
            Distance avg = Distance.ZERO;
            int count = 0;

            @Override
            public ControlFlow update() {
                if (!doneWaiting) {
                    if (waiter.update().shouldContinue()) {
                        distanceSensors.doI2cRead();
                        avg = avg.add(distanceSensors.distanceFromTarget());
                        count++;
                        return ControlFlow.CONTINUE;
                    } else {
                        waiter.end();
                        Distance measured = avg.div(count == 0 ? 1 : count);
                        final Distance TARGET = Distance.inMM(75);
                        mover = poser.moveBy(TARGET.sub(measured), Distance.ZERO);
                        doneWaiting = true;
                    }
                }
                return mover.update();
            }

            @Override
            public void end() {
                if (doneWaiting) mover.end();
                else waiter.end();
                doneWaiting = false;
                avg = Distance.ZERO;
                count = 0;
            }
        };
    }
}