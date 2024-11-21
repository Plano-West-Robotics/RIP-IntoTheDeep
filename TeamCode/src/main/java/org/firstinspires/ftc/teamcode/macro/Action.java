/**
 * The Action class represents an action that the robot will take. The Actions will be used for
 * multi-step processes called Macros
 */
package org.firstinspires.ftc.teamcode.macro;

public interface Action {

    /**
     * Updates the `Action` and returns whether it should keep running.
     *
     * After this returns `BREAK` you should call `end` before calling `update` again.
     * @return whether the Action should keep running
     */
    ControlFlow update();

    /**
     * Stops the Action.
     *
     * Should be called after `update` returns `BREAK`, but can be called at any time.
     *
     * The `Action` can be restarted again after this is called.
     */
    void end();

    /**
     * A helper method that runs the `Action` to completion by calling `update` in a loop.
     */
    default void run() {
        while (!Thread.interrupted() && this.update().shouldContinue());
        this.end();
    }

    /**
     * Generates an `Action` from a function that will be run once whenever the `Action` is executed.
     * @param runnable the function
     * @return an Action that will run the function
     */
    static Action fromFn(Runnable runnable) {
        return new Oneshot(runnable);
    }

    /**
     * An `Action` that executes a function when run.
     */
    class Oneshot implements Action {
        Runnable runnable;

        /**
         * Constructs a `Oneshot` action.
         * @param runnable the function to be run
         */
        public Oneshot(Runnable runnable) {
            this.runnable = runnable;
        }

        public ControlFlow update() {
            return ControlFlow.BREAK;
        }

        /**
         *
         */
        public void end() {
            runnable.run();
        }

        public void run() {
            this.end();
        }
    }
}
