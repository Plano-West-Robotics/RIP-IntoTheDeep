/**
 * The Wait class is a user-defined pause Action
 */
package org.firstinspires.ftc.teamcode.macro;

/**
 * An `Action` that waits for a specified amount of time.
 */
public class Wait implements Action {
    private boolean started;
    private long endTime;
    private final long duration;

    private static final long NANOS_PER_SEC = 1000 * 1000 * 1000;
    private static final double NANOS_PER_MILLI = 1000 * 1000;

    /**
     * Constructs a `Wait` with the given delay in seconds.
     * @param secs how long to wait in seconds
     * @return the `Wait`
     */
    public static Wait seconds(double secs) {
        return new Wait((long)(secs * NANOS_PER_SEC));
    }

    /**
     * Constructs a `Wait` with the given delay in milliseconds.
     * @param millis how long to wait in milliseconds
     * @return the `Wait`
     */
    public static Wait millis(double millis) {
        return new Wait((long)(millis * NANOS_PER_MILLI));
    }

    public Wait(long duration) {
        this.started = false;
        this.duration = duration;
    }

    public ControlFlow update() {
        if (!started) {
            started = true;
            endTime = System.nanoTime() + duration;
        } else {
            if (System.nanoTime() >= endTime) {
                return ControlFlow.BREAK;
            }
        }

        return ControlFlow.CONTINUE;
    }

    public void end() {
        started = false;
    }
}