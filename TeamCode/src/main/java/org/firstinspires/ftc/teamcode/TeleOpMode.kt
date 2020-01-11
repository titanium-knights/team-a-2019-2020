package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.Button
import org.firstinspires.ftc.teamcode.util.makeButton

/*
 Gamepad 1:
 LEFT STICK - Move & strafe
 RIGHT STICK - Turn
 LEFT BUMPER - Clamps down
 RIGHT BUMPER - Clamps up

 Gamepad 2:
 LEFT STICK - Arm up/down
 RIGHT STICK - Arm left/right
 LEFT BUMPER - Grabber down
 RIGHT BUMPER - Grabber up
 Y - Bypass software limits
 DPAD DOWN - Auto down
 */

@TeleOp(name = "Tele Op Mode (final)", group = "* Main")
class TeleOpMode: OpMode() {
    private val drive by lazy { MecanumDrive.standard(hardwareMap) }
    private val arm by lazy { Arm.standard(hardwareMap) }
    private val clamps by lazy { FoundationClamps.standard(hardwareMap) }
    private val grabber by lazy { Grabber.standard(hardwareMap) }

    private val standardSensors by lazy { StandardSensors(hardwareMap) }
    private val armDistance by lazy { standardSensors.armDistanceSensor }

    private var buttons = listOf<Button>()
    private val clampDownButton by lazy { makeButton(gamepad1, Gamepad::left_bumper)!! }
    private val clampUpButton by lazy { makeButton(gamepad1, Gamepad::right_bumper)!! }
    private val grabberDownButton by lazy { makeButton(gamepad2, Gamepad::left_bumper)!! }
    private val grabberUpButton by lazy { makeButton(gamepad2, Gamepad::right_bumper)!! }

    override fun init() {
        // Since we're using lazy properties, get each of them to initialize them
        drive; arm; clamps; grabber; armDistance
        buttons = listOf(clampDownButton, clampUpButton, grabberDownButton, grabberUpButton)
    }

    override fun loop() {
        buttons.forEach { it.update() }
    }
}