package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import kotlin.math.abs
import kotlin.math.sign

/**
 * Boilerplate linear op mode for autonomous programs.
 */
open class AutoBaseOpMode(
        /** Number of milliseconds to move the motors to drive one inch. **/
        val msPerInch: Double
): LinearOpMode() {
    /**
     * The robot's mecanum drive.
     * @see MecanumDrive
     */
    val drive by lazy { MecanumDrive.standard(hardwareMap) }
    val arm by lazy { Arm.standard(hardwareMap) }
    val standardSensors by lazy { StandardSensors(hardwareMap) }
    val armDistance by lazy { standardSensors.armDistanceSensor }
    val gyro: Gyro by lazy { BNO055IMUGyro.standard(hardwareMap) }

    /**
     * Contains code to run during the op mode. Override this method to implement your own subclass of AutoBaseOpMode.
     *
     * When overriding, ensure that you include a call to `super.runOpMode()`.
     */
    override fun runOpMode() {
        drive
        arm
        armDistance
        gyro.initialize()
        gyro.calibrate()
    }
}

/**
 * Drives the motors at a specified direction for a specified number of inches.
 * @param x horizontal movement.
 * @param y vertical movement.
 * @param inches number of inches to move and wait for.
 */
fun AutoBaseOpMode.drive(x: Double, y: Double, inches: Double) {
    if (inches < 0) {
        drive.move(-1.0, MecanumDrive.Motor.Vector2D(x, y), 0.0)
    } else {
        drive.move(1.0, MecanumDrive.Motor.Vector2D(x, y), 0.0)
    }
    sleep((msPerInch * abs(inches)).toLong())
    drive.stop()
}

/**
 * Overload for the drive extension function.
 *
 * @see drive
 */
fun AutoBaseOpMode.drive(x: Int, y: Int, inches: Int) {
    drive(x.toDouble(), y.toDouble(), inches.toDouble())
}

fun AutoBaseOpMode.raiseArm() {
    arm.setVerticalPower(0.8)
    while (armDistance.getDistance(DistanceUnit.INCH) < 6.5) {
        idle()
    }
    arm.stop()
}

fun AutoBaseOpMode.lowerArm() {
    arm.setVerticalPower(-1.0)
    while (armDistance.getDistance(DistanceUnit.INCH) > 4 && opModeIsActive()) {
        idle()
    }
    arm.stop()
}

fun AutoBaseOpMode.turn(target: Double) {
    if (abs(gyro.angle - target) > 2) {
        drive.steerWithPower(0.2, sign(target - gyro.angle))
        while (abs(target - gyro.angle) > 2) {
            idle()
        }
        drive.stop()
    }
}