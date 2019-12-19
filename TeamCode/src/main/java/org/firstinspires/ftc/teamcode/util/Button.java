package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;

public class Button {
    private Gamepad gamepad;
    private Field field;

    private boolean previous = false;
    private boolean current = false;

    public Button(Gamepad gamepad, Field field) {
        this.gamepad = gamepad;
        this.field = field;
    }

    public static Button make(Gamepad gamepad, String buttonName) {
        try {
            return new Button(gamepad, gamepad.getClass().getDeclaredField(buttonName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(buttonName + " is not a button");
        }
    }

    public void update() {
        previous = current;
        try {
            current = field.getBoolean(gamepad);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Button update failed");
        }
    }

    public boolean wasPressed() {
        return current && !previous;
    }

    public boolean wasReleased() {
        return previous && !current;
    }
}
