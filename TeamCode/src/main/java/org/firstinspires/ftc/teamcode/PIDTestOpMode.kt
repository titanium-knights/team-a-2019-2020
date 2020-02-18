package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.Gyro
import org.firstinspires.ftc.teamcode.auto.PIDController
import org.firstinspires.ftc.teamcode.util.set

@TeleOp(name = "PID Test", group = "Tests")
class PIDTestOpMode: OpMode() {
    val elapsedTime by lazy { ElapsedTime() }
    val drive by lazy { MecanumDrive.standard(hardwareMap) }
    val controller = PIDController(1.0 / 30, 0.0, 0.0)
    val gyro: Gyro by lazy { BNO055IMUGyro.standard(hardwareMap) }
    private var lastLoop: Double = 0.0
    private var startAngle: Double = 0.0

    override fun init() {
        elapsedTime.reset()
        drive
        gyro.initialize()
    }

    override fun start() {
        super.start()
        lastLoop = elapsedTime.milliseconds()
        startAngle = gyro.angle
    }

    override fun loop() {
        val now = elapsedTime.milliseconds()
        val turn = controller.evaluate(gyro.angle, startAngle, now - lastLoop)
        lastLoop = now

        drive.move(0.5, MecanumDrive.Motor.Vector2D(0.0, 1.0), turn, MecanumDrive.TurnBehavior.MULTIPLY)

        telemetry["Kp"] = controller.Kp
        telemetry["Turn"] = turn
    }

}