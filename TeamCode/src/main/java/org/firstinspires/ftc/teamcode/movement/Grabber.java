package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Grabber {
    private Servo servo;
    private Servo servoMirror;

    public Grabber(Servo servo, Servo servoMirror) {
        this.servo = servo;
        this.servoMirror = servoMirror;
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public int getPortNumber() {
        return servo.getPortNumber();
    }

    public int getMirrorPortNumber() {
        return servoMirror.getPortNumber();
    }

    public void setPosition(double position) {
        servo.setPosition(position);
        servoMirror.setPosition(1 - position);
    }

    public void move(double delta) {
        setPosition(Range.clip(getPosition() + delta, 0, 1));
    }

    public static final double LIFT_POSITION = 0;
    public static final double GRAB_POSITION = 1;

    public void lift() {
        setPosition(LIFT_POSITION);
    }

    public void grab() {
        setPosition(GRAB_POSITION);
    }

    private static String GRABBER_SERVO_NAME = "grabber1";
    private static String GRABBER_SERVO_MIRROR_NAME = "grabber2";

    public static Grabber standard(HardwareMap hardwareMap) {
        return new Grabber(
                hardwareMap.get(Servo.class, GRABBER_SERVO_NAME),
                hardwareMap.get(Servo.class, GRABBER_SERVO_MIRROR_NAME));
    }
}
