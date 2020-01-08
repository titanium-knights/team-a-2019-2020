package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.ColorSensor
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive
import org.firstinspires.ftc.teamcode.util.set
import kotlin.math.abs

open class AutoQuarryOpMode(
        private val colorModifier: Double
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    private val arm: Arm by lazy { Arm.standard(hardwareMap) }
    private val grabber: Grabber by lazy { Grabber.standard(hardwareMap) }
    private val colorSensor: ColorSensor by lazy { StandardSensors(hardwareMap).colorSensor }
    private val gyro: Gyro by lazy { BNO055IMUGyro.standard(hardwareMap) }

    private var skystonePos = 0 // 0 is stone at center

    private val grabberTime = 1000L

    override fun runOpMode() {
        if (colorSensor.argb() == 0) {
            telemetry["Warning"] = "Color sensor is returning 0. It is likely not working properly."
        }

        gyro.initialize()
        gyro.calibrate()

        telemetry["Status"] = "Initialized"
        telemetry.update()

        waitForStart()

        val startingDir = gyro.angle
        telemetry["Starting Direction"] = startingDir
        telemetry.update()

        // Lift grabber and disable color sensor LED
        grabber.lift()
        colorSensor.enableLed(false)

        // Move towards the third stone from center
        drive(0.0, 1.0, 24.0)
        sleep(500L)

        // Move 1 inch away from the edge of the stone away from the center
        drive(colorModifier / 2, 0.0, 14.0 * 2)

        // For each stone, see if it's the skystone, then move right and check the next one
        var minLuminosity = Int.MAX_VALUE
        var lumLog = ""
        for (pos in 2 downTo 0) {
            sleep(4000L)

            val luminosity = colorSensor.alpha()
            if (luminosity < minLuminosity) {
                skystonePos = pos
                minLuminosity = luminosity
            }

            lumLog += "$luminosity  "

            telemetry["Starting Direction"] = startingDir
            telemetry["Skystone Position"] = skystonePos
            telemetry["Luminosity"] = lumLog
            telemetry["Luminosity (min)"] = minLuminosity
            telemetry.update()

            if (pos > 0) {
                drive(-colorModifier / 2, 0.0, 8.0 * 2)
            }
        }

        // Grab stone and wait for grabber to finish
        val inchesToMove = skystonePos * 8.0 - 1.5
        drive(colorModifier, 0.0, inchesToMove)

        drive(0.0, 0.5, 4.0 * 2)
        grabber.grab()
        sleep(grabberTime)

        arm.setVerticalPower(1.0)
        sleep(750L)
        arm.stop()

        // Push back
        drive(0.0, -0.5, 4.0 * 2)

        telemetry["Starting Direction"] = startingDir
        telemetry["Current Angle"] = gyro.angle
        telemetry["Skystone Position"] = skystonePos
        telemetry["Luminosity"] = lumLog
        telemetry["Luminosity (min)"] = minLuminosity
        telemetry.update()

        // Correct for any drift
        val postGrabDir = gyro.angle
        if (abs(postGrabDir - startingDir) > 2) {
            if (postGrabDir > startingDir) {
                drive.steerWithPower(0.2, -1.0)
                while (gyro.angle - startingDir > 2) {
                    idle()
                }
            } else {
                drive.steerWithPower(0.2, 1.0)
                while (startingDir - gyro.angle > 2) {
                    idle()
                }
            }

            drive.stop()
        }

        // Cross skybridge
        drive(-colorModifier, 0.0, skystonePos * 8.0 + 48.0)

        // Release stone
        grabber.lift()
        sleep(grabberTime)
        grabber.grab()
        sleep(grabberTime)

        // Park
        drive(colorModifier, 0.0, 12.0)

        arm.setVerticalPower(-1.0)
        sleep(750L)
        arm.stop()

        while (opModeIsActive()) {
            telemetry["Starting Direction"] = startingDir
            telemetry["Current Angle"] = gyro.angle
            telemetry["Skystone Position"] = skystonePos
            telemetry["Luminosity"] = lumLog
            telemetry["Luminosity (min)"] = minLuminosity
            telemetry.update()

            idle()
        }
    }
}

@Autonomous(name = "Quarry - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(1.0)

@Autonomous(name = "Quarry - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(-1.0)