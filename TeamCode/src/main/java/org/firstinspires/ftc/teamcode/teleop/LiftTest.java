package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.hardware.Extendo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Lift;
import org.firstinspires.ftc.teamcode.hardware.LiftPivot;
import org.firstinspires.ftc.teamcode.hardware.RawHardware;
import org.firstinspires.ftc.teamcode.subsystems.PivotControl;
import org.firstinspires.ftc.teamcode.util.DeltaTimer;

@TeleOp(name = "This is the lift test")
@Config
public class LiftTest extends OpMode {
    Lift lift;
    PivotControl pivot;
    Extendo extendo;
    DeltaTimer dter;

    public static double LIFT_POW = 0;
    public static int PIVOT_TICKS = PivotControl.MIN_TICKS;
    public static double EXTENDO_POS = 0;

    @Override
    public void init() {
        RawHardware raw = new RawHardware(hardwareMap);
        lift = new Lift(raw, Hardware.InitialConfiguration.TELEOP);
        pivot = new PivotControl(new LiftPivot(raw, Hardware.InitialConfiguration.TELEOP));
        extendo = new Extendo(raw, Hardware.InitialConfiguration.TELEOP);
        dter = new DeltaTimer(true);
    }

    @Override
    public void loop() {
        double dt = dter.poll();

        lift.setPower(LIFT_POW);
        pivot.setTarget(PIVOT_TICKS);
        pivot.update(dt);
        extendo.setPosition(EXTENDO_POS);
    }
}
