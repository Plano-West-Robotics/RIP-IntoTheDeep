//package org.firstinspires.ftc.teamcode.subsystems;
//
//import com.qualcomm.robotcore.util.Range;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.hardware.Extendo;
//import org.firstinspires.ftc.teamcode.hardware.Hardware;
//import org.firstinspires.ftc.teamcode.units.Angle;
//
//public class TeleRobotElectricBoogaloo {
//    private final static double EXTEND_THRESH = 0.1;
//
//    private final Grabber grabber;
//    private final Extendo extend;
//
//    private final Arm arm;
//    private final ControlledLift lift;
//
//    Telemetry telemetry;
//
//    private enum InState {
//        IDLE,
//        PICKUP,
//        RETURN,
//        TRANSFER,
//    }
//    private InState inState;
//    double extendPos;
//    boolean grabberDown;
//
//    private enum OutState {
//        DOWN,
//        WITH_SPECIMEN,
//        WITH_SAMPLE,
//    }
//    private OutState outState;
//    boolean transferring = false;
//    boolean transferring2 = false;
//    boolean wallGrab = false;
//    boolean dropping = false;
//
//    public TeleRobotElectricBoogaloo(Hardware hardware) {
//        this.grabber = new Grabber(hardware, Hardware.InitialConfiguration.TELEOP);
//        this.extend = hardware.extend;
//        this.arm = new Arm(hardware, Hardware.InitialConfiguration.TELEOP);
//        this.lift = new ControlledLift(hardware);
//
//        telemetry = hardware.opMode.telemetry;
//
//        inState = InState.IDLE;
//        outState = OutState.DOWN;
//    }
//
//    public void swivelBumpLeft() {
//        if (inState == InState.PICKUP) {
//            this.grabber.swivelBumpLeft();
//        }
//    }
//
//    public void swivelBumpRight() {
//        if (inState == InState.PICKUP) {
//            this.grabber.swivelBumpRight();
//        }
//    }
//
//    public void grabberToggleDown() {
//        if (inState == InState.PICKUP) {
//            grabberDown = !grabberDown;
//            if (grabberDown) {
//                this.grabber.toDown();
//            } else {
//                this.grabber.toUp();
//            }
//        }
//    }
//
//    public void update(double dt,
//                       boolean extendTrigger,
//                       double extendPow,
//                       boolean extendCancelButton,
//                       boolean facingPosY,
//                       boolean outtakeButton,
//                       int directionHint
//    ) {
//        switch (inState) {
//            case IDLE:
//                if (extendTrigger) {
//                    inState = InState.PICKUP;
//                    extendPos = Extendo.OUT_GRABBER_UP;
//                    grabberDown = false;
//
//                    this.extend.setPosition(extendPos);
//                    this.grabber.toUp();
//                }
//                break;
//            case PICKUP:
//                if (!extendTrigger) {
//                    inState = InState.RETURN;
//                    this.extend.setPosition(0.0);
//                    this.grabber.toTransfer();
//                } else {
//                    extendPos += 1.0 * dt * extendPow;
//                    extendPos = Range.clip(
//                            extendPos,
//                            EXTEND_THRESH,
//                            this.grabber.isDefinitelyDown() ? Extendo.OUT_GRABBER_DOWN : Extendo.OUT_GRABBER_UP
//                    );
//                    this.extend.setPosition(extendPos);
//                }
//                break;
//            case RETURN:
//                if (extendCancelButton) {
//                    inState = InState.IDLE;
//                    this.grabber.toIn();
//                } else if (extendTrigger) {
//                    inState = InState.PICKUP;
//                    this.extend.setPosition(extendPos);
//                    if (grabberDown) this.grabber.toDown();
//                    else this.grabber.toUp();
//                } else if (!this.extend.isBusy() && !this.grabber.isBusy()) {
//                    inState = InState.TRANSFER;
//                }
//                break;
//            case TRANSFER:
//                if (!transferring && extendCancelButton) {
//                    inState = InState.IDLE;
//                    this.grabber.toIn();
//                } else if (!transferring && extendTrigger) {
//                    inState = InState.PICKUP;
//                    this.extend.setPosition(extendPos);
//                    if (grabberDown) this.grabber.toDown();
//                    else this.grabber.toUp();
//                }
//                break;
//        }
//
//        boolean hasSample = inState == InState.TRANSFER;
//
//        int at = 1234;
//        if (!transferring && !wallGrab && !dropping) switch (outState) {
//            case DOWN:
//                if (hasSample && directionHint == 0) {
//                    lift.setTarget(0);
//                    arm.toTransfer();
//                    at = 0;
//                } else if (facingPosY && directionHint <= 0 || !facingPosY && directionHint > 0) {
//                    arm.toBackWall();
//                    lift.setTarget(0);
//                    at = 1;
//                } else {
//                    arm.toFrontWall();
//                    lift.setTarget(0);
//                    at = 2;
//                }
//                break;
//            case WITH_SAMPLE:
//                if (directionHint <= 0) {
//                    if (facingPosY) {
//                        arm.toBackDrop();
//                        lift.setTarget(0);
//                        at = 5;
//                    } else {
//                        arm.toFrontDrop();
//                        lift.setTarget(0);
//                        at = 6;
//                    }
//                } else {
//                    if (facingPosY) {
//                        arm.toFrontBasket();
//                        lift.setTarget(ControlledLift.HIGH_BASKET * 10);
//                        at = 7;
//                    } else {
//                        arm.toBackBasket();
//                        lift.setTarget(ControlledLift.HIGH_BASKET * 10);
//                        at = 8;
//                    }
//                }
//                break;
//            case WITH_SPECIMEN:
//                if (facingPosY && directionHint >= 0 || !facingPosY && directionHint < 0) {
//                    arm.toFrontChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                    at = 3;
//                } else {
//                    arm.toBackChamber();
//                    lift.setTarget(ControlledLift.HIGH_CHAMBER * 10);
//                    at = 4;
//                }
//                break;
//        }
//
//        grabber.update(dt);
//        extend.update(dt);
//
//        arm.update(dt);
//        lift.update(dt);
//
//        telemetry.addData("foo", outtakeButton);
//        telemetry.addData("bar", arm.isBusy());
//        telemetry.addData("a", arm.pivot.current);
//        telemetry.addData("baz", lift.isBusy());
//
//        if (outtakeButton && !arm.isBusy() && !lift.isBusy()) {
//            if (at == 0) {
//                transferring = true;
//                arm.grab();
//            } else if (at == 1 || at == 2) {
//                wallGrab = true;
//                arm.grab();
//            } else if (at == 5 || at == 6 || at == 7 || at == 8) {
//                dropping = true;
//                arm.drop();
//            } else if (at == 3 || at == 4) {
//                dropping = true;
//                arm.drop();
//            }
//        }
//
//        if (transferring) {
//            if (!transferring2) {
//                if (!arm.isBusy()) {
//                    transferring2 = true;
//                    grabber.openClawAtTransfer();
//                    outState = OutState.WITH_SAMPLE;
//                    inState = InState.IDLE;
//                }
//            } else {
//                if (!grabber.isBusy()) {
//                    transferring = transferring2 = false;
//                    outState = OutState.WITH_SAMPLE;
//                    inState = InState.IDLE;
//                }
//            }
//        }
//
//        if (wallGrab) {
//            if (!arm.isBusy()) {
//                wallGrab = false;
//                outState = OutState.WITH_SPECIMEN;
//            }
//        }
//
//        if (dropping) {
//            if (!arm.isBusy()) {
//                dropping = false;
//                outState = OutState.DOWN;
//            }
//        }
//
//        // State.DOWN && hasSample -> transfer
//        // State.DOWN && !hasSample && facingPosY -> backward wall
//        // State.DOWN && !hasSample && !facingPosY -> forward wall
//        // State.UP_WITH_SPECIMEN && facingPosY -> forward chamber
//        // State.UP_WITH_SPECIMEN && !facingPosY -> backward chamber
//        // State.UP_WITH_SAMPLE && facingPosY -> forward basket // er hm. we need to do both drop and basket
//        // State.UP_WITH_SAMPLE && !facingPosY -> backward basket // ditto
//    }
//}
