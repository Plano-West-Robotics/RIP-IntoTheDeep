package org.firstinspires.ftc.teamcode.macro;

/**
 * An `Action` that executes one `Action` until another `Action` completes.
 */
public class RunUntil implements Action {
    private final Action condition;
    private final Action action;
    boolean complete;

    /**
     * Creates a `RunWhile` from two `Action`s.
     * @param condition the condition `Action` that dictates when this `RunWhile` ends
     * @param action the `Action` that runs until the condition ends
     * @return the `RunWhile`
     */
    public static RunUntil firstCompletes(Action condition, Action action) {
        return new RunUntil(condition, action);
    }

    public RunUntil(Action condition, Action action) {
        this.condition = condition;
        this.action = action;
        this.complete = false;
    }

    public ControlFlow update() {
        if (!complete) {
            if (action.update().shouldStop()) {
                action.end();
                complete = true;
            }
        }

        return condition.update();
    }

    public void end() {
        if (!complete) action.end();
        condition.end();
        complete = false;
    }
}
