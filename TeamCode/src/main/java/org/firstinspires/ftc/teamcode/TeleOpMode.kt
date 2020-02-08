package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import io.github.titanium_knights.stunning_waddle.*
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.movement.Arm
import org.firstinspires.ftc.teamcode.movement.FoundationClamps
import org.firstinspires.ftc.teamcode.movement.Grabber
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.Vector2D
import org.firstinspires.ftc.teamcode.util.plusAssign
import org.firstinspires.ftc.teamcode.util.rem
import kotlin.math.max

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
@TeleOp(name = "Tele Op Mode (experimental)", group = "* Main")
class NewTeleOpMode: EventOpMode({
    val drive = MecanumDrive.standard(hardwareMap)
    val clamps = FoundationClamps.standard(hardwareMap)
    val grabber = Grabber.standard(hardwareMap)
    val arm = Arm.standard(hardwareMap)

    val sensors = StandardSensors(hardwareMap)
    val armDistance = sensors.armDistanceSensor
    val armTouch = sensors.armTouchSensor

    val elapsedTime = ElapsedTime()

    var armMode = TeleOpMode.ArmState.MANUAL
    var armModeStartTime = elapsedTime.milliseconds()
    fun setArmMode(newMode: TeleOpMode.ArmState) {
        if (armMode == newMode) {
            armMode = TeleOpMode.ArmState.MANUAL
        } else {
            armMode = newMode
            armModeStartTime = elapsedTime.milliseconds()
        }
    }

    var bypass = false
    registerLoopHook {
        bypass = gamepad1.y
    }

    val slow = makeToggleButton(gamepad1::x)
    registerLoopHook {
        val vector = Vector2D(gamepad1.left_stick_x.toDouble(), -gamepad1.left_stick_y.toDouble())
        val turn = gamepad1.right_stick_x.toDouble()
        val power = if (slow.selection) 0.3 else 1.0
        drive.move(power, vector, turn, MecanumDrive.TurnBehavior.ADDSUBTRACT)

        telemetry += "=== DRIVE ==="
        telemetry += "(%.2f, %.2f) @ %.2f, Turn = %.2f" % arrayOf(vector.x, vector.y, power, turn)
        telemetry += ""
    }

    doWhen(
            makeButton(gamepad1::left_bumper).pushed then { clamps.moveDown() },
            makeButton(gamepad1::right_bumper).pushed then { clamps.moveUp() }
    )

    doWhen(
            makeButton(gamepad2::left_bumper).pushed then { grabber.grab() },
            makeButton(gamepad2::right_bumper).pushed then { grabber.lift() }
    )

    doWhen(
            makeButton(gamepad2::dpad_down).pushed then { setArmMode(TeleOpMode.ArmState.MOVE_DOWN) },
            makeButton(gamepad2::dpad_up).pushed then { setArmMode(TeleOpMode.ArmState.MOVE_UP) },
            makeButton(gamepad2::dpad_right).pushed then { setArmMode(TeleOpMode.ArmState.MOVE_TO_TOUCH_SENSOR) }
    )

    registerLoopHook {
        if (elapsedTime.milliseconds() - armModeStartTime > 3 || gamepad2.left_stick_y > 0 || gamepad2.right_stick_x > 0) {
            armMode = TeleOpMode.ArmState.MANUAL
        }

        if (when (armMode) {
                    TeleOpMode.ArmState.MOVE_DOWN -> armDistance.getDistance(DistanceUnit.INCH) < 4.2
                    TeleOpMode.ArmState.MOVE_UP -> armDistance.getDistance(DistanceUnit.INCH) > 6.5
                    TeleOpMode.ArmState.MOVE_TO_TOUCH_SENSOR -> armTouch.isPressed
                    else -> true
                }) {
            armMode = TeleOpMode.ArmState.MANUAL
        }

        val horizontal = when (armMode) {
            TeleOpMode.ArmState.MOVE_TO_TOUCH_SENSOR -> 0.65
            else -> gamepad2.right_stick_x.toDouble()
        }
        val vertical = when (armMode) {
            TeleOpMode.ArmState.MOVE_UP -> 0.8
            TeleOpMode.ArmState.MOVE_DOWN -> -1.0
            else -> {
                if (!bypass && armDistance.getDistance(DistanceUnit.INCH) < 4.2) {
                    max(0.0, -gamepad2.left_stick_y.toDouble())
                } else {
                    -gamepad2.left_stick_y.toDouble()
                }
            }
        }
        arm.setPowers(horizontal, vertical)

        telemetry += "=== ARM ==="
        telemetry += "Mode: ${when (armMode) {
            TeleOpMode.ArmState.MOVE_UP -> "Auto Up"
            TeleOpMode.ArmState.MOVE_DOWN -> "Auto Down"
            TeleOpMode.ArmState.MOVE_TO_TOUCH_SENSOR -> "Auto Right"
            TeleOpMode.ArmState.MANUAL -> "Manual"
        }}"
        telemetry += ""
        telemetry += "Horizontal: %.2f" % arrayOf(horizontal)
        telemetry += "Vertical: %.2f" % arrayOf(vertical)
        telemetry += ""
        telemetry += if (armTouch.isPressed) "•   Arm" else "    Arm"
        telemetry += "(%06.2f in)" % arrayOf(armDistance.getDistance(DistanceUnit.INCH))
        telemetry += "   Floor   "
    }
})