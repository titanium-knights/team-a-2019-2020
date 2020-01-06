package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * <p>Helper class representing the robot's arm.</p>
 *
 * <p>Our robot's arm is composed of a four-bar lift attached to a horizontally rotating motor, meaning it can move
 * horizontally and vertically simultaneously. This arm is attached to a {@link Grabber} and comprises the first segment
 * of our intake and outtake, used to collect and deposit stones.</p>
 *
 * <p>Internally, both axes of motion are controlled and represented by {@link DcMotor}. Access these using
 * {@link Arm#getHorizontalMotor()} and {@link Arm#getVerticalMotor()}.</p>
 */
public class Arm {
    private DcMotor horizontalMotor;
    private DcMotor verticalMotor;

    /**
     * Multiplier for all powers sent to the horizontal rotation motor.
     *
     * @see #setHorizontalPower(double)
     */
    public double horizontalMultiplier;

    /**
     * Multiplier for all powers sent to the vertical rotation motor.
     *
     * @see #setVerticalPower(double)
     */
    public double verticalMultiplier;

    /**
     * Constructs a new arm object with a specified horizontal motor, vertical motor, horizontal multiplier, and
     * vertical multiplier.
     *
     * @param horizontal motor that rotates the arm horizontally.
     * @param vertical motor that rotates the arm vertically (four-bar lift).
     * @param horizontalMultiplier multiplier for all powers sent to the horizontal rotation motor.
     * @param verticalMultiplier multiplier for all powers sent to the vertical rotation motor.
     */
    public Arm(DcMotor horizontal, DcMotor vertical,
               double horizontalMultiplier, double verticalMultiplier) {
        horizontalMotor = horizontal;
        verticalMotor = vertical;

        this.horizontalMultiplier = horizontalMultiplier;
        this.verticalMultiplier = verticalMultiplier;
    }

    /**
     * Returns the motor that rotates the arm horizontally.
     * @return the motor that rotates the arm horizontally.
     */
    public DcMotor getHorizontalMotor() {
        return horizontalMotor;
    }

    /**
     * Returns the motor that rotates the arm vertically.
     * @return the motor that rotates the arm vertically.
     */
    public DcMotor getVerticalMotor() {
        return verticalMotor;
    }

    /**
     * Begins rotating the arm horizontally.
     * @param power power at which to rotate the arm, which will be multiplied by {@link Arm#horizontalMultiplier}.
     */
    public void setHorizontalPower(double power) {
        horizontalMotor.setPower(power * horizontalMultiplier);
    }

    /**
     * Begins rotating the arm vertically.
     * @param power power at which to rotate the arm, which will be multiplied by {@link Arm#verticalMultiplier}.
     */
    public void setVerticalPower(double power) {
        verticalMotor.setPower(power * verticalMultiplier);
    }

    /**
     * Begins rotating the arm horizontally and vertically.
     * @param horizontalPower power at which to rotate horizontally, which will be multiplied by {@link Arm#horizontalMultiplier}.
     * @param verticalPower power at which to rotate vertically, which will be multiplied by {@link Arm#verticalMultiplier}.
     */
    public void setPowers(double horizontalPower, double verticalPower) {
        setHorizontalPower(horizontalPower);
        setVerticalPower(verticalPower);
    }

    /**
     * Stops the arm by setting both motor powers to 0.
     */
    public void stop() {
        setPowers(0, 0);
    }

    private static String ARM_HORIZONTAL_NAME = "arm_horizontal";
    private static String ARM_VERTICAL_NAME = "arm_vertical";

    private static double ARM_HORIZONTAL_MULTIPLIER = 1;
    private static double ARM_VERTICAL_MULTIPLIER = -1;

    /**
     * <p>Returns an Arm object preconfigured for our robot.</p>
     *
     * <p>Specifically, it:</p>
     * <ul>
     *     <li>Retrieves and assigns the correct motors</li>
     *     <li>Sets the {@link Arm#horizontalMultiplier} to -1, such that {@link Arm#setHorizontalPower(double)} moves the arm right when passed a positive power</li>
     *     <li>Sets the {@link Arm#verticalMultiplier} to 1, such that {@link Arm#setVerticalPower(double)} moves the arm up when passed a positive power</li>
     * </ul>
     *
     * @param hardwareMap hardware map of the robot.
     * @return an Arm object preconfigured for our robot.
     */
    public static Arm standard(HardwareMap hardwareMap) {
        return new Arm(
                hardwareMap.get(DcMotor.class, ARM_HORIZONTAL_NAME),
                hardwareMap.get(DcMotor.class, ARM_VERTICAL_NAME),
                ARM_HORIZONTAL_MULTIPLIER,
                ARM_VERTICAL_MULTIPLIER);
    }
}
