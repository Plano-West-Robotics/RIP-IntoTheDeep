package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.RawHardware;

@Autonomous
public class asrfghjfyyjh extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RawHardware raw = new RawHardware(hardwareMap);
        Drivetrain drivetrain = new Drivetrain(raw);

        waitForStart();

        drivetrain.drive(0.5, 0, 0);
        sleep(1500);
        drivetrain.stop();
    }
}
