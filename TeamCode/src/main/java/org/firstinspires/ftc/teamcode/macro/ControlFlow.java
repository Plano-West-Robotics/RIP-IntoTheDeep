package org.firstinspires.ftc.teamcode.macro;

/**
 * An enum indicating if an `Action` should `CONTINUE` running or `BREAK`.
 */
public enum ControlFlow {
    CONTINUE, BREAK;

    public boolean shouldContinue() {
        return this == ControlFlow.CONTINUE;
    }

    public boolean shouldStop() {
        return this == ControlFlow.BREAK;
    }

    public static ControlFlow continueIf(boolean cond) {
        return cond ? CONTINUE : BREAK;
    }

    public static ControlFlow stopIf(boolean cond) {
        return cond ? BREAK : CONTINUE;
    }
}