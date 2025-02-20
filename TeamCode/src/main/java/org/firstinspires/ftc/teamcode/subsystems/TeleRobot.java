package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

public class TeleRobot {
    private final Extendo extend;
    private final Grabber grabber;
    private final ControlledLift lift;
    private final Arm arm;

    private static final double EXTEND_THRESH = 0.1;

    private enum State {
        IDLE, // outtake @ wall, intake @ in, out claw open, in claw open

        SAMPLE_PICKUP, // arm to pre-transfer, lift to down, intake out and moving
        SAMPLE_TO_TRANSFER, // arm to transfer, lift to down, intake to transfer, out claw open
        SAMPLE_TRANSFER_HOLD, // outtake @ transfer, intake @ transfer, out claw open, in claw closed
        SAMPLE_TRANSFER_1, // outtake @ transfer, intake @ transfer, out claw closing, in claw closed
        SAMPLE_TRANSFER_2, // outtake @ transfer, intake @ transfer, out claw closed, in claw opening
        SAMPLE_TO_DROP, // arm to basket, lift anywhere, intake to in, out claw closed
        SAMPLE_DROP, // arm @ basket, lift anywhere, intake @ in, out claw closed
        SAMPLE_RETRACT_1, // arm @ basket, lift anywhere, intake @ in, out claw opening
        SAMPLE_RETRACT_2, // arm to wall, lift to down, intake @ in, out claw open
        SAMPLE_RETRACT_CANCEL, // arm to pre-transfer, lift to down, intake to in,

        SPECIMEN_GRAB, // arm @ wall, lift @ down, claw closing
        SPECIMEN_TO_HI_CHAMBER, // arm to chamber, lift to high chamber, claw closed
        SPECIMEN_HI_CHAMBER, // arm @ chamber, lift @ high chamber, claw closed
        SPECIMEN_DROP, // arm @ chamber, lift @ high chamber, claw opening
        SPECIMEN_TO_WALL, // arm to wall, lift to down, claw open
    }
    private State state;
    private double extendPos;
    private boolean grabberDown;

    private boolean extendTrigger;
    private boolean sampleDropButton;
    private double slidePow;
    private boolean specimenCycleButton;
    private boolean halfSequenceMode;

    public TeleRobot(Hardware hardware) {
        this(
                hardware.extend,
                new Grabber(hardware, Hardware.InitialConfiguration.TELEOP),
                new ControlledLift(hardware),
                new Arm(hardware, Hardware.InitialConfiguration.TELEOP)
        );
    }

    public TeleRobot(Extendo extend, Grabber grabber, ControlledLift lift, Arm arm) {
        this.extend = extend;
        this.grabber = grabber;
        this.lift = lift;
        this.arm = arm;

        this.state = State.IDLE;

        this.extendTrigger = false;
        this.sampleDropButton = false;
        this.slidePow = 0.0;
        this.specimenCycleButton = false;
    }

    public void setExtendTrigger(boolean extendTrigger) {
        this.extendTrigger = extendTrigger;
    }

    public void setSlidePow(double slidePow) {
        this.slidePow = slidePow;
    }

    public void pressSampleDropButton() {
        this.sampleDropButton = true;
    }

    public void pressSpecimenCycleButton() {
        this.specimenCycleButton = true;
    }

    public void pressHalfSequenceModeButton() {
        this.halfSequenceMode = true;
    }

    public void pressFullSequenceModeButton() {
        this.halfSequenceMode = false;
    }

    public void swivelBumpLeft() {
        if (state == State.SAMPLE_PICKUP) {
            this.grabber.swivelBumpLeft();
        }
    }

    public void swivelBumpRight() {
        if (state == State.SAMPLE_PICKUP) {
            this.grabber.swivelBumpRight();
        }
    }

    public void grabberToggleDown() {
        if (state == State.SAMPLE_PICKUP) {
            grabberDown = !grabberDown;
            if (grabberDown) {
                this.grabber.toDown();
            } else {
                this.grabber.toUp();
            }
        }
    }

    public boolean driver2ShouldHaveDrivetrainControl() {
        return state == State.SAMPLE_PICKUP;
    }

    private void helperToSamplePickup() {
        this.extend.setPosition(extendPos);
        if (grabberDown) this.grabber.toDown();
        else this.grabber.toUp();
        this.arm.toPreTransfer();
    }

