package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * <p>Class that uses the {@link Button} class to emulate a toggle button.</p>
 *
 * <p>Toggle buttons have a state, which represents whether they have been toggled on or off.</p>
 *
 * <p>You use a toggle button in a manner similar to that of {@link Button}. See {@link Button} for usage instructions.</p>
 *
 * <p>Bear in mind that this class is not a subclass of {@link Button}.</p>
 *
 * <p>If using this class, <strong>do not</strong> call {@link Button#update()} and {@link ToggleButton#update()} in the
 * same {@link OpMode#loop()} method. {@link ToggleButton#update()} calls {@link Button#update()} for you. Doing so
 * yourself will result in undefined behavior.</p>
 */
public class ToggleButton {
    /**
     * Determines the behavior of the toggle button.
     */
    public enum Mode {
        /**
         * The button is toggled as soon as the button is pressed.
         */
        ON_PRESS,

        /**
         * The button is toggled after the button is released.
         */
        ON_RELEASE
    }

    private Button button;
    private Mode mode;
    private boolean state;

    /**
     * Constructs a ToggleButton using a {@link Button} object, a mode, and a current state.
     * @param button button to track.
     * @param mode behavior of the toggle button.
     * @param state initial state of the toggle button.
     */
    public ToggleButton(Button button, Mode mode, boolean state) {
        this.button = button;
        this.mode = mode;
    }

    /**
     * Constructs a ToggleButton using a {@link Button} object and a mode. Sets the initial state to false.
     * @param button button to track.
     * @param mode behavior of the toggle button.
     */
    public ToggleButton(Button button, Mode mode) {
        this(button, mode, false);
    }

    /**
     * Constructs a ToggleButton that tracks a {@link Button} object. Sets the initial state to false, and toggles the button
     * as soon as it is pressed.
     * @param button button to track
     */
    public ToggleButton(Button button) {
        this(button, Mode.ON_PRESS);
    }

    /**
     * <p>Makes a toggle button using a gamepad and a button name. Sets the initial state to false, and toggles the button
     * as soon as it is pressed.</p>
     *
     * <p>This method obtains the field automatically, so most op modes will use this method to create instances of
     * {@link ToggleButton}.</p>
     *
     * @param gamepad gamepad to track.
     * @param buttonName name of the button to track. Must be the name of a boolean property of {@link Gamepad}.
     * @throws IllegalArgumentException if the given button name does not exist on the gamepad.
     * @return a gamepad that tracks a button on a gamepad.
     */
    public static ToggleButton make(Gamepad gamepad, String buttonName) {
        return new ToggleButton(Button.make(gamepad, buttonName));
    }

    /**
     * Returns the toggle button's underlying {@link Button} instance.
     * @return the underlying button.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Toggles the button's state programmatically.
     */
    public void toggle() {
        state = !state;
    }

    /**
     * <p>Updates the underlying button, then toggle the toggle button's state if the button was pushed.</p>
     *
     * <p>Calls {@link Button#update()}.</p>
     *
     * @throws RuntimeException if {@link Button#update()} threw.
     */
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

    /**
     * Returns the current state (whether the button is on or off.)
     * @return the current state.
     */
    public boolean getState() {
        return state;
    }
}
