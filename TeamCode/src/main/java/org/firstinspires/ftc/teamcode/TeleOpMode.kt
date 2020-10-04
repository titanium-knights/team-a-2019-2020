package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
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
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
class TeleOpMode: EventOpMode({
    val drive = MecanumDrive.standard(hardwareMap)
    val clamps = FoundationClamps.standard(hardwareMap)
    val grabber = Grabber.standard(hardwareMap)
    val arm = Arm.standard(hardwareMap)

    val sensors = StandardSensors(hardwareMap)
    val armDistance = sensors.armDistanceSensor
    val armTouch = sensors.armTouchSensor

    val elapsedTime = ElapsedTime()

    var previousTime = 0.0
    var currentTime = 0.0
    var deltaTime = 0.0

    registerStartHook {
        currentTime = elapsedTime.milliseconds()
    }

    registerLoopHook {
        previousTime = currentTime
        currentTime = elapsedTime.milliseconds()
        deltaTime = currentTime - previousTime
    }

    var bypass = false
    registerLoopHook {
        bypass = gamepad1.y || gamepad2.y
    }

    var isSlow = false
    registerLoopHook {
        val vector = Vector2D(gamepad1.left_stick_x.toDouble(), -gamepad1.left_stick_y.toDouble())
        val turn = gamepad1.right_stick_x.toDouble()
        val power = if (isSlow) 0.3 else 1.0
        drive.move(power, vector, turn * power, MecanumDrive.TurnBehavior.ADDSUBTRACT)

        telemetry += "=== DRIVE ==="
        telemetry += "(%.2f, %.2f) @ %.2f, Turn = %.2f" % arrayOf(vector.x, vector.y, power, turn)
        telemetry += ""
    }

    doWhen(gamepad1::x.pressed) { isSlow = true }
    doWhen(gamepad1::b.pressed) { isSlow = false }

    doWhen(
            makeButton(gamepad1::left_bumper).pushed then { clamps.moveDown() },
            makeButton(gamepad1::right_bumper).pushed then { clamps.moveUp() }
    )

    doWhen(
            makeButton(gamepad2::left_bumper).pushed then { grabber.grab() },
            makeButton(gamepad2::right_bumper).pushed then { grabber.lift() }
    )

    var horizontalTarget = 0.0
    var verticalTarget = 0.0

    var horizontalActual = 0.0
    var verticalActual = 0.0

    registerLoopHook {
        val verticalRamp = if (abs(verticalActual) < abs(verticalTarget)) {
            TeleOpConfig.armVerticalRampUp
        } else {
            TeleOpConfig.armVerticalRampDown
        }

        verticalActual = if (bypass || verticalRamp == 0.0 || verticalActual == verticalTarget) {
            verticalTarget
        } else if (verticalActual < verticalTarget) {
            min(verticalTarget, verticalActual + deltaTime / verticalRamp)
        } else {
            max(verticalTarget, verticalActual - deltaTime / verticalRamp)
        }
    }

    registerLoopHook {
        arm.setPowers(horizontalActual, verticalActual)
    }

    registerLoopHook {
        telemetry += "=== ARM ==="
        telemetry += ""
        telemetry += "Horizontal: %.2f (%.2f)" % arrayOf(horizontalTarget, horizontalActual)
        telemetry += "Vertical: %.2f (%.2f)" % arrayOf(verticalTarget, verticalActual)
        telemetry += ""
        telemetry += if (armTouch.isPressed) "•   Arm" else "    Arm"
        telemetry += "(%06.2f in)" % arrayOf(armDistance.getDistance(DistanceUnit.INCH))
        telemetry += "   Floor   "
    }
})

@Config object TeleOpConfig {
    @JvmField var armHorizontalRampUp = 1000.0
    @JvmField var armHorizontalRampDown = 500.0
    @JvmField var armVerticalRampUp = 500.0
    @JvmField var armVerticalRampDown = 200.0
}
