package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.Vector2D
import org.firstinspires.ftc.teamcode.util.set
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
    val pidController = PIDController(1.0 / 30, 0.0, 0.0)
    val elapsedTime by lazy { ElapsedTime() }

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
        elapsedTime
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
        drive.move(-0.5, MecanumDrive.Motor.Vector2D(x, y), 0.0)
    } else {
        drive.move(0.5, MecanumDrive.Motor.Vector2D(x, y), 0.0)
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

fun AutoBaseOpMode.drive(x: Double, y: Double, inches: Double, targetAngle: Double) {
    val start = elapsedTime.milliseconds()
    var previous = start
    val time = msPerInch * abs(inches)

    pidController.reset()

    sleep(10L)

    do {
        val currentAngle = gyro.angle
        val now = elapsedTime.milliseconds()
        val turn = pidController.evaluate(currentAngle, targetAngle, now - previous)
        previous = now

        drive.move(0.5, MecanumDrive.Motor.Vector2D(x, y), turn / 2, MecanumDrive.TurnBehavior.ADDSUBTRACT)

        telemetry.update()

        sleep(8L)
    } while ((now - start) < time && opModeIsActive() && !isStopRequested)

    drive.stop()
}

fun AutoBaseOpMode.raiseArm() {
    arm.setVerticalPower(0.8)
    while (armDistance.getDistance(DistanceUnit.INCH) < 6.3) {
        idle()
    }
    arm.stop()
}

fun AutoBaseOpMode.lowerArm(thing: Boolean = false) {
    arm.setVerticalPower(-1.0)
    while (armDistance.getDistance(DistanceUnit.INCH) > 4.2 && (opModeIsActive() || thing)) {
        idle()
    }
    arm.stop()
}

fun AutoBaseOpMode.turn(target: Double, speed: Double = 0.3) {
    if (abs(gyro.angle - target) > 2) {
        drive.steerWithPower(speed, sign(target - gyro.angle))
        while (abs(target - gyro.angle) > 2) {
            idle()
        }
        drive.stop()
    }
}

fun AutoBaseOpMode.drive(vector: Vector2D, targetAngle: Double, sensor: DistanceSensor, inches: Double, stop: Boolean = true, onLoop: (Double) -> Unit = {}) {
    var previous = elapsedTime.milliseconds()

    pidController.reset()

    var prevDistance: Double
    var distance = sensor.getDistance(DistanceUnit.INCH) * sign(inches)
    sleep(10L)

    do {
        prevDistance = distance
        distance = sensor.getDistance(DistanceUnit.INCH) * sign(inches)
        val avgDistance = (prevDistance + distance) / 2

        val power = if (stop) ((avgDistance - inches) / 18).coerceIn(0.5..1.0) else 1.0

        val currentAngle = gyro.angle
        val now = elapsedTime.milliseconds()
        val turn = pidController.evaluate(currentAngle, targetAngle, now - previous)
        previous = now

        drive.move(power / 2, vector, turn / 2, MecanumDrive.TurnBehavior.ADDSUBTRACT)
        onLoop(distance)

        telemetry["Distance"] = distance
        telemetry.update()

        sleep(8L)
    } while ((prevDistance + distance) / 2 - inches >= 5 && opModeIsActive() && !isStopRequested)

    if (stop) {
        drive.stop()
    }
}