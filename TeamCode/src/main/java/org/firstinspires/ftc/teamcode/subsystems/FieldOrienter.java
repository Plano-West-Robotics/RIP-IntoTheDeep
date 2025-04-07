package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Imu;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class FieldOrienter {
    private final Imu imu;
    private Angle currentYaw;
    private Angle offset;

    public FieldOrienter(Hardware hardware) {
        this(hardware.imu);
    }

    public FieldOrienter(Imu imu) {
        this.imu = imu;
        this.currentYaw = this.offset = imu.getYaw();
    }

    public void update() {
        this.currentYaw = imu.getYaw();
    }

    public void reset() {
        this.offset = this.currentYaw;
    }

    public Vector2 fieldToRobot(Vector2 v) {
        return v.rot(currentYaw.sub(offset).neg());
    }
    public Distance2 fieldToRobot(Distance2 v) {
        return v.rot(currentYaw.sub(offset).neg());
    }
    public Angle fieldToRobot(Angle v) {
        return v.add(currentYaw.sub(offset).neg());
    }

    public Vector2 robotToField(Vector2 v) {
        return v.rot(currentYaw.sub(offset));
    }
    public Distance2 robotToField(Distance2 v) {
        return v.rot(currentYaw.sub(offset));
    }
    public Angle robotToField(Angle v) {
        return v.add(currentYaw.sub(offset));
    }

    public boolean facingPosY() {
        return this.currentYaw.modSigned().abs().valInDegrees() < 90;
    }
}
