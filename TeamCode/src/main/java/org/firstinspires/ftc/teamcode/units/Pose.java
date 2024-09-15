package org.firstinspires.ftc.teamcode.units;

public class Pose {
    public final Distance2 pos;
    public final Angle yaw;

    public final static Pose ZERO = new Pose(
            Distance2.ZERO,
            Angle.ZERO
    );

    public Pose(Distance2 pos, Angle yaw) {
        this.pos = pos;
        this.yaw = yaw;
    }

    public Pose then(Pose other) {
        return new Pose(
                this.pos.add(other.pos.rot(this.yaw)),
                this.yaw.add(other.yaw)
        );
    }

    public Pose sub(Pose base) {
        return new Pose(
                this.pos.sub(base.pos).rot(base.yaw.neg()),
                this.yaw.sub(base.yaw)
        );
    }

    public Pose scale(double k) {
        return new Pose(
                this.pos.mul(k),
                this.yaw.mul(k)
        );
    }

    public Pose flippedAcrossXAxis() {
        return new Pose(
                new Distance2(this.pos.x, this.pos.y.neg()),
                this.yaw.neg()
        );
    }

    public String toString() {
        return this.pos + " @ " + this.yaw;
    }
}
