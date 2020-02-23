package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.auto.AutoBaseOpMode
import org.firstinspires.ftc.teamcode.auto.raiseArm
import org.firstinspires.ftc.teamcode.auto.turn
import org.firstinspires.ftc.teamcode.auto.drive
import org.firstinspires.ftc.teamcode.auto.lowerArm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.util.*

@Autonomous(name = "Stuff")
class Stuff: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    override fun runOpMode() {
        super.runOpMode()
        val angle = gyro.angle
        val clamps = FoundationClamps.standard(hardwareMap)
        val grabber = Grabber.standard(hardwareMap)
        waitForStart()

        telemetry.addData("Dist", standardSensors.backDistanceSensor.getDistance(DistanceUnit.INCH))
        telemetry.update()

        raiseArm()
        clamps.moveDown()
        grabber.lift()
        sleep(400L)
        turn(angle + 90, 0.9)

        drive(0.0, 1.0, 10.0)
        clamps.moveUp()
        arm.setHorizontalPower(1.0)
        sleep(70L)
        arm.stop()
        drive(MecanumDrive.Motor.Vector2D(1.0, 0.0), angle + 90, standardSensors.rightDistanceSensor, 24.0)

        drive(0.0, -1.0, 12.0, angle + 90)
        lowerArm()
        drive(0.0, -3.0, 30.0, angle + 90)
        drive(MecanumDrive.Motor.Vector2D(1.0, 0.0), angle + 90, standardSensors.rightDistanceSensor, 24.0)
        turn(angle)
    }
}