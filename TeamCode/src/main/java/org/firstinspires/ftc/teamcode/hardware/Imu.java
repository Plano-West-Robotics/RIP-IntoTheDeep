package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.units.Angle;

public class Imu {
    private final IMU inner;

    public Imu(HardwareMap hardwareMap) {
        this.inner = hardwareMap.get(IMU.class, "imu");
        inner.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        ));
    }

    public Angle getYaw() {
        // TODO: *wow* the internal imu code is heavy, it's probably irrelevant but it would please
        //       me to replace getRobotYawPitchRollAngles with getRobotOrientationAsQuaternion and
        //       do our own conversion to yaw.
        return Angle.inRadians(inner.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
    }

    public void resetYaw() {
        inner.resetYaw();
    }
}
