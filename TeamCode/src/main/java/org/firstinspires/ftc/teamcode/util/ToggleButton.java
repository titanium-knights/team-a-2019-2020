package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ToggleButton {
    public enum Mode {
        ON_PRESS, ON_RELEASE
    }

    private Button button;
    private Mode mode;
    private boolean state;

    public ToggleButton(Button button, Mode mode, boolean state) {
        this.button = button;
        this.mode = mode;
    }

    public ToggleButton(Button button, Mode mode) {
        this(button, mode, false);
    }

    public ToggleButton(Button button) {
        this(button, Mode.ON_PRESS);
    }

    public static ToggleButton make(Gamepad gamepad, String buttonName) {
        try {
            return new ToggleButton(new Button(gamepad, gamepad.getClass().getDeclaredField(buttonName)));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(buttonName + " is not a button");
        }
    }

    public Button getButton() {
        return button;
    }

    public void toggle() {
        state = !state;
    }

    public void update() {
        button.update();

        if (mode == Mode.ON_PRESS) {
            if (button.wasPressed()) {
                toggle();
            }
        } else if (mode == Mode.ON_RELEASE) {
            if (button.wasReleased()) {
                toggle();
            }
        }
    }

    public boolean getState() {
        return state;
    }
}
