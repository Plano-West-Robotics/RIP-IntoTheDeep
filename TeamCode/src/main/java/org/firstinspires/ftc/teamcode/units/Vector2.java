package org.firstinspires.ftc.teamcode.units;

public class Vector2 {
    public final double x;
    public final double y;

    public static final Vector2 ZERO = new Vector2(0, 0);

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Vector2 rhs) {
        return this.x == rhs.x && this.y == rhs.y;
    }

    public Vector2 add(Vector2 rhs) {
        return new Vector2(this.x + rhs.x, this.y + rhs.y);
    }

    public Vector2 sub(Vector2 rhs) {
        return new Vector2(this.x - rhs.x, this.y - rhs.y);
    }

    public Vector2 mul(double rhs) {
        return new Vector2(this.x * rhs, this.y * rhs);
    }

    public Distance2 mul(Distance rhs) {
        return new Distance2(rhs.mul(this.x), rhs.mul(this.y));
    }

    public Vector2 div(double rhs) {
        return new Vector2(this.x / rhs, this.y / rhs);
    }

    public Vector2 rot(Angle rhs) {
        double sin = rhs.sin();
        double cos = rhs.cos();
        return new Vector2(this.x * cos - this.y * sin, this.x * sin + this.y * cos);
    }

    public Vector2 neg() {
        return new Vector2(-this.x, -this.y);
    }

    public double magnitude() {
        return Math.sqrt(this.sqMagnitude());
    }

    public double sqMagnitude() {
        return this.x * this.x + this.y * this.y;
    }

    public Vector2 normalized() {
        if (this.magnitude() == 0) {
            return Vector2.ZERO;
        } else {
            return this.div(this.magnitude());
        }
    }

    public Angle angle() {
        return Angle.inRadians(Math.atan2(this.y, this.x));
    }
}
