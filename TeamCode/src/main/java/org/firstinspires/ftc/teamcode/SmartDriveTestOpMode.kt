package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.util.Vector2D
import org.firstinspires.ftc.teamcode.util.drive
import java.lang.RuntimeException

@Autonomous(name = "Smart Drive Test", group = "Tests")
class SmartDriveTestOpMode: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()

        val sensors = StandardSensors(hardwareMap)
        val right = sensors.rightDistanceSensor
        val left = sensors.leftDistanceSensor

        if (right.getDistance(DistanceUnit.INCH) > 1000 && left.getDistance(DistanceUnit.INCH) > 1000) {
            throw RuntimeException("Distance sensors misconfigured")
        }

        val angle = gyro.angle

        waitForStart()

        while (opModeIsActive()) {
            drive(Vector2D(1.0, 0.0), angle, right, 24.0)
            drive(Vector2D(-1.0, 0.0), angle, left, 24.0)
        }
    }
}