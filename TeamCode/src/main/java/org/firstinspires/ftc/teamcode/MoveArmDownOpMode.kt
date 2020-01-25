package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.sensors.StandardSensors

@Autonomous(name = "Move Arm Down", group = "Maintenance")
class MoveArmDownOpMode: LinearOpMode() {
    override fun runOpMode() {
        val arm = Arm.standard(hardwareMap)
        val armDistance = StandardSensors(hardwareMap).armDistanceSensor

        waitForStart()

        arm.setVerticalPower(-1.0)
        while (armDistance.getDistance(DistanceUnit.INCH) > 4.2 && opModeIsActive()) {
            idle()
        }
        arm.stop()

        requestOpModeStop()
    }
}