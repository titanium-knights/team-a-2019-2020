package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.BuildSiteClamps
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive

class AutoFoundationOpMode(private val colorModifier: Double): AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        val arm = Arm.standard(hardwareMap)
        val clamps = BuildSiteClamps.standard(hardwareMap)

        waitForStart()

        // Move clamps up
        // (clamps.move)(1.0, 500L)

        // Move arm up
        arm.setVerticalPower(1.0)
        sleep(1000L)
        arm.stop()

        // Move forward one tile
        drive(0, 1, 24)

        // Move right one tile
        drive(-colorModifier, 0.0, 24.0)

        // Move forward 26 inches
        drive(0, 1, 26)

        // Move clamps down
        (clamps.move)(-1.0, 500L)

        // Move backwards 50 inches
        drive(0, -1, 50)

        // Move left 36 inches
        drive(colorModifier, 1.0, 36.0)

        // Move forward 1 inch
        drive(0, 1, 1)

        // Move arm down
        arm.setVerticalPower(-1.0)
        sleep(1000L)
        arm.stop()

        // Move left 36 inches
        drive(colorModifier, 1.0, 36.0)
    }
}

@Autonomous(name = "Foundation - Red", group = "Foundation")
class AutoFoundationRedOpMode: AutoQuarryOpMode(1.0)

@Autonomous(name = "Foundation - Blue", group = "Foundation")
class AutoFoundationBlueOpMode: AutoQuarryOpMode(-1.0)