package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.teamcode.auto.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.auto.drive
import org.firstinspires.ftc.teamcode.auto.lowerArm
import org.firstinspires.ftc.teamcode.auto.raiseArm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.*

const val CLAMP_TIME = 1000L

open class AutoFoundationOpMode(
        private val colorModifier: Double,
        private val getSideSensor: StandardSensors.() -> DistanceSensor
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        val clamps = FoundationClamps.standard(hardwareMap)
        val grabber = Grabber.standard(hardwareMap)

        val backDistance = standardSensors.backDistanceSensor
        val sideDistance = standardSensors.run { getSideSensor() }

        waitForStart()

        val startingDir = gyro.angle

        // Move clamps up
        clamps.moveUp()
        grabber.lift()
        raiseArm()

        drive(Vector2D(-colorModifier, 0.0), startingDir, sideDistance, 12.0)

        // Move forward to foundation
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -32.0)
        drive.forwardWithPower(0.27)
        sleep(200L)
        drive.stop()
        clamps.moveDown()
        sleep(CLAMP_TIME)

        drive(Vector2D(0.0, -1.0), startingDir, backDistance, 0.01)
        drive.forwardWithPower(-0.27)
        sleep(800L)
        drive.stop()

        clamps.moveUp()
        sleep(CLAMP_TIME)

        drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, -50.0)
        grabber.grab()
        lowerArm()
        drive(colorModifier, 0.0, 19.0)

        /* drive.forwardWithPower(-1.0)
        sleep(1000L)
        drive.stop() */
    }
}

@Autonomous(name = "Route 2 - Red", group = "Foundation")
class AutoFoundationRedOpMode: AutoFoundationOpMode(-1.0, StandardSensors::getRightDistanceSensor)

@Autonomous(name = "Route 2 - Blue", group = "Foundation")
class AutoFoundationBlueOpMode: AutoFoundationOpMode(1.0, StandardSensors::getLeftDistanceSensor)