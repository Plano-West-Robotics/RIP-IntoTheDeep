//package org.firstinspires.ftc.teamcode.subsystems;
//
//import org.firstinspires.ftc.teamcode.hardware.Hardware;
//
//public class OuttakeController {
//    private final ControlledLift lift;
//    private final PivotControl pivot;
//    private final Arm arm;
//
//    private enum State {
//        DOWN,
//        UP_WITH_SPECIMEN,
//        UP_WITH_SAMPLE,
//    }
//    private State state;
//
//    public OuttakeController(Hardware hardware) {
//        this(
//                new ControlledLift(hardware),
//                new PivotControl(hardware, Hardware.InitialConfiguration.TELEOP),
//                new Arm(hardware, Hardware.InitialConfiguration.TELEOP)
//        );
//    }
//
//    public OuttakeController(ControlledLift lift, PivotControl pivot, Arm arm) {
//        this.lift = lift;
//        this.pivot = pivot;
//        this.arm = arm;
//
//        state = State.DOWN;
//    }
//
//    public void update(double dt, boolean hasSample, boolean facingPosY) {
//
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
