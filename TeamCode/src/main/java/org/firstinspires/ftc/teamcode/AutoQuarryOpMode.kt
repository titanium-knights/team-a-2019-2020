package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.ColorSensor
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive
import org.firstinspires.ftc.teamcode.util.set

fun ColorSensor.skystoneDetected(): Boolean {
    return alpha() < 10
}

open class AutoQuarryOpMode(
        private val colorModifier: Double
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    private val grabber: Grabber by lazy { Grabber.standard(hardwareMap) }
    private val colorSensor: ColorSensor by lazy { StandardSensors(hardwareMap).colorSensor }

    private var skystonePos = 0 // 0 is stone at center

    private val grabberTime = 1000L

    override fun runOpMode() {
        if (colorSensor.argb() == 0) {
            telemetry["Warning"] = "Color sensor is returning 0. It is likely not working properly."
        }

        telemetry["Status"] = "Initialized"
        telemetry.update()

        waitForStart()

        // Lift grabber and disable color sensor LED
        grabber.lift()
        colorSensor.enableLed(false)

        // Move towards the third stone from center
        drive(0, 1, 29)

        // Move 1 inch away from the edge of the stone away from the center
        drive(colorModifier, 0.0, 6.0)

        // For each stone, see if it's the skystone, then move right and check the next one
        for (pos in 2 downTo 1) {
            if (colorSensor.skystoneDetected()) {
                skystonePos = pos
                break
            }

            drive(-colorModifier, 0.0, 24.0 / 3)
        }

        // Grab stone and wait for grabber to finish
        grabber.grab()
        sleep(grabberTime)

        // Push back
        drive(0.0, -1.0, 2.0)

        // Cross skybridge
        drive(-colorModifier, 0.0, skystonePos * 6.0 + 36.0)

        // Release stone
        grabber.lift()
        sleep(grabberTime)

        // Park
        drive(colorModifier, 0.0, 12.0)
    }
}

@Autonomous(name = "Quarry - Red", group = "Quarry")
class AutoQuarryRedOpMode: AutoQuarryOpMode(1.0)

@Autonomous(name = "Quarry - Blue", group = "Quarry")
class AutoQuarryBlueOpMode: AutoQuarryOpMode(-1.0)