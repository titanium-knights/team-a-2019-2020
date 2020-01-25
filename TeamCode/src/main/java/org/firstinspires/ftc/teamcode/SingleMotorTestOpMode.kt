package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.util.makeButton
import org.firstinspires.ftc.teamcode.util.set

@TeleOp(name = "Single Motor Test Op Mode", group = "Tests")
class SingleMotorTestOpMode: OpMode() {
    val motorNames = arrayOf("mecanum_fl", "mecanum_fr", "mecanum_bl", "mecanum_br")

    val motors by lazy { motorNames.map { hardwareMap[DcMotor::class.java, it] } }
    var i = 0

    val prevButton by lazy { makeButton(gamepad1, Gamepad::dpad_left)!! }
    val nextButton by lazy { makeButton(gamepad1, Gamepad::dpad_right)!! }

    override fun init() {
        motors.forEach { it.direction = DcMotorSimple.Direction.FORWARD }
        prevButton
        nextButton
    }

    override fun loop() {
        prevButton.update()
        nextButton.update()

        if (prevButton.wasPressed()) {
            motors[i].power = 0.0
            i = (i - 1 + motors.size) % motors.size
        }

        if (nextButton.wasPressed()) {
            motors[i].power = 0.0
            i = (i + 1) % motors.size
        }

        val name = motorNames[i]
        val current = motors[i]
        val power = -gamepad1.left_stick_y.toDouble()

        current.power = power

        telemetry["Current"] = name
        telemetry["Power"] = power
    }
}
