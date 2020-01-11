package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.util.*
import kotlin.math.abs
import kotlin.math.sign

const val CLAMP_TIME = 1000L

open class AutoFoundationOpMode(private val colorModifier: Double): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        val clamps = FoundationClamps.standard(hardwareMap)
        val grabber = Grabber.standard(hardwareMap)

        waitForStart()

        val startingDir = gyro.angle

        // Move clamps up
        clamps.moveUp()
        grabber.lift()

        // Move left one tile
        drive(-colorModifier, 0.0, 24.0)

        // Move forward 1.25 tiles
        drive(0, 1, 33)

        // Move clamps down
        clamps.moveDown()
        sleep(CLAMP_TIME)

        // Move backwards 1.5 tiles
        drive(0, -1, 37)

        // Move clamps up
        clamps.moveUp()
        sleep(CLAMP_TIME)

        turn(startingDir)

        drive(0.0, 0.5, 2.0)

        // Move right 48 inches
        drive(colorModifier, 0.0, 48.0)

        // Move right 30 inches
        drive(colorModifier, 0.0, 24.0)
    }
}

@Autonomous(name = "Route 2 - Red", group = "Foundation")
class AutoFoundationRedOpMode: AutoFoundationOpMode(-1.0)

@Autonomous(name = "Route 2 - Blue", group = "Foundation")
class AutoFoundationBlueOpMode: AutoFoundationOpMode(1.0)