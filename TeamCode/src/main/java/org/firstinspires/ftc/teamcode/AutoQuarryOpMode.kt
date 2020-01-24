package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.*
import kotlin.math.abs
import kotlin.math.sign

open class AutoQuarryOpMode(
        private val colorModifier: Double,
        private val distanceSide: DistanceSide
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    enum class DistanceSide { LEFT, RIGHT }

    private val grabber: Grabber by lazy { Grabber.standard(hardwareMap) }
    private val leftColorSensor: ColorSensor by lazy { standardSensors.leftColorSensor }
    private val rightColorSensor: ColorSensor by lazy { standardSensors.rightColorSensor }
    private val backDistance: DistanceSensor by lazy { standardSensors.backDistanceSensor }
    private val clamps: FoundationClamps by lazy { FoundationClamps.standard(hardwareMap) }
    private val sideDistance: DistanceSensor by lazy { when (distanceSide) {
        DistanceSide.LEFT -> standardSensors.leftDistanceSensor
        DistanceSide.RIGHT -> standardSensors.rightDistanceSensor
    } }
    private val otherSideDistance: DistanceSensor by lazy { when (distanceSide) {
        DistanceSide.LEFT -> standardSensors.rightDistanceSensor
        DistanceSide.RIGHT -> standardSensors.leftDistanceSensor
    } }

    private var skystonePos = 0 // 0 is stone at center

    private val grabberTime = 1000L

    override fun runOpMode() {
        super.runOpMode()

        arrayOf(leftColorSensor, rightColorSensor).forEach { it.enableLed(false) }
        backDistance
        sideDistance

        telemetry["Status"] = "Initialized"
        telemetry.update()

        waitForStart()

        val startingDir = gyro.angle
        telemetry["Starting Direction"] = startingDir
        telemetry.update()

        // Lift grabber and disable color sensor LED
        grabber.lift()
        clamps.moveDown()

        // Move towards the center stone
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -44.0)

        // Move right, checking for the skystone
        val left = leftColorSensor.red().toDouble() / leftColorSensor.green()
        val right = rightColorSensor.red().toDouble() / rightColorSensor.green()

        skystonePos = when {
            abs(left - right) < 0.03 -> 1
            left < right -> 0
            else -> 2
        }

        val inchesToMove = (skystonePos - 1) * 8.0
        drive(Vector2D(sign(inchesToMove) * colorModifier, 0.0), startingDir, sideDistance, -(37.5 - inchesToMove))

        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -50.0)
        grabber.grab()
        sleep(grabberTime)
        raiseArm()

        // Push back
        drive(Vector2D(0.0, -1.0), startingDir, backDistance, 27.0)

        // Cross skybridge
        drive(Vector2D(-colorModifier, 0.0), startingDir, otherSideDistance, 50.0)

        // Release stone
        arm.setVerticalPower(1.0)
        grabber.lift()
        sleep(500L)
        arm.stop()
        grabber.grab()
        drive(Vector2D(colorModifier, 0.0), startingDir, otherSideDistance, -55.0)
        arm.setVerticalPower(-1.0)
        sleep(500L)
        arm.stop()

        // Park
        drive(Vector2D(colorModifier, 0.0), startingDir, otherSideDistance, -63.0)

        /* arm.setHorizontalPower(1.0)
        sleep(200L)
        arm.stop()

        lowerArm()
        turn(startingDir)

        if (skystonePos == 2) {
            skystonePos = 0
        }
        val target = (2 - skystonePos) * 8.0 - 5.0
        drive.move(0.8, MecanumDrive.Motor.Vector2D(colorModifier, 0.0), 0.0)
        while (abs(target - rightDistance.getDistance(DistanceUnit.INCH)) > 15) {
            idle()
        }
        drive.move(0.2, MecanumDrive.Motor.Vector2D(colorModifier, 0.0), 0.0)
        while (abs(target - rightDistance.getDistance(DistanceUnit.INCH)) > 3) {
            idle()
        }
        drive.stop()

        drive.forwardWithPower(0.15)
        while (frontDistance.getDistance(DistanceUnit.INCH) > 4) {
            idle()
        }
        drive(0.0, 0.5, 2.0 * 2)
        grabber.grab()
        sleep(grabberTime)
        raiseArm()
        drive(0.0, -0.5, 3.0 * 2)
        turn(startingDir - 90 * colorModifier)
        drive(0.0, 1.0, 75.0 - target)
        grabber.lift()
        drive(0.0, -1.0, 9.0) */

    }
}

@Autonomous(name = "Route 1 - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(-1.0, DistanceSide.LEFT)

@Autonomous(name = "Route 1 - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(1.0, DistanceSide.RIGHT)