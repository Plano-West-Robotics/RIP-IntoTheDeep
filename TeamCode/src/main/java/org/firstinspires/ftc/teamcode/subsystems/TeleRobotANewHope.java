package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;

public class TeleRobotANewHope {
    private final Extendo extend;
    private final Grabber grabber;
    private final ControlledLift lift;
    private final Arm arm;

    Telemetry telemetry;

    private static final double EXTEND_THRESH = 0.1;

    private enum InState {
        IDLE,

        PICKUP,
        TO_TRANSFER,
        TRANSFER_HOLD,
        TRANSFER_1,
        TRANSFER_2,
        TRANSFER_3,
        TO_BASKET_FRONT,
        BASKET_FRONT,
        TO_BASKET_BACK,
        BASKET_BACK,
        TO_DROP_FRONT,
        DROP_FRONT,
        TO_DROP_BACK,
        DROP_BACK,
        BASKET_DROP,
    }
    private InState inState;
    private double extendPos;
    private boolean grabberDown;

    private enum OutState {
        INITIAL,

        TO_WALL_FRONT,
        WALL_FRONT,
        TO_WALL_BACK,
        WALL_BACK,
        WALL_GRABBING,

        TO_CHAMBER_FRONT,
        CHAMBER_FRONT,
        TO_CHAMBER_BACK,
        CHAMBER_BACK,
        CHAMBER_DROPPING,

        TO_TRANSFER,
        CONTROLLED_BY_INTAKE,
    }
    private OutState outState;

    private boolean extendTrigger;
    private boolean sampleDropButton;
    private double slidePow;
    private boolean specimenCycleButton;

    public TeleRobotANewHope(Hardware hardware) {
        this(
                hardware.extend,
                new Grabber(hardware, Hardware.InitialConfiguration.TELEOP),
                new ControlledLift(hardware),
                new PivotControl(hardware),
                new Arm(hardware, Hardware.InitialConfiguration.TELEOP)
        );
        this.telemetry = hardware.opMode.telemetry;
    }

