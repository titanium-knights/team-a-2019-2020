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

open class AutoQuarryOpMode(
        private val colorModifier: Double
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    private val grabber: Grabber by lazy { Grabber.standard(hardwareMap) }
    private val colorSensor: ColorSensor by lazy { standardSensors.colorSensor }
    private val frontDistance: DistanceSensor by lazy { standardSensors.frontDistanceSensor }
    private val clamps: FoundationClamps by lazy { FoundationClamps.standard(hardwareMap) }
    private val rightDistance: DistanceSensor by lazy { standardSensors.rightDistanceSensor }

    private var skystonePos = 0 // 0 is stone at center

    private val grabberTime = 1000L

    override fun runOpMode() {
        super.runOpMode()

        if (colorSensor.argb() == 0) {
            telemetry["Warning"] = "Color sensor is returning 0. It is likely not working properly."
        }

        colorSensor
        frontDistance
        rightDistance

        telemetry["Status"] = "Initialized"
        telemetry.update()

        waitForStart()

        val startingDir = gyro.angle
        telemetry["Starting Direction"] = startingDir
        telemetry.update()

        // Lift grabber and disable color sensor LED
        grabber.lift()
        clamps.moveDown()
        colorSensor.enableLed(false)

        // Move towards the third stone from center
        val voltage = hardwareMap.voltageSensor.map { it.voltage }.minBy { if (it < 0) Double.MAX_VALUE else it } ?: Double.MAX_VALUE
        val maxPower = if (voltage < 13.5) 1.0 else 0.85
        drive.forwardWithPower(0.7)
        while (frontDistance.getDistance(DistanceUnit.INCH) > 15) {
            idle()
        }
        drive.forwardWithPower(0.15)
        while (frontDistance.getDistance(DistanceUnit.INCH) > 4) {
            idle()
        }
        drive.stop()

        // Move 1 inch away from the edge of the stone away from the center
        drive.move(1.0, MecanumDrive.Motor.Vector2D(colorModifier * 0.5, 0.0), 0.0)
        while (rightDistance.getDistance(DistanceUnit.INCH) > 24) {
            idle()
        }
        drive.move(1.0, MecanumDrive.Motor.Vector2D(colorModifier * 0.15, 0.0), 0.0)
        while (rightDistance.getDistance(DistanceUnit.INCH) > 16) {
            idle()
        }
        drive.stop()

        // For each stone, see if it's the skystone, then move right and check the next one
        var minLuminosity = Double.MAX_VALUE
        var lumLog = ""
        for (pos in 2 downTo 0) {
            sleep(300L)

            val luminosity = colorSensor.red().toDouble() / colorSensor.green()
            if (luminosity < minLuminosity) {
                skystonePos = pos
                minLuminosity = luminosity
            }

            lumLog += "$luminosity  "

            telemetry["Starting Direction"] = startingDir
            telemetry["Skystone Position"] = skystonePos
            telemetry["Luminosity"] = lumLog
            telemetry["Luminosity (min)"] = minLuminosity
            telemetry["Arm Pos"] = arm.verticalMotor.currentPosition
            telemetry.update()

            if (pos > 0) {
                drive(-colorModifier / 2, 0.0, 8.0 * 2 / 8 * 6.5)
                turn(startingDir)
            }
        }

        // Grab stone and wait for grabber to finish
        val inchesToMove = skystonePos * 8.0 - 4
        drive(colorModifier, 0.0, inchesToMove)

        drive(0.0, 0.5, 2.0 * 2)
        grabber.grab()
        sleep(grabberTime)

        raiseArm()

        // Push back
        drive(0.0, -0.5, 3.0 * 2)

        telemetry["Starting Direction"] = startingDir
        telemetry["Current Angle"] = gyro.angle
        telemetry["Skystone Position"] = skystonePos
        telemetry["Luminosity"] = lumLog
        telemetry["Luminosity (min)"] = minLuminosity
        telemetry.update()

        // Correct for any drift
        turn(startingDir)

        // Cross skybridge
        drive(-colorModifier, 0.0, skystonePos * 8.0 + 54.0)

        // Release stone
        arm.setVerticalPower(1.0)
        grabber.lift()
        sleep(500L)
        arm.stop()
        grabber.grab()
        drive(colorModifier, 0.0, 4.0)
        arm.setVerticalPower(-1.0)
        sleep(500L)
        arm.stop()

        turn(startingDir)

        // Park
        drive(colorModifier, 0.0, 14.0)

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

@Autonomous(name = "Quarry - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(1.0)

@Autonomous(name = "Quarry - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(-1.0)