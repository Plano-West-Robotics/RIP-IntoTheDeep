package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Gamepads {
    private final Gamepad prevGp1;
    private final Gamepad gp1;
    private final Gamepad prevGp2;
    private final Gamepad gp2;

    /**
     * Every <b>digital</b> button (buttons that return true/false)
     */
    public enum Button {
        GP1_CROSS,
        GP1_CIRCLE,
        GP1_SQUARE,
        GP1_TRIANGLE,
        GP1_LEFT_BUMPER,
        GP1_RIGHT_BUMPER,
        GP1_LEFT_TRIGGER,
        GP1_RIGHT_TRIGGER,
        GP1_LEFT_STICK_BUTTON,
        GP1_RIGHT_STICK_BUTTON,
        GP1_DPAD_UP,
        GP1_DPAD_DOWN,
        GP1_DPAD_LEFT,
        GP1_DPAD_RIGHT,
        GP1_GUIDE,
        GP1_START,
        GP1_BACK,

        GP2_CROSS,
        GP2_CIRCLE,
        GP2_SQUARE,
        GP2_TRIANGLE,
        GP2_LEFT_BUMPER,
        GP2_RIGHT_BUMPER,
        GP2_LEFT_TRIGGER,
        GP2_RIGHT_TRIGGER,
        GP2_LEFT_STICK_BUTTON,
        GP2_RIGHT_STICK_BUTTON,
        GP2_DPAD_UP,
        GP2_DPAD_DOWN,
        GP2_DPAD_LEFT,
        GP2_DPAD_RIGHT,
        GP2_GUIDE,
        GP2_START,
        GP2_BACK,
    }

    /**
     * Every <b>analog</b> button. (buttons that report values as doubles)
     */
    public enum AnalogInput {
        GP1_LEFT_TRIGGER,
        GP1_RIGHT_TRIGGER,
        GP1_LEFT_STICK_X,
        GP1_LEFT_STICK_Y,
        GP1_RIGHT_STICK_X,
        GP1_RIGHT_STICK_Y,

        GP2_LEFT_TRIGGER,
        GP2_RIGHT_TRIGGER,
        GP2_LEFT_STICK_X,
        GP2_LEFT_STICK_Y,
        GP2_RIGHT_STICK_X,
        GP2_RIGHT_STICK_Y,
    }

    public Gamepads(Gamepad gp1, Gamepad gp2) {
        this();
        this.gp1.copy(gp1);
        this.gp2.copy(gp2);
    }

    public Gamepads() {
        this.prevGp1 = new Gamepad();
        this.gp1 = new Gamepad();
        this.prevGp2 = new Gamepad();
        this.gp2 = new Gamepad();
    }

    /**
     * Determine whether button is pressed
     * @param button Button to check
     * @return whether button is pressed
     */
    public boolean isPressed(Button button) {
        return this.isPressed(button, this.gp1, this.gp2);
    }

    private boolean isPressed(Button button, Gamepad gp1, Gamepad gp2) {
        switch (button) {
            case GP1_CROSS: return gp1.a;
            case GP1_CIRCLE: return gp1.b;
            case GP1_SQUARE: return gp1.x;
            case GP1_TRIANGLE: return gp1.y;
            case GP1_LEFT_BUMPER: return gp1.left_bumper;
            case GP1_RIGHT_BUMPER: return gp1.right_bumper;
            case GP1_LEFT_TRIGGER: return gp1.left_trigger > 0.5;
            case GP1_RIGHT_TRIGGER: return gp1.right_trigger > 0.5;
            case GP1_LEFT_STICK_BUTTON: return gp1.left_stick_button;
            case GP1_RIGHT_STICK_BUTTON: return gp1.right_stick_button;
            case GP1_DPAD_UP: return gp1.dpad_up;
            case GP1_DPAD_DOWN: return gp1.dpad_down;
            case GP1_DPAD_LEFT: return gp1.dpad_left;
            case GP1_DPAD_RIGHT: return gp1.dpad_right;
            case GP1_GUIDE: return gp1.guide;
            case GP1_START: return gp1.start;
            case GP1_BACK: return gp1.back;

            case GP2_CROSS: return gp2.a;
            case GP2_CIRCLE: return gp2.b;
            case GP2_SQUARE: return gp2.x;
            case GP2_TRIANGLE: return gp2.y;
            case GP2_LEFT_BUMPER: return gp2.left_bumper;
            case GP2_RIGHT_BUMPER: return gp2.right_bumper;
            case GP2_LEFT_TRIGGER: return gp2.left_trigger > 0.5;
            case GP2_RIGHT_TRIGGER: return gp2.right_trigger > 0.5;
            case GP2_LEFT_STICK_BUTTON: return gp2.left_stick_button;
            case GP2_RIGHT_STICK_BUTTON: return gp2.right_stick_button;
            case GP2_DPAD_UP: return gp2.dpad_up;
            case GP2_DPAD_DOWN: return gp2.dpad_down;
            case GP2_DPAD_LEFT: return gp2.dpad_left;
            case GP2_DPAD_RIGHT: return gp2.dpad_right;
            case GP2_GUIDE: return gp2.guide;
            case GP2_START: return gp2.start;
            case GP2_BACK: return gp2.back;

            default: throw new RuntimeException("unreachable code entered");
        }
    }

    /**
     * Get the value of an analog input
     * @param input Input to check
     * @return Value of the analog input
     */
    public double getAnalogValue(AnalogInput input) {
        return getAnalogValue(input, gp1, gp2);
    }

    private double getAnalogValue(AnalogInput input, Gamepad gp1, Gamepad gp2) {
        switch (input) {
            case GP1_LEFT_TRIGGER: return gp1.left_trigger;
            case GP1_RIGHT_TRIGGER: return gp1.right_trigger;
            case GP1_LEFT_STICK_X: return gp1.left_stick_x;
            case GP1_LEFT_STICK_Y: return -gp1.left_stick_y;
            case GP1_RIGHT_STICK_X: return gp1.right_stick_x;
            case GP1_RIGHT_STICK_Y: return -gp1.right_stick_y;

            case GP2_LEFT_TRIGGER: return gp2.left_trigger;
            case GP2_RIGHT_TRIGGER: return gp2.right_trigger;
            case GP2_LEFT_STICK_X: return gp2.left_stick_x;
            case GP2_LEFT_STICK_Y: return -gp2.left_stick_y;
            case GP2_RIGHT_STICK_X: return gp2.right_stick_x;
            case GP2_RIGHT_STICK_Y: return -gp2.right_stick_y;

            default: throw new RuntimeException("unreachable code entered");
        }
    }

    /**
     * Determine whether the button was pressed between this timestep and last timestep.
     * @param button Button to check
     * @return whether the button was pressed between this timestep and last timestep.
     */
    public boolean justPressed(Button button) {
        return (!isPressed(button, this.prevGp1, this.prevGp2) && isPressed(button, this.gp1, this.gp2));
    }

    /**
     * Determine whether the button was released between this timestep and last timestep.
     * @param button Button to check
     * @return whether the button was released between this timestep and last timestep.
     */
    public boolean justReleased(Button button) {
        return (isPressed(button, this.prevGp1, this.prevGp2) && !isPressed(button, this.gp1, this.gp2));
    }

    public void update(Gamepad gp1, Gamepad gp2) {
        this.prevGp1.copy(this.gp1);
        this.gp1.copy(gp1);
        this.prevGp2.copy(this.gp2);
        this.gp2.copy(gp2);
    }
}