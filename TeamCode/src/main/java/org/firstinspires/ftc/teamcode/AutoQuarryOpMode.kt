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

    private var skystonePos = 0 // 0 is stone at wall

    private val grabberTime = 1000L

    fun collect(startingDir: Double) {
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -35.0)
        grabber.grab()
        sleep(grabberTime)
        raiseArm()

        // Push back
        drive(Vector2D(0.0, -1.0), startingDir, backDistance, 24.6)

        // Cross skybridge
        drive(Vector2D(-colorModifier, 0.0), startingDir, sideDistance, -50.0, false)
        drive(Vector2D(-colorModifier, 0.0), startingDir, otherSideDistance, 50.0)

        // Release stone
        arm.setVerticalPower(1.0)
        grabber.lift()
        sleep(700L)
        arm.stop()

        drive(colorModifier * 0.5, 0.0, 15.0)
        arm.setVerticalPower(-1.0)
        sleep(700L)
        arm.stop()
    }

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
        drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, 30.0)

        // Move towards the center stone
        drive(Vector2D(0.0, 1.0), startingDir, backDistance, -31.0)

        sleep(200L)

        // Move right, checking for the skystone
        val left = leftColorSensor.red().toDouble() / leftColorSensor.green()
        val right = rightColorSensor.red().toDouble() / rightColorSensor.green()

        skystonePos = when {
            abs(left - right) < 0.04 -> 1
            left < right -> 2
            else -> 0
        }

        skystonePos = sign(colorModifier).toInt() * (skystonePos - 1) + 1

        when (skystonePos) {
            2 -> drive(Vector2D(-colorModifier, 0.0), startingDir, sideDistance, -40.0)
            0 -> drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, 21.0)
        }

        collect(startingDir)

        arm.setPowers(0.5, 0.0)
        sleep(200L)
        arm.setHorizontalPower(0.0)
        lowerArm()

        drive(Vector2D(colorModifier, 0.0), startingDir, sideDistance, (if (skystonePos == 0) 5 else skystonePos) * 8.0 - 1.5)

        collect(startingDir)
        drive(colorModifier * 0.5, 0.0, 15.0)
    }
}

@Autonomous(name = "Route 1 - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(-1.0, DistanceSide.LEFT)

@Autonomous(name = "Route 1 - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(1.0, DistanceSide.RIGHT)