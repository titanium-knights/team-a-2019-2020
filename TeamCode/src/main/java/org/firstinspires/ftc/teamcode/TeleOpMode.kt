package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.Button
import org.firstinspires.ftc.teamcode.util.makeButton
import org.firstinspires.ftc.teamcode.util.plusAssign
import org.firstinspires.ftc.teamcode.util.rem

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
    private val armTouch by lazy { standardSensors.armTouchSensor }

    private var buttons = listOf<Button>()
    private val clampDownButton by lazy { makeButton(gamepad1, Gamepad::left_bumper)!! }
    private val clampUpButton by lazy { makeButton(gamepad1, Gamepad::right_bumper)!! }
    private val grabberDownButton by lazy { makeButton(gamepad2, Gamepad::left_bumper)!! }
    private val grabberUpButton by lazy { makeButton(gamepad2, Gamepad::right_bumper)!! }
    private val moveArmDownButton by lazy { makeButton(gamepad2, Gamepad::dpad_down)!! }

    private var armDownStartTime: Double? = null

    private val elapsedTime by lazy { ElapsedTime() }

    override fun init() {
        // Since we're using lazy properties, get each of them to initialize them
        drive; arm; clamps; grabber; armDistance; armTouch; elapsedTime
        buttons = listOf(clampDownButton, clampUpButton, grabberDownButton, grabberUpButton, moveArmDownButton)
    }

    override fun loop() {
        buttons.forEach { it.update() }
        val bypass = gamepad2.y
        val distance = armDistance.getDistance(DistanceUnit.INCH)

        // Move mecanum drive
        val vector = MecanumDrive.Motor.Vector2D(
                gamepad1.left_stick_x.toDouble(),
                -gamepad1.left_stick_y.toDouble()
        )
        val turn = gamepad1.right_stick_x.toDouble()
        drive.move(1.0, vector, turn)

        // Move clamps
        if (clampDownButton.wasPressed()) {
            clamps.moveDown()
        }
        if (clampUpButton.wasPressed()) {
            clamps.moveUp()
        }

        // Move grabber
        if (grabberDownButton.wasPressed()) {
            grabber.grab()
        }
        if (grabberUpButton.wasPressed()) {
            grabber.lift()
        }

        // Toggle whether arm is being moved down
        val now = elapsedTime.seconds()
        if (moveArmDownButton.wasPressed()) {
            armDownStartTime = if (armDownStartTime == null) now else null
        }

        // If arm is <= 3 cm from ground, or if we've spent over 5 seconds moving the arm, stop moving the arm down
        if (
                armDownStartTime != null &&
                (distance <= 3 || now - armDownStartTime!! > 5)
        ) {
            armDownStartTime = null
        }

        // Move arm
        val horizontal = gamepad2.left_stick_x.toDouble()
        val vertical = if (armDownStartTime != null) -1.0 else -gamepad2.right_stick_y.toDouble()
        arm.setPowers(horizontal, vertical)

        telemetry += "=== DRIVE ==="
        telemetry += "X = %.2f, Y = %.2f, Turn = %.2f" % arrayOf(vector.x, vector.y, turn)

        telemetry += "=== ARM ==="
        telemetry += "< Horizontal >: %.2f" % arrayOf(horizontal)
        telemetry += if (armDownStartTime == null)
            "^  Vertical  v: %.2f" % arrayOf(vertical)
            else "Moving down... (%d/5s)" % arrayOf((now - armDownStartTime!!).toInt())
        telemetry += ""
        telemetry += if (armTouch.isPressed) "•   Arm" else "    Arm"
        telemetry += "     |     "
        telemetry += "(%06.2f in)" % arrayOf(distance)
        telemetry += "     |     "
        telemetry += "   Floor   "
    }
}