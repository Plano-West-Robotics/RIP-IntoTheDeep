package org.firstinspires.ftc.teamcode.util;

import java.util.function.IntSupplier;

public class IntDelta {
    private int value;
    private boolean initialised;
    private IntSupplier supplier;

    public IntDelta(IntSupplier supplier) {
        this(true, supplier);
    }

    public IntDelta(boolean lazy, IntSupplier supplier) {
        if (lazy) {
            this.initialised = false;
        } else {
            this.value = supplier.getAsInt();
            this.initialised = true;
        }
        this.supplier = supplier;
    }

    public int poll() {
        if (!this.initialised) {
            this.value = supplier.getAsInt();
            this.initialised = true;
            return 0;
        }

        int newValue = supplier.getAsInt();
        int delta = newValue - this.value;
        this.value = newValue;
        return delta;
    }

    public int getValue() {
        if (!this.initialised) {
            this.value = supplier.getAsInt();
            this.initialised = true;
        }

        return this.value;
    }
}
