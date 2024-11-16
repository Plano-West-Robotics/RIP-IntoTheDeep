package org.firstinspires.ftc.teamcode.macro;

/**
 * An `Action` that executes another `Action` repeatedly, forever.
 */
public class Repeat implements Action {
    private final Action action;

    /**
     * Creates a `Repeat` from the given `Action`.
     * @param action the action to execute
     * @return the `Repeat`
     */
    public static Repeat forever(Action action) {
        return new Repeat(action);
    }

    public Repeat(Action action) {
        this.action = action;
    }

    public ControlFlow update() {
        if (action.update().shouldStop()) action.end();

        return ControlFlow.CONTINUE;
    }

    public void end() {
        action.end();
    }
}
