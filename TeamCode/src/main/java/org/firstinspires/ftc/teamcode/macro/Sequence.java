/**
 * The Sequence class is a series of Actions to be ran
 */
package org.firstinspires.ftc.teamcode.macro;

/**
 * An `Action` that executes other `Actions` in sequence.
 */
public class Sequence implements Action {
    private final Action[] actions;
    private int i;

    /**
     * Creates a `Sequence` from any amount of `Actions`.
     * @param actions the list of actions
     * @return the `Sequence`
     */
    public static Sequence of(Action... actions) {
        return new Sequence(actions);
    }

    public Sequence(Action[] actions) {
        this.actions = actions;
        i = 0;
    }

    public ControlFlow update() {
        while (i < actions.length) {
            if (actions[i].update().shouldContinue()) {
                return ControlFlow.CONTINUE;
            } else {
                actions[i].end();
                i += 1;
            }
        }

        return ControlFlow.BREAK;
    }

    public void end() {
        if (i < actions.length) {
            actions[i].end();
        }
        i = 0;
    }

    public void run() {
        while (!Thread.interrupted() && i < actions.length) {
            actions[i].run();
            i += 1;
        }
    }
}
