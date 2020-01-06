package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;

/**
 * <p>Class that tracks a button on the gamepad and determines if it has been bumped (pressed then released).</p>
 *
 * <p>Used to emulate push buttons in the FTC SDK.</p>
 *
 * <h3>Basic Usage</h3>
 * <p>To use this class, create a Button instance using {@link Button#make(Gamepad, String)} during the {@link OpMode#init()} method.
 * Pass {@link Button#make(Gamepad, String)} a gamepad ({@link OpMode#gamepad1} or {@link OpMode#gamepad2}) and the name of a button
 * on the gamepad (such as "dpad_left" or "x".)</p>
 *
 * <p>Then, at the beginning of the {@link OpMode#loop()} method, call {@link Button#update()}..</p>
 *
 * <p>Finally, to determine whether the button has been pushed/bumped, use {@link Button#wasPressed()} or {@link Button#wasReleased()}.</p>
 *
 * <p>See {@link org.firstinspires.ftc.teamcode.TestOpMode} for an example of how Button can be used.</p>
 */
public class Button {
    private Gamepad gamepad;
    private Field field;

    private boolean previous = false;
    private boolean current = false;

    /**
     * <p>Constructs a button that tracks a field on a gamepad.</p>
     *
     * <p>This constructor is rarely used directly.</p>
     *
     * @param gamepad gamepad to track.
     * @param field field of the gamepad to track. Must be a boolean field.
     */
    public Button(Gamepad gamepad, Field field) {
        this.gamepad = gamepad;
        this.field = field;
    }

    /**
     * <p>Makes a button using a gamepad and a button name.</p>
     *
     * <p>This method obtains the field automatically, so most op modes will use this method to create instances of
     * {@link Button}.</p>
     *
     * @param gamepad gamepad to track.
     * @param buttonName name of the button to track. Must be the name of a boolean property of {@link Gamepad}.
     * @throws IllegalArgumentException if the given button name does not exist on the gamepad.
     * @return a gamepad that tracks a button on a gamepad.
     */
    public static Button make(Gamepad gamepad, String buttonName) {
        try {
            return new Button(gamepad, gamepad.getClass().getDeclaredField(buttonName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(buttonName + " is not a button");
        }
    }

    /**
     * <p>Updates the button's state, allowing it to track changes over time.</p>
     *
     * <p>Call this method towards the beginning (or end) of an op mode's {@link OpMode#loop()} method.</p>
     *
     * @throws RuntimeException if the field does not exist on the gamepad. You should only see this if have directly invoked the constructor.
     */
    public void update() {
        previous = current;
        try {
            current = field.getBoolean(gamepad);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Button update failed");
        }
    }

    /**
     * <p>Returns whether the button was released, then pressed.</p>
     *
     * <p>Most op modes use this method, as it allows for more instantaneous feedback.</p>
     *
     * @return true if the button was released, then pressed
     */
    public boolean wasPressed() {
        return current && !previous;
    }

    /**
     * Returns whether the button was pressed, then released.
     * @return true if the button was pressed, then released
     */
    public boolean wasReleased() {
        return previous && !current;
    }
}
