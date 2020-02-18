package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.OdometryConfig
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors

@Autonomous(name = "Odometry Op Mode")
class OdometryTestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val gyro = BNO055IMUGyro.standard(hardwareMap)
        val drive = MecanumDrive.standard(hardwareMap)
        val standardSensors = StandardSensors(hardwareMap)
        val dashboard = FtcDashboard.getInstance()

        gyro.initialize()

        waitForStart()

        val startingDir = gyro.angle

        while (opModeIsActive()) {
            if (gamepad1.dpad_down) {
                val odometry = Odometry(drive, arrayOf(DistanceSensorLocalizer(
                        standardSensors.rightDistanceSensor,
                        standardSensors.leftDistanceSensor,
                        standardSensors.backDistanceSensor,
                        gyro
                )), OdometryConfig.x, OdometryConfig.y, OdometryConfig.r, 3.0, 2.0)
                odometry.moveSync(Position(12.0, 12.0, startingDir)) {
                    val packet = TelemetryPacket()
                    packet.fieldOverlay().setFill("blue").fillCircle(it.x, it.y, 10.0)
                    packet.put("X", it.x)
                    packet.put("Y", it.y)
                    packet.put("R", it.rotation)
                    //packet.put("Vx", odometry.xPIDController.evaluate(it.x, 3.0, 1.0))
                    //packet.put("Vy", odometry.yPIDController.evaluate(it.x, 3.0, 1.0))
                    //packet.put("Vr", odometry.rotPIDController.evaluate(it.x, 3.0, 1.0))
                    dashboard.sendTelemetryPacket(packet)
                    idle()
                    opModeIsActive()
                }
            }
        }
    }
}