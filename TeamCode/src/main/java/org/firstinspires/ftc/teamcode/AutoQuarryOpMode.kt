package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.teamcode.auto.*
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
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

    private var skystonePos = 0 // 0 is stone at wall

    private val grabberTime = 1000L

    override fun runOpMode() {
        super.runOpMode()

        arrayOf(leftColorSensor, rightColorSensor).forEach { it.enableLed(false) }
        backDistance
        sideDistance

        telemetry["Status"] = "Initialized"
        telemetry.update()

        // lowerArm(true)
        waitForStart()

        if (isStopRequested) {
            return;
        }

        val startingDir = gyro.angle
        telemetry["Starting Direction"] = startingDir
        telemetry.update()

        // Lift grabber and disable color sensor LED
        grabber.lift()
        clamps.moveDown()

        // Move towards the center stone
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -AutoQuarryConfig.approachDist)

        sleep(AutoQuarryConfig.approachDelay.toLong())

        // Move right, checking for the skystone
        val left = leftColorSensor.red().toDouble() / leftColorSensor.green()
        val right = rightColorSensor.red().toDouble() / rightColorSensor.green()

        skystonePos = when {
            abs(left - right) < AutoQuarryConfig.colorThres -> 1
            left < right -> 2
            else -> 0
        }

        if (colorModifier < 0) {
            skystonePos = 2 - skystonePos
        }

        when (skystonePos) {
            2 -> drive(Vector2D(-colorModifier, 0.0), startingDir, sideDistance, -44.0)
            0 -> drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, 20.0)
        }

        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -35.0)
        grabber.grab()
        sleep(grabberTime)
        raiseArm()

        val transitInches = AutoQuarryConfig.transitInches - skystonePos * AutoQuarryConfig.transitMul
        drive(Vector2D(0.0, -1.0), startingDir, backDistance, if (colorModifier > 0) transitInches else 23.0)

        drive(-colorModifier * 4.0, 0.0, 48.0, startingDir)
        if (colorModifier > 0) {
            drive(Vector2D(0.0, -1.0), startingDir, backDistance, transitInches)
        }
        clamps.moveUp()
        drive(Vector2D(-colorModifier, 0.0), startingDir, otherSideDistance, 20.0)
        arm.setVerticalPower(1.0)
        sleep(150L)
        arm.stop()

        drive(0.0, AutoQuarryConfig.bumpPower, AutoQuarryConfig.bumpDist)
        clamps.moveDown()
        grabber.lift()
        sleep(AutoQuarryConfig.moveHorizontalTime.toLong())
        drive(0.0, -3.0, 16.0, startingDir)

        val newAngle = startingDir - colorModifier * 90
        turn(newAngle, AutoQuarryConfig.turnFastSpeed)
        drive(0.0, 1.0, 10.0)
        clamps.moveUp()

        if (colorModifier > 0) {
            drive(-1.0, 0.0, 3.0, newAngle)
        }

        drive(0.0, -1.0, 12.0, newAngle)
        clamps.moveDown()
        arm.setHorizontalPower(1.0)
        sleep(300L)
        arm.stop()
        lowerArm()
        drive(0.0, -3.0, 30.0, newAngle)
        turn(startingDir)

        drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, if (skystonePos == 0) 0.000001 else (skystonePos * 8.0 - AutoQuarryConfig.adjMid - colorModifier * AutoQuarryConfig.adjMul))
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -35.0)
        grabber.grab()
        sleep(grabberTime)
        raiseArm()
        drive(Vector2D(0.0, -1.0), startingDir, backDistance, 28.0)
        turn(newAngle + colorModifier * 10)
        drive(0.0, 3.0, 28.0 + (2 - skystonePos) * 8.0, newAngle)
        clamps.moveUp()
        arm.setVerticalPower(1.0)
        sleep(150L)
        arm.stop()
        drive(0.0, 3.0, 25.0, newAngle)
        grabber.lift()
        sleep(grabberTime)
        drive(0.0, -3.0, 10.0, newAngle)
        grabber.grab()
        arm.setVerticalPower(-1.0)
        sleep(150L)
        arm.stop()
        drive(0.0, -3.0, 10.0, newAngle)
    }

    @Config object AutoQuarryConfig {
        @JvmField var moveHorizontalTime = 300
        @JvmField var turnFastSpeed = 0.7
        @JvmField var transitInches = 27.0
        @JvmField var approachDist = 20.0
        @JvmField var adjMid = 6.0
        @JvmField var adjMul = -0.0
        @JvmField var bumpDist = 28.0
        @JvmField var bumpPower = 0.5
        @JvmField var colorThres = 0.035
        @JvmField var approachDelay = 500
        @JvmField var transitMul = 0.5
    }
}

@Autonomous(name = "Route 1 - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(-1.0, DistanceSide.LEFT)

@Autonomous(name = "Route 1 - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(1.0, DistanceSide.RIGHT)