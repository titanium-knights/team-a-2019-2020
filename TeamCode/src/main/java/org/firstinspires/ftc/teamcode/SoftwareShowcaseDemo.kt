package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.auto.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.auto.drive
import org.firstinspires.ftc.teamcode.auto.turn

@Autonomous(name = "Software Showcase Demo")
class SoftwareShowcaseDemo: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        waitForStart()

        val target = gyro.angle + 90

        turn(target)
        drive(0.0, 1.0, 24.0, target)

        requestOpModeStop()
    }
}