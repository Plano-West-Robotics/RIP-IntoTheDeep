package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.teleop.Gamepads;

public class Controls {
    // ======================= GAMEPAD 1 ============================
    public static Gamepads.AnalogInput STRAIGHT = Gamepads.AnalogInput.GP1_LEFT_STICK_Y;
    public static Gamepads.AnalogInput STRAFE = Gamepads.AnalogInput.GP1_LEFT_STICK_X;
    public static Gamepads.AnalogInput TURN = Gamepads.AnalogInput.GP1_RIGHT_STICK_X;
    public static Gamepads.Button SLOW_MODE = Gamepads.Button.GP1_RIGHT_TRIGGER;
    public static Gamepads.Button YAW_RESET = Gamepads.Button.GP1_DPAD_UP;
    public static Gamepads.Button SPECIMEN_CYCLE = Gamepads.Button.GP1_SQUARE;

    // ======================= GAMEPAD 2 ============================
    public static Gamepads.Button EXTEND = Gamepads.Button.GP2_RIGHT_TRIGGER;
    public static Gamepads.AnalogInput SLIDES = Gamepads.AnalogInput.GP2_LEFT_STICK_Y;
    public static Gamepads.AnalogInput STRAFE_2 = Gamepads.AnalogInput.GP2_RIGHT_STICK_X;
    public static Gamepads.Button INTAKE_TOGGLE_DOWN = Gamepads.Button.GP2_SQUARE;
    public static Gamepads.Button INTAKE_BUMP_LEFT = Gamepads.Button.GP2_LEFT_BUMPER;
    public static Gamepads.Button INTAKE_BUMP_RIGHT = Gamepads.Button.GP2_LEFT_TRIGGER;
    public static Gamepads.Button DROP_SAMPLE = Gamepads.Button.GP2_CROSS;
    public static Gamepads.Button HALF_SEQUENCE_MODE = Gamepads.Button.GP2_CIRCLE;
    public static Gamepads.Button FULL_SEQUENCE_MODE = Gamepads.Button.GP2_TRIANGLE;
}
