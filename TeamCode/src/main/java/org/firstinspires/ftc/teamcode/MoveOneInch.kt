package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive

@Autonomous(name = "Move Three Inches")
class MoveThreeInches: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        while (true) {
            drive(0, 1, 12)
            drive(-1, 0, 12)
            drive(0, -1, 12)
            drive(1, 0, 12)
        }
    }
}