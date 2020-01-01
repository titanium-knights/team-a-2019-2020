package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.ColorSensor
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.AutoFieldSide
import org.firstinspires.ftc.teamcode.util.drive
import org.firstinspires.ftc.teamcode.util.set
import java.lang.NullPointerException

const val MILLISECONDS_PER_INCH = 0.0

fun ColorSensor.skystoneDetected(): Boolean {
    return alpha() < 20
}

class AutoQuarryOpMode(
        private val colorModifier: Double,
        private val crossVia: AutoFieldSide,
        private val parkDestination: AutoFieldSide
): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    private val grabber: Grabber by lazy { Grabber.standard(hardwareMap) }
    private val colorSensor: ColorSensor by lazy { StandardSensors(hardwareMap).colorSensor }

    private var skystonePos = 0 // 0 is stone at center

    private val grabberTime = 1000L
    private val inchesToWall = 28

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

        // If we're crossing the skybridge via the wall side, move backwards
        if (crossVia == AutoFieldSide.WALL) {
            drive(0, -1, inchesToWall)
        }
    }
}