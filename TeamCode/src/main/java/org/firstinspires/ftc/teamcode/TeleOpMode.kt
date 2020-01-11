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
    enum class ArmState { MANUAL, MOVE_DOWN, MOVE_UP, MOVE_TO_TOUCH_SENSOR }

    private val ArmState.telemetryName get() = when (this) {
        ArmState.MANUAL -> "Manual"
        ArmState.MOVE_DOWN -> "Move Down"
        ArmState.MOVE_UP -> "Move Up"
        ArmState.MOVE_TO_TOUCH_SENSOR -> "Move to Front"
    }

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
    private val moveArmUpButton by lazy { makeButton(gamepad2, Gamepad::dpad_up)!! }
    private val moveArmToTouchSensorButton by lazy { makeButton(gamepad2, Gamepad::dpad_right)!! }

    private var armStartTime: Double = 0.0
    private var armState = ArmState.MANUAL

    private val elapsedTime by lazy { ElapsedTime() }

    private fun setArmState(armState: ArmState, time: Double) {
        if (this.armState == armState) {
            this.armState = ArmState.MANUAL
        } else {
            this.armState = armState
            this.armStartTime = time
        }
    }

    private fun shouldStopArm(time: Double): Boolean {
        if (armState == ArmState.MANUAL) {
            return false
        }

        if (time - armStartTime > 5) {
            return true
        }

        return when (armState) {
            ArmState.MOVE_DOWN -> armDistance.getDistance(DistanceUnit.INCH) < 4
            ArmState.MOVE_UP -> armDistance.getDistance(DistanceUnit.INCH) > 6.5
            ArmState.MOVE_TO_TOUCH_SENSOR -> armTouch.isPressed
            else -> true
        }
    }

    override fun init() {
        // Since we're using lazy properties, get each of them to initialize them
        drive; arm; clamps; grabber; armDistance; armTouch; elapsedTime
        buttons = listOf(
                clampDownButton, clampUpButton, grabberDownButton, grabberUpButton,
                moveArmDownButton, moveArmUpButton, moveArmToTouchSensorButton
        )
    }

    override fun loop() {
        buttons.forEach { it.update() }
        val bypass = gamepad2.y
        val distance = armDistance.getDistance(DistanceUnit.INCH)
        val now = elapsedTime.seconds()

        // Move mecanum drive
        val vector = MecanumDrive.Motor.Vector2D(
                gamepad1.left_stick_x.toDouble(),
                -gamepad1.left_stick_y.toDouble()
        )
        val turn = gamepad1.right_stick_x.toDouble()
        drive.move(1.0, vector, turn, MecanumDrive.TurnBehavior.ADDSUBTRACT)

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

        // Toggle whether arm is being moved
        if (moveArmDownButton.wasPressed()) {
            setArmState(ArmState.MOVE_DOWN, now)
        }
        if (moveArmUpButton.wasPressed()) {
            setArmState(ArmState.MOVE_UP, now)
        }
        if (moveArmToTouchSensorButton.wasPressed()) {
            setArmState(ArmState.MOVE_TO_TOUCH_SENSOR, now)
        }

        // Stop moving the arm if needed
        if (shouldStopArm(now)) {
            armState = ArmState.MANUAL
        }

        // Move arm
        val horizontal = when (armState) {
            ArmState.MOVE_TO_TOUCH_SENSOR -> 0.65
            else -> gamepad2.right_stick_x.toDouble()
        }
        val vertical = when (armState) {
            ArmState.MOVE_UP -> 0.8
            ArmState.MOVE_DOWN -> -1.0
            else -> -gamepad2.left_stick_y.toDouble()
        }
        arm.setPowers(horizontal, vertical)

        telemetry += "=== DRIVE ==="
        telemetry += "X = %.2f, Y = %.2f, Turn = %.2f" % arrayOf(vector.x, vector.y, turn)

        telemetry += "=== ARM ==="
        telemetry += "Mode: ${armState.telemetryName}"
        telemetry += ""
        telemetry += "< Horizontal >: %.2f" % arrayOf(horizontal)
        telemetry += "^  Vertical  v: %.2f" % arrayOf(vertical)
        telemetry += ""
        telemetry += if (armTouch.isPressed) "•   Arm" else "    Arm"
        telemetry += "     |     "
        telemetry += "(%06.2f in)" % arrayOf(distance)
        telemetry += "     |     "
        telemetry += "   Floor   "
    }
}