package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Bucket;
import org.firstinspires.ftc.teamcode.hardware.Claw;
import org.firstinspires.ftc.teamcode.hardware.IntakeClaw;
import org.firstinspires.ftc.teamcode.hardware.Swivel;
import org.firstinspires.ftc.teamcode.hardware.Wrist;
import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ConcurrentSet;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.macro.Sequence;
import org.firstinspires.ftc.teamcode.macro.Wait;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Right Auto (optimized 1+1)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight3 extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, true);
        hardware.bucket.down();

        ControlledLift lift = new ControlledLift(hardware);
        DeltaTimer dter = new DeltaTimer();

        Action liftUpdaterAction = new Action() {
            public ControlFlow update() {
                lift.update(dter.poll());
                return ControlFlow.continueIf(lift.isBusy());
            }

            public void end() {}
        };

        waitForStart();

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
                        alignUsingDistSensors()
                )
        ).run();

        // lift down
        dter.poll();
        lift.setTarget(1550);
        Wait wait = Wait.seconds(1.5);
        while (lift.isBusy() && wait.update().shouldContinue()) lift.update(dter.poll());
        wait.end();

        // let go of preload
        hardware.claw.goTo(Claw.OPEN).run();

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
                hardware.swivel.goTo(Swivel.MIDDLE),
                hardware.wrist.goTo(Wrist.State.DOWN),
                hardware.claw.goTo(Claw.OPEN)
        ).run();

        // grab the sample
        hardware.intake.goTo(IntakeClaw.CLOSED).run();

        ConcurrentSet.of(
                // drop the sample into the bucket
                Sequence.of(
                        ConcurrentSet.of(
                                hardware.swivel.goTo(Swivel.BUCKET),
                                hardware.wrist.goTo(Wrist.State.BUCKET)
                        ),
                        hardware.intake.goTo(IntakeClaw.OPEN),
                        hardware.wrist.goTo(Wrist.State.UP)
                ),
                // and meanwhile move to the observation zone
                poser.moveBy(
                        Distance.inInches(-15),
                        Distance.ZERO
                )
        ).run();

        // drop the sample into the observation zone
        hardware.bucket.goTo(Bucket.UP).run();
        hardware.bucket.setPosition(Bucket.DOWN);

        sleep(5000);

        hardware.wrist.setPosition(Wrist.State.BUCKET);

        dter.poll();
        lift.setTarget(200);

        ConcurrentSet.of(
                liftUpdaterAction,
                poser.goToY(
                        Distance.inTiles(-2.5).sub(Distance.inInches(1.5))
                ).withStuckCheck()
        ).run();

        hardware.claw.goTo(Claw.CLOSED).run();

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
                        alignUsingDistSensors()
                )
        ).run();

        // lift down
        dter.poll();
        lift.setTarget(1550);
        wait = Wait.seconds(1.5);
        while (lift.isBusy() && wait.update().shouldContinue()) lift.update(dter.poll());
        wait.end();

        // let go of specimen
        hardware.claw.goTo(Claw.OPEN).run();

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

    private Action alignUsingDistSensors() {
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
                        hardware.dist.doI2cRead();
                        avg = avg.add(hardware.dist.distanceFromTarget());
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