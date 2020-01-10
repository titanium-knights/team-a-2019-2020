package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.util.Button
import org.firstinspires.ftc.teamcode.util.makeButton
import org.firstinspires.ftc.teamcode.util.set

@TeleOp(name = "Clamp Test Op Mode", group = "Tests")
class ClampTestOpMode: OpMode() {
    val clamps by lazy { FoundationClamps.standard(hardwareMap) }
    val servos by lazy { clamps.servos }
    var servoIndex = 0
    val servo get() = servos[servoIndex]

    val prevButton by lazy { makeButton(gamepad1, Gamepad::left_bumper)!! }
    val nextButton by lazy { makeButton(gamepad1, Gamepad::right_bumper)!! }
    val incButton by lazy { makeButton(gamepad1, Gamepad::b)!! }
    val decButton by lazy { makeButton(gamepad1, Gamepad::x)!! }
    val upButton by lazy { makeButton(gamepad1, Gamepad::y)!! }
    val downButton by lazy { makeButton(gamepad1, Gamepad::a)!! }
    var buttons: List<Button> = listOf()

    override fun init() {
        servos
        buttons = listOf(prevButton, nextButton, incButton, decButton, upButton, downButton)
    }

    override fun loop() {
        buttons.forEach { it.update() }

        if (prevButton.wasPressed()) {
            servoIndex = (servoIndex - 1 + servos.size) % servos.size
        }

        if (nextButton.wasPressed()) {
            servoIndex = (servoIndex + 1) % servos.size
        }

        if (incButton.wasPressed()) {
            servo.position += 0.1
        }

        if (decButton.wasPressed()) {
            servo.position -= 0.1
        }

        if (upButton.wasPressed()) {
            clamps.moveUp()
        }

        if (downButton.wasPressed()) {
            clamps.moveDown()
        }

        telemetry["Positions"] = servos.map { it.position }.joinToString(", ")
    }
}