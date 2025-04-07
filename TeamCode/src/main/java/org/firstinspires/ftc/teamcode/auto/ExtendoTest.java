package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeWrapper;
import org.firstinspires.ftc.teamcode.hardware.InWrist;
import org.firstinspires.ftc.teamcode.hardware.OutShoulder;
import org.firstinspires.ftc.teamcode.teleop.Gamepads;
import org.firstinspires.ftc.teamcode.units.Distance;

@TeleOp
public class ExtendoTest extends OpModeWrapper {
    public int inches = 0;

    public void setup() {
//        hardware.outShoulder.setPosition(OutShoulder.State.PRE_TRANSFER);
        hardware.inWrist.setPosition(InWrist.State.DOWN);
    }

    public void run() {
        if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_UP)) inches++;
        if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_DOWN)) inches--;
        telemetry.addData("inches", inches);
        hardware.extend.setClawX(Distance.inInches(inches));
    }
}
