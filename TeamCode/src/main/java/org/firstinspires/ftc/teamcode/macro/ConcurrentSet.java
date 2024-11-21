package org.firstinspires.ftc.teamcode.macro;

/**
 * An `Action` that executes other `Action` concurrently.
 */
public class ConcurrentSet implements Action {
    private final Action[] actions;
    private final boolean[] complete;

    /**
     * Creates a `ConcurrentSet` from any amount of `Actions`.
     * @param actions the list of actions
     * @return the `ConcurrentSet`
     */
    public static ConcurrentSet of(Action... actions) {
        return new ConcurrentSet(actions);
    }

    public ConcurrentSet(Action[] actions) {
        this.actions = actions;
        this.complete = new boolean[actions.length];
    }

    public ControlFlow update() {
        boolean allComplete = true;
        for (int i = 0; i < actions.length; i++) {
            if (!complete[i]) {
                if (actions[i].update().shouldStop()) {
                    actions[i].end();
                    complete[i] = true;
                } else {
                    allComplete = false;
                }
            }
        }

        return ControlFlow.stopIf(allComplete);
    }

    public void end() {
        for (int i = 0; i < actions.length; i++) {
            if (!complete[i]) {
                actions[i].end();
            }
            complete[i] = false;
        }
    }
}
