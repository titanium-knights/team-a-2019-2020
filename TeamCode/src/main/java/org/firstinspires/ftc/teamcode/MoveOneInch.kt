package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.drive

@Autonomous(name = "Move One Inch")
class MoveOneInch: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        drive(0, 1, 1)
        drive(-1, 0, 1)
        drive(0, -1, 1)
        drive(1, 0, 1)
    }
}