package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive
import org.firstinspires.ftc.teamcode.util.lowerArm
import org.firstinspires.ftc.teamcode.util.raiseArm

const val CLAMP_TIME = 1000L

class AutoFoundationOpMode(private val colorModifier: Double): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        val clamps = FoundationClamps.standard(hardwareMap)

        waitForStart()

        clamps.moveUp()
        raiseArm()

        // Move left one tile
        drive(-colorModifier, 0.0, 24.0)

        // Move forward 1.25 tiles
        drive(0, 1, 30)

        // Move clamps down
        clamps.moveDown()
        sleep(CLAMP_TIME)

        // Move backwards 1.5 tiles
        drive(0, -1, 36)

        // Move clamps up
        clamps.moveUp()

        // Move right 36 inches
        drive(colorModifier, 1.0, 36.0)

        // Lower arm
        lowerArm()

        // Move right 24 inches
        drive(colorModifier, 1.0, 24.0)
    }
}

@Autonomous(name = "Foundation - Red", group = "Foundation")
class AutoFoundationRedOpMode: AutoQuarryOpMode(1.0)

@Autonomous(name = "Foundation - Blue", group = "Foundation")
class AutoFoundationBlueOpMode: AutoQuarryOpMode(-1.0)