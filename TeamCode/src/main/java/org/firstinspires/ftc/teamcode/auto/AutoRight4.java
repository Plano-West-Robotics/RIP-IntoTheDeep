package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.OutClaw;
import org.firstinspires.ftc.teamcode.hardware.InClaw;
import org.firstinspires.ftc.teamcode.hardware.InSwivel;
import org.firstinspires.ftc.teamcode.hardware.InWrist;
import org.firstinspires.ftc.teamcode.hardware.OutShoulder;
import org.firstinspires.ftc.teamcode.hardware.OutWrist;
import org.firstinspires.ftc.teamcode.macro.Action;
import org.firstinspires.ftc.teamcode.macro.ConcurrentSet;
import org.firstinspires.ftc.teamcode.macro.ControlFlow;
import org.firstinspires.ftc.teamcode.macro.Sequence;
import org.firstinspires.ftc.teamcode.macro.Wait;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

@Autonomous(name = "Right Auto (4 spec)", preselectTeleOp = "DDDDDDDDD")
public class AutoRight4 extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.RIGHT, true);

        ControlledLift lift = new ControlledLift(hardware);

        waitForStart();

        ////////////////////
        // first specimen //
        ////////////////////

        ConcurrentSet.of(
                ConcurrentSet.of(
                        lift.goTo(ControlledLift.HIGH_CHAMBER),
                        hardware.outShoulder.goTo(OutShoulder.State.OUT),
                        hardware.outWrist.goTo(OutWrist.State.CHAMBER)
                ),
                poser.goTo(
                        Distance.ZERO,
                        Distance.inTiles(-1.5).sub(Distance.inInches(6))
                )
        ).run();

        clipUsingDistSensors().run();
        hardware.outClaw.goTo(OutClaw.OPEN).run();

        /////////////
        // samples //
        /////////////

        ConcurrentSet.of(
                ConcurrentSet.of(
                        lift.goTo(ControlledLift.MIN_TICKS),
                        hardware.outShoulder.goTo(OutShoulder.State.PRE_TRANSFER),
                        hardware.outWrist.goTo(OutWrist.State.TRANSFER)
                ),
                ConcurrentSet.of(
                        hardware.inWrist.goTo(InWrist.State.DOWN),
                        hardware.inSwivel.goTo(InSwivel.MIDDLE * 4/9. + InSwivel.LEFT * 5/9.),
                        hardware.extend.goTo(1.0)
                ),
                poser.goTo(
                        Distance.inTiles(1.30),
                        Distance.inTiles(-1.75),
                        Angle.inDegrees(40)
                )
        ).run();

        // first sample
        hardware.inClaw.goTo(InClaw.CLOSED).run();
        poser.goTo(
                Distance.inTiles(1.30),
                Distance.inTiles(-2),
                Angle.inDegrees(-40)
        ).run();
        hardware.inClaw.goTo(InClaw.OPEN).run();

        // to second sample
        poser.goTo(
                Distance.inTiles(1.75),
                Distance.inTiles(-1.75),
                Angle.inDegrees(44)
        ).run();

        // second sample
        hardware.inClaw.goTo(InClaw.CLOSED).run();
        poser.goTo(
                Distance.inTiles(1.75),
                Distance.inTiles(-2),
                Angle.inDegrees(-40)
        ).run();
        hardware.inClaw.goTo(InClaw.OPEN).run();

