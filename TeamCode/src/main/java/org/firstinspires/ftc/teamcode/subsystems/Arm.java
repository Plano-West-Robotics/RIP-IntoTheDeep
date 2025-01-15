package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.OutClaw;
import org.firstinspires.ftc.teamcode.hardware.OutShoulder;
import org.firstinspires.ftc.teamcode.hardware.OutWrist;

public class Arm {
    private final OutWrist wrist;
    private final OutClaw claw;
    private final OutShoulder shoulder;

    private enum Target {
        WALL {
            public OutWrist.State wrist() { return OutWrist.State.WALL; }
            public double claw() { return OutClaw.OPEN; }
            public OutShoulder.State shoulder() { return OutShoulder.State.WALL; }
        },
        TRANSFER {
            public OutWrist.State wrist() { return OutWrist.State.TRANSFER; }
            public double claw() { return OutClaw.OPEN; }
            public OutShoulder.State shoulder() { return OutShoulder.State.TRANSFER; }
        },
        PRE_TRANSFER {
            public OutWrist.State wrist() { return OutWrist.State.TRANSFER; }
            public double claw() { return OutClaw.OPEN; }
            public OutShoulder.State shoulder() { return OutShoulder.State.PRE_TRANSFER; }
        },
        BASKET {
            public OutWrist.State wrist() { return OutWrist.State.BASKET; }
            public double claw() { return OutClaw.CLOSED; }
            public OutShoulder.State shoulder() { return OutShoulder.State.OUT; }
        },
        CHAMBER {
            public OutWrist.State wrist() { return OutWrist.State.CHAMBER; }
            public double claw() { return OutClaw.CLOSED; }
            public OutShoulder.State shoulder() { return OutShoulder.State.OUT; }
        };

        public abstract OutWrist.State wrist();
        public abstract double claw();
        public abstract OutShoulder.State shoulder();
    }

    private Target target;

    public Arm(Hardware hardware, Hardware.InitialConfiguration initial) {
        this(
                hardware.outWrist, hardware.outClaw, hardware.outShoulder,
                initial
        );
    }

    public Arm(
            OutWrist wrist, OutClaw claw, OutShoulder shoulder,
            Hardware.InitialConfiguration initial
    ) {
        this.wrist = wrist;
        this.claw = claw;
        this.shoulder = shoulder;

        if (initial.branch(false, false, true)) {
            throw new RuntimeException("asdjasdhaskdhaskd TODO");
        }
        this.target = initial.branch(Target.WALL, Target.TRANSFER, Target.TRANSFER);
    }

    private void startMoving() {
        this.wrist.setPosition(this.target.wrist());
        this.claw.setPosition(this.target.claw());
        this.shoulder.setPosition(this.target.shoulder());
    }

    public void toWall() {
        this.target = Target.WALL;
        this.startMoving();
    }

    public void toTransfer() {
        this.target = Target.TRANSFER;
        this.startMoving();
    }

    public void toPreTransfer() {
        this.target = Target.PRE_TRANSFER;
        this.startMoving();
    }

    public void toBasket() {
        this.target = Target.BASKET;
        this.startMoving();
    }

    public void toChamber() {
        this.target = Target.CHAMBER;
        this.startMoving();
    }

    public void grab() {
        if (this.target == Target.TRANSFER || this.target == Target.WALL) {
            this.claw.close();
        }
    }

    public void drop() {
        if (this.target == Target.BASKET || this.target == Target.CHAMBER) {
            this.claw.open();
        }
    }

    public void update(double dt) {
        this.wrist.update(dt);
        this.claw.update(dt);
        this.shoulder.update(dt);
    }

    public boolean isBusy() {
        return this.wrist.isBusy() || this.claw.isBusy() || this.shoulder.isBusy();
    }
}
