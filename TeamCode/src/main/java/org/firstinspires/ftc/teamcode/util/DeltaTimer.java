package org.firstinspires.ftc.teamcode.util;

public class DeltaTimer {
    private long time;

    private final double NANOS_PER_SEC = 1_000_000_000;

    public DeltaTimer() {
        this(true);
    }

    public DeltaTimer(boolean lazy) {
        if (lazy) this.time = -1;
        else this.time = System.nanoTime();
    }

    /**
     * Fetch the time since the last call to <code>poll</code>.
     * @return the delta-time, in seconds
     */
    public double poll() {
        long now = System.nanoTime();

        long dt;
        if (this.time < 0) dt = 0;
        else dt = now - this.time;

        this.time = now;
        return dt / NANOS_PER_SEC;
    }
}