//        // to third sample
//        poser.goTo(
//                Distance.inTiles(2.15),
//                Distance.inTiles(-1.75),
//                Angle.inDegrees(44)
//        ).run();
//
//        // third sample
//        hardware.inClaw.goTo(InClaw.CLOSED).run();
//        poser.goTo(
//                Distance.inTiles(1.5),
//                Distance.inTiles(-2),
//                Angle.inDegrees(-40)
//        ).run();
//        hardware.inClaw.goTo(InClaw.OPEN).run();

        /////////////////////////
        // align with specimen //
        /////////////////////////

        ConcurrentSet.of(
                hardware.inWrist.goTo(InWrist.State.IN),
                hardware.inSwivel.goTo(InSwivel.TRANSFER),
                hardware.extend.goTo(0),
                poser.goTo(
                        Distance.inTiles(1.6),
                        Distance.inTiles(-2),
                        Angle.RIGHT
                )
        ).run();

        /////////////////////
        // second specimen //
        /////////////////////

        ConcurrentSet.of(
                hardware.outShoulder.goTo(OutShoulder.State.WALL),
                hardware.outWrist.goTo(OutWrist.State.WALL),
                poser.goToY(
                        Distance.inTiles(-2.5).sub(Distance.inInches(1.5))
                ).withStuckCheck()
        ).run();
        hardware.outClaw.goTo(OutClaw.CLOSED).run();

        ConcurrentSet.of(
                lift.goTo(ControlledLift.HIGH_CHAMBER),
                hardware.outShoulder.goTo(OutShoulder.State.OUT),
                hardware.outWrist.goTo(OutWrist.State.CHAMBER),
                Sequence.of(
                        Wait.millis(500),
                        poser.goTo(
                                Distance.inInches(3),
                                Distance.inTiles(-1.5).sub(Distance.inInches(6))
                        )
                )
        ).run();

        clipUsingDistSensors().run();
        hardware.outClaw.goTo(OutClaw.OPEN).run();

        ////////////////////
        // third specimen //
        ////////////////////

        ConcurrentSet.of(
                Sequence.of(
                        Wait.millis(300),
                        lift.goTo(ControlledLift.MIN_TICKS)
                ),
                Sequence.of(
                        Wait.millis(500),
                        ConcurrentSet.of(
                                hardware.outShoulder.goTo(OutShoulder.State.WALL),
                                hardware.outWrist.goTo(OutWrist.State.WALL)
                        )
                ),
                poser.goTo(
                        Distance.inTiles(1.6),
                        Distance.inTiles(-2.5).sub(Distance.inInches(1.5))
                ).withStuckCheck()
        ).run();
        hardware.outClaw.goTo(OutClaw.CLOSED).run();

        ConcurrentSet.of(
                lift.goTo(ControlledLift.HIGH_CHAMBER),
                hardware.outShoulder.goTo(OutShoulder.State.OUT),
                hardware.outWrist.goTo(OutWrist.State.CHAMBER),
                Sequence.of(
                        Wait.millis(500),
                        poser.goTo(
                                Distance.inInches(6),
                                Distance.inTiles(-1.5).sub(Distance.inInches(6))
                        )
                )
        ).run();

        clipUsingDistSensors().run();
        hardware.outClaw.goTo(OutClaw.OPEN).run();

        /////////////////////
        // fourth specimen //
        /////////////////////

        ConcurrentSet.of(
                Sequence.of(
                        Wait.millis(300),
                        lift.goTo(ControlledLift.MIN_TICKS)
                ),
                Sequence.of(
                        hardware.outShoulder.goTo(OutShoulder.State.WALL),
                        hardware.outWrist.goTo(OutWrist.State.WALL)
                ),
                poser.goTo(
                        Distance.inTiles(1.6),
                        Distance.inTiles(-2.5).sub(Distance.inInches(1.5))
                ).withStuckCheck()
        ).run();
        hardware.outClaw.goTo(OutClaw.CLOSED).run();

        ConcurrentSet.of(
                lift.goTo(ControlledLift.HIGH_CHAMBER),
                hardware.outShoulder.goTo(OutShoulder.State.OUT),
                hardware.outWrist.goTo(OutWrist.State.CHAMBER),
                Sequence.of(
                        Wait.millis(500),
                        poser.goTo(
                                Distance.inInches(9),
                                Distance.inTiles(-1.5).sub(Distance.inInches(6))
                        )
                )
        ).run();

        clipUsingDistSensors().run();
        hardware.outClaw.goTo(OutClaw.OPEN).run();

        ///////////
        // reset //
        ///////////

        ConcurrentSet.of(
                poser.moveBy(
                        Distance.inInches(5),
                        Distance.ZERO
                ),
                Sequence.of(
                        Wait.millis(300),
                        lift.goTo(ControlledLift.MIN_TICKS)
                ),
                hardware.outShoulder.goTo(OutShoulder.State.WALL),
                hardware.outWrist.goTo(OutWrist.State.WALL)
        ).run();
    }

    private Action clipUsingDistSensors() {
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
                        final Distance TARGET = Distance.inMM(30);
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