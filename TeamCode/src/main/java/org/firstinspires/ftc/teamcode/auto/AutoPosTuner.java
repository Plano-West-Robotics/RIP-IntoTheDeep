package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.RawHardware;
import org.firstinspires.ftc.teamcode.poser.PIDController;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.ControlledLift;
import org.firstinspires.ftc.teamcode.subsystems.Grabber;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp
@Config
public class AutoPosTuner extends OpMode {
    private enum ArmPos {
        TRANSFER,
        WALL_FRONT,
        WALL_BACK,
        CHAMBER_FRONT,
        CHAMBER_BACK,
        BASKET_FRONT,
        BASKET_BACK,
    }

    public static ArmPos ARM_POS = ArmPos.TRANSFER;
    public static int LIFT_TICKS = 0;
    public static double EXTENDO = 0;
    public static double POS_X = 0;
    public static double POS_Y = 0;
    public static double POS_YAW = 0;

    private Hardware hardware;
    private Arm arm;
    private ControlledLift lift;
    private Extendo extend;
    private Grabber grabber;
    private Poser poser;
    private DeltaTimer dter;

    private Poser.TuningMotion motion;

    public static double T_KP = 3.5;
    public static double T_KI = 0;
    public static double T_KD = 0.25;
    public static double Y_KP = 3.5;
    public static double Y_KI = 0;
    public static double Y_KD = 0.15;

    @Override
    public void init() {
        hardware = new Hardware(this, Hardware.InitialConfiguration.AUTO);

        arm = new Arm(hardware, Hardware.InitialConfiguration.AUTO);
        lift = new ControlledLift(hardware);
        extend = hardware.extend;
        grabber = new Grabber(hardware, Hardware.InitialConfiguration.AUTO);
        Pose initialPose = AutoBase.computeInitialPose(AutoBase.LeftOrRight.LEFT, false);
        poser = new Poser(hardware, 1.0, false, initialPose);
        motion = poser.new TuningMotion(initialPose);
        dter = new DeltaTimer(true);

        ARM_POS = ArmPos.TRANSFER;
        LIFT_TICKS = 0;
        EXTENDO = 0;
        POS_X = initialPose.pos.x.valInInches();
        POS_Y = initialPose.pos.y.valInInches();
        POS_YAW = initialPose.yaw.valInDegrees();
    }

    @Override
    public void loop() {
        double dt = dter.poll();

        switch (ARM_POS) {
            case TRANSFER:
                arm.toTransfer();
                break;
            case WALL_FRONT:
                arm.toFrontWall();
                break;
            case WALL_BACK:
                arm.toBackWall();
                break;
            case CHAMBER_FRONT:
                arm.toFrontChamber();
                break;
            case CHAMBER_BACK:
                arm.toBackChamber();
                break;
            case BASKET_FRONT:
                arm.toFrontBasket();
                break;
            case BASKET_BACK:
                arm.toBackBasket();
                break;
        }

        lift.setTarget(LIFT_TICKS);

        extend.setPosition(EXTENDO);

        motion.setTarget(new Pose(Distance2.inInches(POS_X, POS_Y), Angle.inDegrees(POS_YAW)));
        motion.setTransCoeffs(T_KP, T_KI, T_KD);
        motion.setYawCoeffs(Y_KP, Y_KI, Y_KD);

        arm.update(dt);
        lift.update(dt);
        extend.update(dt);
        motion.update();
    }
}
