package org.firstinspires.ftc.teamcode.units;

public class Distance2 {
    public final Distance x;
    public final Distance y;

    public static final Distance2 ZERO = new Distance2(Distance.ZERO, Distance.ZERO);

    public static Distance2 inTiles(double x, double y) {
        return new Distance2(Distance.inTiles(x), Distance.inTiles(y));
    }

    public static Distance2 inFeet(double x, double y) {
        return new Distance2(Distance.inFeet(x), Distance.inFeet(y));
    }

    public static Distance2 inInches(double x, double y) {
        return new Distance2(Distance.inInches(x), Distance.inInches(y));
    }

    public static Distance2 inMM(double x, double y) {
        return new Distance2(Distance.inMM(x), Distance.inMM(y));
    }

    public Distance2(Distance x, Distance y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Distance2 rhs) {
        return this.x.equals(rhs.x) && this.y.equals(rhs.y);
    }

    public Distance2 add(Distance2 rhs) {
        return new Distance2(this.x.add(rhs.x), this.y.add(rhs.y));
    }

    public Distance2 sub(Distance2 rhs) {
        return new Distance2(this.x.sub(rhs.x), this.y.sub(rhs.y));
    }

    public Distance2 mul(double rhs) {
        return new Distance2(this.x.mul(rhs), this.y.mul(rhs));
    }

    public Distance2 div(double rhs) {
        return new Distance2(this.x.div(rhs), this.y.div(rhs));
    }

    public Vector2 div(Distance rhs) {
        return new Vector2(this.x.div(rhs), this.y.div(rhs));
    }

    public Distance2 rot(Angle rhs) {
        double sin = rhs.sin();
        double cos = rhs.cos();
        return new Distance2(this.x.mul(cos).sub(this.y.mul(sin)), this.x.mul(sin).add(this.y.mul(cos)));
    }

    public Distance2 neg() {
        return new Distance2(this.x.neg(), this.y.neg());
    }

    public Distance magnitude() {
        return Distance.inDefaultUnits(Math.sqrt(this.sqMagnitude().valInDefaultUnits()));
    }

    // HACK: real return unit is square distance
    public Distance sqMagnitude() {
        return Distance.inDefaultUnits(
                this.x.valInDefaultUnits() * this.x.valInDefaultUnits()
                        + this.y.valInDefaultUnits() * this.y.valInDefaultUnits()
        );
    }

    public Vector2 normalized() {
        if (this.magnitude().isZero()) {
            return Vector2.ZERO;
        } else {
            return this.div(this.magnitude());
        }
    }

    public Angle angle() {
        return Angle.inRadians(Math.atan2(this.y.valInDefaultUnits(), this.x.valInDefaultUnits()));
    }
}
