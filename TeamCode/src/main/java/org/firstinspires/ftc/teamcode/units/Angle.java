package org.firstinspires.ftc.teamcode.units;

public class Angle {
    private static final double TAU = 2 * Math.PI;

    private final double val; // in radians

    public static final Angle ZERO = new Angle(0);
    public static final Angle FORWARD = Angle.inRadians(0);
    public static final Angle LEFT = Angle.inRadians(Math.PI / 2);
    public static final Angle BACKWARD = Angle.inRadians(Math.PI);
    public static final Angle RIGHT = Angle.inRadians(3 * Math.PI / 2);

    public static Angle inDefaultUnits(double val) {
        return new Angle(val);
    }

    public static Angle inDegrees(double val) {
        return Angle.inRadians(Math.toRadians(val));
    }

    public static Angle inRadians(double val) {
        return new Angle(val);
    }

    public double valInDefaultUnits() {
        return this.val;
    }

    public double valInDegrees() {
        return Math.toDegrees(this.valInRadians());
    }

    public double valInRadians() {
        return this.val;
    }

    private Angle(double val) {
        this.val = val;
    }

    public String toString() {
        return this.valInDegrees() + " deg";
    }

    public boolean equals(Angle other) {
        return this.val == other.val;
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Angle add(Angle rhs) {
        return new Angle(this.val + rhs.val);
    }

    public Angle sub(Angle rhs) {
        return new Angle(this.val - rhs.val);
    }

    public Angle mul(double rhs) {
        return new Angle(this.val * rhs);
    }

    public Angle div(double rhs) {
        return new Angle(this.val / rhs);
    }

    public double div(Angle rhs) {
        return this.val / rhs.val;
    }

    public Angle neg() {
        return new Angle(-this.val);
    }

    public Angle abs() {
        return new Angle(Math.abs(val));
    }

    private double modTau(double v) {
        v = v % TAU;
        if (v < 0) {
            v += TAU;
        }
        return v;
    }

    public Angle modPositive() {
        return new Angle(modTau(this.val));
    }

    public Angle modNegative() {
        return new Angle(modTau(this.val) - TAU);
    }

    public Angle modSigned() {
        return new Angle(modTau(this.val + Math.PI) - Math.PI);
    }

    public double sin() {
        return Math.sin(this.val);
    }

    public double cos() {
        return Math.cos(this.val);
    }

    public double sinc() {
        return this.isZero() ? 1 : (this.sin() / this.val);
    }

    public double cosc() {
        return this.isZero() ? 0 : ((1 - this.cos()) / this.val);
    }
}