    public TeleRobotANewHope(Extendo extend, Grabber grabber, ControlledLift lift, PivotControl pivot, Arm arm) {
        this.extend = extend;
        this.grabber = grabber;
        this.lift = lift;
        this.arm = arm;

        this.inState = InState.IDLE;
        this.outState = OutState.INITIAL;

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

    public void swivelBumpLeft() {
        if (inState == InState.PICKUP) {
            this.grabber.swivelBumpLeft();
        }
    }

    public void swivelBumpRight() {
        if (inState == InState.PICKUP) {
            this.grabber.swivelBumpRight();
        }
    }

    public void grabberToggleDown() {
        if (inState == InState.PICKUP) {
            grabberDown = !grabberDown;
            if (grabberDown) {
                this.grabber.toDown();
            } else {
                this.grabber.toUp();
            }
        }
    }

    private void helperToSamplePickup() {
        inState = InState.PICKUP;
        this.extend.setPosition(extendPos);
        if (grabberDown) this.grabber.toDown();
        else this.grabber.toUp();
    }

    private void helperOutToTransfer() {
        outState = OutState.TO_TRANSFER;
        arm.toPreTransfer();
        lift.setTarget(0);
    }

    private void helperToPickupAndTransfer() {
        helperToSamplePickup();
        helperOutToTransfer();
    }

    private void helperToWall(boolean facingPosY) {
        if (facingPosY) {
            outState = OutState.TO_WALL_BACK;
            arm.toBackWall();
        } else {
            outState = OutState.TO_WALL_FRONT;
            arm.toFrontWall();
        }
        lift.setTarget(0);
    }

    private void helperToChamber(boolean facingPosY) {
        if (facingPosY) {
            outState = OutState.TO_CHAMBER_FRONT;
            arm.toFrontChamber();
            lift.setTarget(ControlledLift.CHAMBER_FRONT);
        } else {
            outState = OutState.TO_CHAMBER_BACK;
            arm.toBackChamber();
            lift.setTarget(ControlledLift.CHAMBER_BACK);
        }
    }

    private void helperToBasket(boolean facingPosY) {
        if (facingPosY) {
            inState = InState.TO_BASKET_BACK;
            arm.toBackBasket();
//            lift.setTarget(ControlledLift.BASKET_BACK);
        } else {
            inState = InState.TO_BASKET_FRONT;
            arm.toFrontBasket();
//            lift.setTarget(ControlledLift.BASKET_FRONT);
        }
        lift.clearTarget();
    }

    private void helperToDrop(boolean facingPosY) {
        if (facingPosY) {
            inState = InState.TO_DROP_BACK;
            arm.toBackDrop();
        } else {
            inState = InState.TO_DROP_FRONT;
            arm.toFrontDrop();
        }
        lift.setTarget(0);
    }

    public void update(double dt, boolean facingPosY) {
        switch (inState) {
            case IDLE:
                if (extendTrigger) {
                    extendPos = Extendo.OUT_GRABBER_UP;
                    grabberDown = false;
                    helperToSamplePickup();
                }
                break;
            case PICKUP:
                if (!extendTrigger) {
                    inState = InState.TO_TRANSFER;
                    this.extend.setPosition(0.0);
                    this.grabber.toTransfer();
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
            case TO_TRANSFER:
                if (sampleDropButton) {
                    inState = InState.IDLE;
                    this.grabber.toIn();
                } else if (extendTrigger) {
                    helperToSamplePickup();
                } else if (!this.extend.isBusy() && !this.grabber.isBusy()) {
                    inState = InState.TRANSFER_HOLD;
                }
                break;
            case TRANSFER_HOLD:
                if (sampleDropButton) {
                    inState = InState.IDLE;
                    this.grabber.toIn();
                } else if (extendTrigger) {
                    helperToSamplePickup();
                } else if (outState == OutState.CONTROLLED_BY_INTAKE) {
                    inState = InState.TRANSFER_1;
                    arm.toTransfer();
                }
                break;
            case TRANSFER_1:
                if (sampleDropButton) {
                    inState = InState.IDLE;
                    this.grabber.toIn();
                } else if (extendTrigger) {
                    helperToPickupAndTransfer();
                } else if (!arm.isBusy()) {
                    inState = InState.TRANSFER_2;
                    arm.grab();
                }
                break;
            case TRANSFER_2:
                if (!arm.isBusy()) {
                    inState = InState.TRANSFER_3;
                    grabber.openClawAtTransfer();
                }
                break;
            case TRANSFER_3:
                if (!grabber.isBusy()) {
                    grabber.toIn();

                    helperToDrop(facingPosY);
                }
                break;
            case TO_BASKET_FRONT:
                lift.setPower(slidePow);
                if (extendTrigger) {
                    helperToPickupAndTransfer();
//                } else if (facingPosY) {
//                    inState = InState.TO_BASKET_BACK;
//                    arm.toBackBasket();
//                    lift.setTarget(ControlledLift.HIGH_BASKET * 10);
                } else if (slidePow < 0 && lift.getCurrentPos() <= 2000) {
                    helperToDrop(facingPosY);
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    inState = InState.BASKET_FRONT;
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case BASKET_FRONT:
                lift.setPower(slidePow);
                if (extendTrigger) {
                    helperToPickupAndTransfer();
//                } else if (facingPosY) {
//                    inState = InState.TO_BASKET_BACK;
//                    arm.toBackBasket();
//                    lift.setTarget(ControlledLift.HIGH_BASKET * 10);
                } else if (slidePow < 0 && lift.getCurrentPos() <= 2000) {
                    helperToDrop(facingPosY);
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case TO_BASKET_BACK:
                lift.setPower(slidePow);
                if (extendTrigger) {
                    helperToPickupAndTransfer();
//                } else if (!facingPosY) {
//                    inState = InState.TO_BASKET_FRONT;
//                    arm.toFrontBasket();
//                    lift.setTarget(ControlledLift.HIGH_BASKET * 10);
                } else if (slidePow < 0 && lift.getCurrentPos() <= 2000) {
                    helperToDrop(facingPosY);
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    inState = InState.BASKET_BACK;
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case BASKET_BACK:
                lift.setPower(slidePow);
                if (extendTrigger) {
                    helperToPickupAndTransfer();
//                } else if (!facingPosY) {
//                    inState = InState.TO_BASKET_FRONT;
//                    arm.toFrontBasket();
//                    lift.setTarget(ControlledLift.HIGH_BASKET * 10);
                } else if (slidePow < 0 && lift.getCurrentPos() <= 2000) {
                    helperToDrop(facingPosY);
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case TO_DROP_FRONT:
                if (extendTrigger) {
                    helperToPickupAndTransfer();
                } else if (facingPosY) {
                    inState = InState.TO_DROP_BACK;
                    arm.toBackDrop();
                } else if (slidePow > 0) {
                    helperToBasket(facingPosY);
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    inState = InState.DROP_FRONT;
                }
                break;
            case DROP_FRONT:
                if (extendTrigger) {
                    helperToPickupAndTransfer();
                } else if (facingPosY) {
                    inState = InState.TO_DROP_BACK;
                    arm.toBackDrop();
                } else if (slidePow > 0) {
                    helperToBasket(facingPosY);
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case TO_DROP_BACK:
                if (extendTrigger) {
                    helperToPickupAndTransfer();
                } else if (!facingPosY) {
                    inState = InState.TO_DROP_FRONT;
                    arm.toFrontDrop();
                } else if (slidePow > 0) {
                    helperToBasket(facingPosY);
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    inState = InState.DROP_BACK;
                }
                break;
            case DROP_BACK:
                if (extendTrigger) {
                    helperToPickupAndTransfer();
                } else if (!facingPosY) {
                    inState = InState.TO_DROP_FRONT;
                    arm.toFrontDrop();
                } else if (slidePow > 0) {
                    helperToBasket(facingPosY);
                } else if (sampleDropButton) {
                    inState = InState.BASKET_DROP;
                    arm.drop();
                }
                break;
            case BASKET_DROP:
                if (!arm.isBusy()) {
                    inState = InState.IDLE;
                }
                break;
        }

        boolean intakeWantsOuttake = false;
        switch (inState) {
            case PICKUP:
            case TO_TRANSFER:
            case TRANSFER_HOLD:
                intakeWantsOuttake = true;
                break;
        }
        if (intakeWantsOuttake) switch (outState) {
            case TO_WALL_FRONT:
            case WALL_FRONT:
            case TO_WALL_BACK:
            case WALL_BACK:
                helperOutToTransfer();
                break;
        }

        switch (outState) {
            case INITIAL:
                helperToWall(facingPosY);
                break;
            case TO_WALL_FRONT:
                if (facingPosY) {
                    outState = OutState.TO_WALL_BACK;
                    arm.toBackWall();
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    outState = OutState.WALL_FRONT;
                }
                break;
            case WALL_FRONT:
                if (facingPosY) {
                    outState = OutState.TO_WALL_BACK;
                    arm.toBackWall();
                } else if (specimenCycleButton) {
                    outState = OutState.WALL_GRABBING;
                    arm.grab();
                }
                break;
            case TO_WALL_BACK:
                if (!facingPosY) {
                    outState = OutState.TO_WALL_FRONT;
                    arm.toFrontWall();
                } else if (!arm.isBusy() && !lift.isBusy()) {
                    outState = OutState.WALL_BACK;
                }
                break;
            case WALL_BACK:
                if (!facingPosY) {
                    outState = OutState.TO_WALL_FRONT;
                    arm.toFrontWall();
                } else if (specimenCycleButton) {
                    outState = OutState.WALL_GRABBING;
                    arm.grab();
                }
                break;
            case WALL_GRABBING:
                if (!arm.isBusy()) {
                    helperToChamber(facingPosY);
                }
                break;
            case TO_CHAMBER_FRONT:
//                if (!facingPosY) {
//                    outState = OutState.TO_CHAMBER_BACK;
//                    arm.toBackChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                } else
                if (!arm.isBusy() && !lift.isBusy()) {
                    outState = OutState.CHAMBER_FRONT;
                }
                break;
            case CHAMBER_FRONT:
//                if (!facingPosY) {
//                    outState = OutState.TO_CHAMBER_BACK;
//                    arm.toBackChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                } else
                if (specimenCycleButton) {
                    outState = OutState.CHAMBER_DROPPING;
                    arm.drop();
                }
                break;
            case TO_CHAMBER_BACK:
//                if (facingPosY) {
//                    outState = OutState.TO_CHAMBER_FRONT;
//                    arm.toFrontChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                } else
                if (!arm.isBusy() && !lift.isBusy()) {
                    outState = OutState.CHAMBER_BACK;
                }
                break;
            case CHAMBER_BACK:
//                if (facingPosY) {
//                    outState = OutState.TO_CHAMBER_FRONT;
//                    arm.toFrontChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                } else
                if (specimenCycleButton) {
                    outState = OutState.CHAMBER_DROPPING;
                    arm.drop();
                }
                break;
            case CHAMBER_DROPPING:
                if (!arm.isBusy()) {
                    helperToWall(facingPosY);
                }
                break;
            case TO_TRANSFER:
                if (!arm.isBusy() && !lift.isBusy()) {
                    outState = OutState.CONTROLLED_BY_INTAKE;
                }
                break;
            case CONTROLLED_BY_INTAKE:
                if (inState == InState.IDLE) {
                    helperToWall(facingPosY);
                }
                break;
        }

        this.extend.update(dt);
        this.grabber.update(dt);
        this.lift.update(dt);
        this.arm.update(dt);

        if (telemetry != null) {
            telemetry.addLine(String.format("Lift: %d, %s", lift.getCurrentPos(), lift.isBusy() ? "busy" : "not busy"));
        }

        this.sampleDropButton = false;
        this.specimenCycleButton = false;
    }
}
