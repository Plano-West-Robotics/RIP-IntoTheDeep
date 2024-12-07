package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.teleop.Gamepads;

public class Controls {
    // ======================= GAMEPAD 1 ============================
    public static Gamepads.AnalogInput STRAIGHT = Gamepads.AnalogInput.GP1_LEFT_STICK_Y;
    public static Gamepads.AnalogInput STRAFE = Gamepads.AnalogInput.GP1_LEFT_STICK_X;
    public static Gamepads.AnalogInput TURN = Gamepads.AnalogInput.GP1_RIGHT_STICK_X;
    public static Gamepads.Button SLOW_MODE = Gamepads.Button.GP1_RIGHT_TRIGGER;
    public static Gamepads.Button YAW_RESET = Gamepads.Button.GP1_DPAD_UP;
    public static Gamepads.Button BUCKET_DROP = Gamepads.Button.GP1_SQUARE;

    // ======================= GAMEPAD 2 ============================
    public static Gamepads.AnalogInput EXTENDO = Gamepads.AnalogInput.GP2_RIGHT_STICK_Y;
    public static Gamepads.Button EXTENDO_GOTO = Gamepads.Button.GP2_RIGHT_STICK_BUTTON;
    public static Gamepads.Button INTAKE_TOGGLE_DOWN = Gamepads.Button.GP2_SQUARE;
    public static Gamepads.Button INTAKE_BUMP_LEFT = Gamepads.Button.GP2_LEFT_BUMPER;
    public static Gamepads.Button INTAKE_BUMP_RIGHT = Gamepads.Button.GP2_RIGHT_BUMPER;

    public static Gamepads.AnalogInput LIFT = Gamepads.AnalogInput.GP2_LEFT_STICK_Y;
    public static Gamepads.Button LIFT_OVERRIDE = Gamepads.Button.GP2_LEFT_STICK_BUTTON;
    public static Gamepads.Button LIFT_TO_DOWN = Gamepads.Button.GP2_DPAD_DOWN;
    public static Gamepads.Button LIFT_TO_BASKET1 = Gamepads.Button.GP2_DPAD_RIGHT;
    public static Gamepads.Button LIFT_TO_BASKET2 = Gamepads.Button.GP2_DPAD_UP;
    public static Gamepads.Button LIFT_TO_SPECIMEN = Gamepads.Button.GP2_DPAD_LEFT;
    public static Gamepads.Button CLAW_GRAB = Gamepads.Button.GP2_CROSS;
}
