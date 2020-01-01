package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.movement.MecanumDrive

@Autonomous(name = "Move for 5 Seconds", group = "Test")
class MoveFor5Seconds: LinearOpMode() {
    override fun runOpMode() {
        val drive = MecanumDrive.standard(hardwareMap)
        waitForStart()

        drive.forwardWithPower(1.0)
        sleep(5000L)
        drive.stop()

        requestOpModeStop()
    }
}
