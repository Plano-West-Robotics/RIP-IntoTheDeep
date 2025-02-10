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
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@Autonomous(name = "Left Auto (4 sample + park)", preselectTeleOp = "DDDDDDDDD")
public class AutoLeft2 extends AutoBase {
    @Override
    public void runOpMode() throws InterruptedException {
        super.setup(LeftOrRight.LEFT, true);

        ControlledLift lift = new ControlledLift(hardware);
        DeltaTimer dter = new DeltaTimer();

        Action liftUpdaterAction = new Action() {
            public ControlFlow update() {
                lift.update(dter.poll());
                return ControlFlow.continueIf(lift.isBusy());
            }

            public void end() {
            }
        };

        waitForStart();

        ////////////////////
        //  first sample  //
        ////////////////////

        dter.poll();
        lift.setTarget(2370);
        ConcurrentSet.of(
                Sequence.of(
                        liftUpdaterAction,
                        ConcurrentSet.of(
                                hardware.outShoulder.goTo(OutShoulder.State.OUT),
                                hardware.outWrist.goTo(OutWrist.State.BASKET)
                        )
                ),
                ConcurrentSet.of(
                        hardware.inWrist.goTo(InWrist.State.UP),
                        hardware.inSwivel.goTo(InSwivel.MIDDLE),
                        hardware.inClaw.goTo(InClaw.OPEN),
                        hardware.extend.goTo(1.0)
                ),
                poser.goTo(
                        Distance.inTiles(-2.5).add(Distance.inInches(3)),
                        Distance.inTiles(-2.5).add(Distance.inInches(11)),
                        Angle.inDegrees(66)
                )
        ).run();

        hardware.outClaw.goTo(OutClaw.OPEN).run();

        ////////////////////
        // second sample  //
        ////////////////////

        dter.poll();
        lift.setTarget(0);
        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                ConcurrentSet.of(
                                        hardware.inWrist.goTo(InWrist.State.DOWN),
                                        hardware.inSwivel.goTo(InSwivel.MIDDLE + (24/90.) * (InSwivel.LEFT - InSwivel.MIDDLE))
                                ),
                                poser.goTo(
                                        Distance.inTiles(-2.5).add(Distance.inInches(1)),
                                        Distance.inTiles(-2.5).add(Distance.inInches(13.5))
                                )
                        ),
                        hardware.inClaw.goTo(InClaw.CLOSED)
                ),
                ConcurrentSet.of(
                        liftUpdaterAction,
                        hardware.outShoulder.goTo(OutShoulder.State.TRANSFER),
                        hardware.outWrist.goTo(OutWrist.State.TRANSFER)
                )
        ).run();

        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                hardware.inWrist.goTo(InWrist.State.TRANSFER),
                                hardware.inSwivel.goTo(InSwivel.TRANSFER),
                                hardware.extend.goTo(0.0)
                        ),
                        hardware.outClaw.goTo(OutClaw.CLOSED),
                        hardware.inClaw.goTo(InClaw.OPEN),
                        ConcurrentSet.of(
                                Sequence.of(
                                        Action.fromFn(() -> {
                                            dter.poll();
                                            lift.setTarget(2370);
                                        }),
                                        liftUpdaterAction,
                                        hardware.outWrist.goTo(OutWrist.State.BASKET)
                                ),
                                hardware.outShoulder.goTo(OutShoulder.State.OUT)
                        )
                ),
                poser.goTo(
                        Distance.inTiles(-2.5).add(Distance.inInches(3)),
                        Distance.inTiles(-2.5).add(Distance.inInches(11)),
                        Angle.inDegrees(66)
                )
        ).run();

        hardware.outClaw.goTo(OutClaw.OPEN).run();

        ////////////////////
        //  third sample  //
        ////////////////////

        dter.poll();
        lift.setTarget(0);
        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                ConcurrentSet.of(
                                        hardware.inWrist.goTo(InWrist.State.DOWN),
                                        hardware.inSwivel.goTo(InSwivel.MIDDLE),
                                        hardware.extend.goTo(1.0)
                                ),
                                poser.goTo(
                                        Distance.inTiles(-2.5),
                                        Distance.inTiles(-2.5).add(Distance.inInches(11)),
                                        Angle.LEFT
                                )
                        ),
                        hardware.inClaw.goTo(InClaw.CLOSED)
                ),
                ConcurrentSet.of(
                        liftUpdaterAction,
                        hardware.outShoulder.goTo(OutShoulder.State.TRANSFER),
                        hardware.outWrist.goTo(OutWrist.State.TRANSFER)
                )
        ).run();

        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                hardware.inWrist.goTo(InWrist.State.TRANSFER),
                                hardware.inSwivel.goTo(InSwivel.TRANSFER),
                                hardware.extend.goTo(0.0)
                        ),
                        hardware.outClaw.goTo(OutClaw.CLOSED),
                        hardware.inClaw.goTo(InClaw.OPEN),
                        ConcurrentSet.of(
                                Sequence.of(
                                        Action.fromFn(() -> {
                                            dter.poll();
                                            lift.setTarget(2370);
                                        }),
                                        liftUpdaterAction,
                                        hardware.outWrist.goTo(OutWrist.State.BASKET)
                                ),
                                hardware.outShoulder.goTo(OutShoulder.State.OUT)
                        )
                ),
                poser.goTo(
                        Distance.inTiles(-2.5).add(Distance.inInches(3)),
                        Distance.inTiles(-2.5).add(Distance.inInches(11)),
                        Angle.inDegrees(66)
                )
        ).run();

        hardware.outClaw.goTo(OutClaw.OPEN).run();

        ////////////////////
        // fourth sample  //
        ////////////////////

        dter.poll();
        lift.setTarget(0);
        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                ConcurrentSet.of(
                                        hardware.inWrist.goTo(InWrist.State.DOWN),
                                        hardware.inSwivel.goTo(InSwivel.MIDDLE + (70/90.) * (InSwivel.RIGHT - InSwivel.MIDDLE)),
                                        hardware.extend.goTo(1.0)
                                ),
                                poser.goTo(
                                        Distance.inTiles(-2.5).add(Distance.inInches(12.25)),
                                        Distance.inTiles(-2.5).add(Distance.inInches(24)),
                                        Angle.inDegrees(160)
                                )
                        ),
                        hardware.inClaw.goTo(InClaw.CLOSED)
                ),
                ConcurrentSet.of(
                        liftUpdaterAction,
                        hardware.outShoulder.goTo(OutShoulder.State.TRANSFER),
                        hardware.outWrist.goTo(OutWrist.State.TRANSFER)
                )
        ).run();

        ConcurrentSet.of(
                Sequence.of(
                        ConcurrentSet.of(
                                hardware.inWrist.goTo(InWrist.State.TRANSFER),
                                hardware.inSwivel.goTo(InSwivel.TRANSFER),
                                hardware.extend.goTo(0.0)
                        ),
                        hardware.outClaw.goTo(OutClaw.CLOSED),
                        hardware.inClaw.goTo(InClaw.OPEN),
                        ConcurrentSet.of(
                                Sequence.of(
                                        Action.fromFn(() -> {
                                            dter.poll();
                                            lift.setTarget(2370);
                                        }),
                                        liftUpdaterAction,
                                        hardware.outWrist.goTo(OutWrist.State.BASKET)
                                ),
                                hardware.outShoulder.goTo(OutShoulder.State.OUT)
                        )
                ),
                poser.goTo(
                        Distance.inTiles(-2.5).add(Distance.inInches(3)),
                        Distance.inTiles(-2.5).add(Distance.inInches(11)),
                        Angle.inDegrees(66)
                )
        ).run();

        hardware.outClaw.goTo(OutClaw.OPEN).run();

        //////////
        // park //
        //////////

        dter.poll();
        lift.setTarget(0);
        Sequence.of(
                ConcurrentSet.of(
                        ConcurrentSet.of(
                                liftUpdaterAction,
                                hardware.outShoulder.goTo(OutShoulder.State.WALL),
                                hardware.outWrist.goTo(OutWrist.State.BASKET),
                                hardware.inSwivel.goTo(InSwivel.TRANSFER),
                                hardware.inWrist.goTo(InWrist.State.IN)
                        ),
                        poser.goTo(
                                Distance.inTiles(-1).sub(Distance.inInches(6)),
                                Distance.inTiles(-0.5),
                                Angle.BACKWARD
                        )
                ),
                poser.goTo(
                        Distance.inTiles(-1),
                        Distance.inTiles(-0.5)
                ),
                hardware.outShoulder.goTo(0.8)
        ).run();

    }
}