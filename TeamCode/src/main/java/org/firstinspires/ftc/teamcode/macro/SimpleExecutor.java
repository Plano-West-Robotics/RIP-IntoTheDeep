package org.firstinspires.ftc.teamcode.macro;

/**
 * A simple executor for a single `Action`.
 */
public class SimpleExecutor {
    private Action action;

    public SimpleExecutor() {
        this.action = null;
    }

    public boolean isRunning() {
        return action != null;
    }

    public void run(Action action) {
        this.action = action;
    }

    public void stop() {
        if (this.action != null) {
            action.end();
            action = null;
        }
    }

    public void update() {
        if (this.action != null) {
            if (action.update().shouldStop()) {
                action.end();
                action = null;
            }
        }
    }
}
