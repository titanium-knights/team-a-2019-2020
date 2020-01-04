package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.movement.MecanumDrive

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
    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }

    /**
     * Contains code to run during the op mode. Override this method to implement your own subclass of AutoBaseOpMode.
     *
     * When overriding, ensure that you include a call to `super.runOpMode()`.
     */
    override fun runOpMode() {
        drive
    }
}

/**
 * Drives the motors at a specified direction for a specified number of inches.
 * @param x horizontal movement.
 * @param y vertical movement.
 * @param inches number of inches to move and wait for.
 */
fun AutoBaseOpMode.drive(x: Double, y: Double, inches: Double) {
    drive.move(1.0, MecanumDrive.Motor.Vector2D(x, y), 0.0)
    sleep((msPerInch * inches).toLong())
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