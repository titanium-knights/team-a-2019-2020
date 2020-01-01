package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * <p>Helper class representing our robot's grabber, which clamps and grips a stone as it is lifted.</p>
 *
 * <p>Together with the {@link Arm}, this comprises our robot's intake and outtake. This class uses two {@link Servo}s to
 * control and represent the grabber.</p>
 */
public class Grabber {
    private Servo servo;
    private Servo servoMirror;

    /**
     * Constructs a new Grabber object with the specified motors.
     * @param servo first servo on the grabber.
     * @param servoMirror second servo on the grabber, which mirrors the actions of the first servo to provide increased power.
     */
    public Grabber(Servo servo, Servo servoMirror) {
        this.servo = servo;
        this.servoMirror = servoMirror;
    }

    /**
     * Returns the first servo on the grabber.
     * @return the first servo on the grabber.
     */
    public Servo getServo() {
        return servo;
    }

    /**
     * Returns the second servo on the grabber, which mirrors the actions of the first servo to provide increased power.
     * @return the second servo on the grabber.
     */
    public Servo getServoMirror() {
        return servoMirror;
    }

    /**
     * Returns the current position of the grabber.
     * @return the current position of the grabber, expressed in the interval [0.0, 1.0].
     */
    public double getPosition() {
        return servo.getPosition();
    }

    /**
     * Sets the position of the grabber.
     * @param position the new position of the grabber, expressed in the interval [0.0, 1.0].
     */
    public void setPosition(double position) {
        servo.setPosition(position);
        servoMirror.setPosition(1 - position);
    }

    /**
     * Moves the grabber relative to its current position.
     * @param delta how much to move the grabber by, expressed in the interval [0.0, 1.0], where 1 represents the full range of the servo's rotation.
     */
    public void move(double delta) {
        setPosition(Range.clip(getPosition() + delta, 0, 1));
    }

    /**
     * Target position for lifting the grabber and depositing a stone.
     */
    public static final double LIFT_POSITION = 0;

    /**
     * Target position for grabbing a stone.
     */
    public static final double GRAB_POSITION = 1;

    /**
     * Lifts the grabber and deposits a stone by setting the position to {@link Grabber#LIFT_POSITION}.
     */
    public void lift() {
        setPosition(LIFT_POSITION);
    }

    /**
     * Grabs the stone by setting the position to {@link Grabber#GRAB_POSITION}.
     */
    public void grab() {
        setPosition(GRAB_POSITION);
    }

    private static String GRABBER_SERVO_NAME = "grabber1";
    private static String GRABBER_SERVO_MIRROR_NAME = "grabber2";

    /**
     * Returns a Grabber object preconfigured for our robot's motors.
     * @param hardwareMap hardware map of the robot.
     * @return a Grabber object preconfigured for our robot.
     */
    public static Grabber standard(HardwareMap hardwareMap) {
        return new Grabber(
                hardwareMap.get(Servo.class, GRABBER_SERVO_NAME),
                hardwareMap.get(Servo.class, GRABBER_SERVO_MIRROR_NAME));
    }
}