    public void update(double dt) {
        switch (state) {
            case IDLE:
                if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    extendPos = Extendo.OUT_GRABBER_UP;
                    grabberDown = false;
                    this.helperToSamplePickup();
                } else if (specimenCycleButton) {
                    state = State.SPECIMEN_GRAB;
                    this.arm.grab();
                }
                break;
            case SAMPLE_PICKUP:
                if (!extendTrigger) {
                    state = State.SAMPLE_TO_TRANSFER;
                    this.extend.setPosition(0.0);
                    this.grabber.toTransfer();
                    this.arm.toTransfer();
                } else {
                    extendPos += 1.0 * dt * slidePow;
                    extendPos = Range.clip(
                            extendPos,
                            EXTEND_THRESH,
                            this.grabber.isDefinitelyDown() ? Extendo.OUT_GRABBER_DOWN : Extendo.OUT_GRABBER_UP
                    );
                    this.extend.setPosition(extendPos);
                }
                break;
            case SAMPLE_TO_TRANSFER:
                if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_CANCEL;
                    this.grabber.toIn();
                    this.arm.toPreTransfer();
                } else if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
                } else if (halfSequenceMode) {
                    state = State.SAMPLE_TRANSFER_HOLD;
                } else if (!this.extend.isBusy() && !this.grabber.isBusy() && !this.lift.isBusy() && !this.arm.isBusy()) {
                    state = State.SAMPLE_TRANSFER_1;
                    this.arm.grab();
                }
                break;
            case SAMPLE_TRANSFER_HOLD:
                if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_CANCEL;
                    this.grabber.toIn();
                    this.arm.toPreTransfer();
                } else if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
                } else if (!halfSequenceMode) {
                    state = State.SAMPLE_TRANSFER_1; // making the assumption the hold is long enough? ;.;
                    this.arm.grab();
                }
                break;
            case SAMPLE_TRANSFER_1:
                if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_CANCEL;
                    this.grabber.toIn();
                    this.arm.toPreTransfer();
                } else if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
                } else if (!this.arm.isBusy()) {
                    state = State.SAMPLE_TRANSFER_2;
                    this.grabber.openClawAtTransfer();
                }
                break;
            case SAMPLE_TRANSFER_2:
                if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_CANCEL;
                    this.grabber.toIn();
                    this.arm.toPreTransfer();
                } else if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
                } else if (!this.grabber.isBusy()) {
                    state = State.SAMPLE_TO_DROP;
                    this.grabber.toIn();
                    this.arm.toBasket();
                }
                break;
            case SAMPLE_TO_DROP:
                if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_CANCEL;
                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                    this.arm.toPreTransfer();
                } else if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                } else if (!this.grabber.isBusy() && !this.arm.isBusy()) {
                    state = State.SAMPLE_DROP;
                } else {
                    this.lift.setPower(slidePow);
                }
                break;
            case SAMPLE_DROP:
                if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    this.helperToSamplePickup();
//                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                } else if (sampleDropButton) {
                    state = State.SAMPLE_RETRACT_1;
                    this.arm.drop();
                } else {
                    this.lift.setPower(slidePow);
                }
                break;
            case SAMPLE_RETRACT_1:
                if (!this.arm.isBusy()) {
                    state = State.SAMPLE_RETRACT_2;
                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                    this.arm.toWall();
                }
                break;
            case SAMPLE_RETRACT_2:
                if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    extendPos = Extendo.OUT_GRABBER_UP;
                    grabberDown = false;
                    this.helperToSamplePickup();
                } else if (!this.lift.isBusy() && !this.arm.isBusy()) {
                    this.state = State.IDLE;
                }
                break;
            case SAMPLE_RETRACT_CANCEL:
                if (extendTrigger) {
                    state = State.SAMPLE_PICKUP;
                    extendPos = Extendo.OUT_GRABBER_UP;
                    grabberDown = false;
                    this.helperToSamplePickup();
                } if (!this.extend.isBusy() && !this.grabber.isBusy()) {
                    this.state = State.SAMPLE_RETRACT_2;
                    this.arm.toWall();
                }
                break;
            case SPECIMEN_GRAB:
                if (specimenCycleButton) {
                    state = State.SPECIMEN_TO_WALL;
                    this.arm.toWall(); // opens the claw again
                } else if (!this.arm.isBusy()) {
                    state = State.SPECIMEN_TO_HI_CHAMBER;
                    this.lift.setTarget(ControlledLift.HIGH_CHAMBER);
                    this.arm.toChamber();
                }
                break;
            case SPECIMEN_TO_HI_CHAMBER:
                if (specimenCycleButton) {
                    state = State.SPECIMEN_TO_WALL;
                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                    this.arm.toWall();
                } else if (!this.lift.isBusy() && !this.arm.isBusy()) {
                    state = State.SPECIMEN_HI_CHAMBER;
                }
                break;
            case SPECIMEN_HI_CHAMBER:
                if (specimenCycleButton) {
                    state = State.SPECIMEN_DROP;
                    this.arm.drop();
                }
                break;
            case SPECIMEN_DROP:
                if (!this.arm.isBusy()) {
                    state = State.SPECIMEN_TO_WALL;
                    this.lift.setTarget(ControlledLift.MIN_TICKS);
                    this.arm.toWall();
                }
                break;
            case SPECIMEN_TO_WALL:
                if (!this.lift.isBusy() && !this.arm.isBusy()) {
                    state = State.IDLE;
                }
                break;
        }

        this.extend.update(dt);
        this.grabber.update(dt);
        this.lift.update(dt);
        this.arm.update(dt);

        this.sampleDropButton = false;
        this.specimenCycleButton = false;
    }
}
