package org.firstinspires.ftc.teamcode.util;

import java.util.function.DoubleSupplier;

public class DoubleDelta {
    private double value;
    private boolean initialised;
    private DoubleSupplier supplier;

    public DoubleDelta(DoubleSupplier supplier) {
        this(true, supplier);
    }

    public DoubleDelta(boolean lazy, DoubleSupplier supplier) {
        if (lazy) {
            this.initialised = false;
        } else {
            this.value = supplier.getAsDouble();
            this.initialised = true;
        }
        this.supplier = supplier;
    }

    public double poll() {
        if (!this.initialised) {
            this.value = supplier.getAsDouble();
            this.initialised = true;
            return 0;
        }

        double newValue = supplier.getAsDouble();
        double delta = newValue - this.value;
        this.value = newValue;
        return delta;
    }

    public double getValue() {
        if (!this.initialised) {
            this.value = supplier.getAsDouble();
            this.initialised = true;
        }

        return this.value;
    }
}
