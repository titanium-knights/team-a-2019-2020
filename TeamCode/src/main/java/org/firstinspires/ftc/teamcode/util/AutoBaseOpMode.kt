package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.movement.MecanumDrive

open class AutoBaseOpMode(val msPerInch: Double): LinearOpMode() {
    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }

    override fun runOpMode() {
        drive
    }
}

fun AutoBaseOpMode.drive(x: Double, y: Double, inches: Double) {
    drive.move(1.0, MecanumDrive.Motor.Vector2D(x, y), 0.0)
    sleep((msPerInch * inches).toLong())
    drive.stop()
}

fun AutoBaseOpMode.drive(x: Int, y: Int, inches: Int) {
    drive(x.toDouble(), y.toDouble(), inches.toDouble())
}