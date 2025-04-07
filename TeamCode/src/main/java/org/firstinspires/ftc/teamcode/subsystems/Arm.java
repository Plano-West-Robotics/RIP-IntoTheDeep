package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.OutClaw;
import org.firstinspires.ftc.teamcode.hardware.OutShoulder;
import org.firstinspires.ftc.teamcode.hardware.OutSwivel;
import org.firstinspires.ftc.teamcode.hardware.OutWrist;
import org.firstinspires.ftc.teamcode.units.Angle;

public class Arm {
    private final OutWrist wrist;
    private final OutClaw claw;
    private final OutSwivel swivel;
    private final OutShoulder shoulder;
    public final PivotControl pivot;

    private enum Target {
        PRE_TRANSFER {
            public OutWrist.State wrist() { return OutWrist.State.PRE_TRANSFER; }
            public double claw() { return OutClaw.OPEN; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.PRE_TRANSFER; }
            public int pivot() { return PivotControl.MIN_TICKS; }
        },
        TRANSFER {
            public OutWrist.State wrist() { return OutWrist.State.TRANSFER; }
            public double claw() { return OutClaw.OPEN; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.TRANSFER; }
            public int pivot() { return PivotControl.MIN_TICKS; }
        },
        DROP_FRONT {
            public OutWrist.State wrist() { return OutWrist.State.DROP_FRONT; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.DROP_FRONT; }
            public int pivot() { return PivotControl.MAX_TICKS; }
        },
        DROP_BACK {
            public OutWrist.State wrist() { return OutWrist.State.DROP_BACK; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.BACK; }
            public OutShoulder.State shoulder() { return OutShoulder.State.DROP_BACK; }
            public int pivot() { return PivotControl.MIN_TICKS; }
        },
        BASKET_FRONT {
            public OutWrist.State wrist() { return OutWrist.State.BASKET_FRONT; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.BASKET_FRONT; }
            public int pivot() { return 1150; }
        },
        BASKET_BACK {
            public OutWrist.State wrist() { return OutWrist.State.BASKET_BACK; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.BACK; }
            public OutShoulder.State shoulder() { return OutShoulder.State.BASKET_BACK; }
            public int pivot() { return 630; }
        },
        WALL_FRONT {
            public OutWrist.State wrist() { return OutWrist.State.WALL_FRONT; }
            public double claw() { return OutClaw.OPEN; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.WALL_FRONT; }
            public int pivot() { return 950; }
        },
        WALL_BACK {
            public OutWrist.State wrist() { return OutWrist.State.WALL_BACK; }
            public double claw() { return OutClaw.OPEN; }
            public double swivel() { return OutSwivel.BACK; }
            public OutShoulder.State shoulder() { return OutShoulder.State.WALL_BACK; }
            public int pivot() { return 500; }
        },
        CHAMBER_FRONT {
            public OutWrist.State wrist() { return OutWrist.State.CHAMBER_FRONT; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.FRONT; }
            public OutShoulder.State shoulder() { return OutShoulder.State.CHAMBER_FRONT; }
            public int pivot() { return 1200; }
        },
        CHAMBER_BACK {
            public OutWrist.State wrist() { return OutWrist.State.CHAMBER_BACK; }
            public double claw() { return OutClaw.CLOSED; }
            public double swivel() { return OutSwivel.BACK; }
            public OutShoulder.State shoulder() { return OutShoulder.State.CHAMBER_BACK; }
            public int pivot() { return PivotControl.MIN_TICKS; }
        };

        public abstract OutWrist.State wrist();
        public abstract double claw();
        public abstract double swivel();
        public abstract OutShoulder.State shoulder();
        public abstract int pivot();
    }

    private Target target;

    public Arm(Hardware hardware, Hardware.InitialConfiguration initial) {
        this(
                hardware.outWrist,
                hardware.outClaw,
                hardware.outSwivel,
                hardware.outShoulder,
                new PivotControl(hardware.pivot),
                initial
        );
    }

    public Arm(
            OutWrist wrist, OutClaw claw, OutSwivel swivel, OutShoulder shoulder, PivotControl pivot,
            Hardware.InitialConfiguration initial
    ) {
        this.wrist = wrist;
        this.claw = claw;
        this.swivel = swivel;
        this.shoulder = shoulder;
        this.pivot = pivot;

        if (initial.branch(false, false, true)) {
            throw new RuntimeException("asdjasdhaskdhaskd TODO");
        }
        this.target = initial.branch(Target.WALL_FRONT, Target.TRANSFER, Target.TRANSFER);
    }

    private void startMoving() {
        this.wrist.setPosition(this.target.wrist());
        this.claw.setPosition(this.target.claw());
        this.swivel.setPosition(this.target.swivel());
        this.shoulder.setPosition(this.target.shoulder());
        this.pivot.setTarget(this.target.pivot());
    }

    public void toPreTransfer() {
        this.target = Target.PRE_TRANSFER;
        this.startMoving();
    }

    public void toTransfer() {
        this.target = Target.TRANSFER;
        this.startMoving();
    }

    public void toFrontBasket() {
        this.target = Target.BASKET_FRONT;
        this.startMoving();
    }

    public void toBackBasket() {
        this.target = Target.BASKET_BACK;
        this.startMoving();
    }

    public void toFrontWall() {
        this.target = Target.WALL_FRONT;
        this.startMoving();
    }

    public void toBackWall() {
        this.target = Target.WALL_BACK;
        this.startMoving();
    }

    public void toFrontChamber() {
        this.target = Target.CHAMBER_FRONT;
        this.startMoving();
    }

    public void toBackChamber() {
        this.target = Target.CHAMBER_BACK;
        this.startMoving();
    }

    public void toFrontDrop() {
        this.target = Target.DROP_FRONT;
        this.startMoving();
    }

    public void toBackDrop() {
        this.target = Target.DROP_BACK;
        this.startMoving();
    }

    public void grab() {
        switch (this.target) {
            case TRANSFER:
            case WALL_FRONT:
            case WALL_BACK:
                this.claw.close();
                break;
        }
    }

    public void drop() {
        switch (this.target) {
            case BASKET_FRONT:
            case BASKET_BACK:
            case CHAMBER_FRONT:
            case CHAMBER_BACK:
                this.claw.open();
                break;
        }
    }

    public void update(double dt) {
        this.wrist.update(dt);
        this.claw.update(dt);
        this.swivel.update(dt);
        this.shoulder.update(dt);
        this.pivot.update(dt);
    }

    public boolean isBusy() {
        return this.wrist.isBusy()
                || this.claw.isBusy()
                || this.swivel.isBusy()
                || this.shoulder.isBusy()
                || this.pivot.isBusy();
    }
}
