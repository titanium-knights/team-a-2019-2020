package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.github.titanium_knights.stunning_waddle.EventOpMode
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.util.Vector2D
import org.firstinspires.ftc.teamcode.util.plusAssign
import org.firstinspires.ftc.teamcode.util.rem

@TeleOp(name = "Andrew Drive", group = "Andrew")
class AndrewDrive: EventOpMode({
    val drive = MecanumDrive.standard(hardwareMap)
    registerLoopHook {
        val vector = Vector2D(gamepad1.left_stick_x.toDouble(), -gamepad1.left_stick_y.toDouble())
        val turn = gamepad1.right_stick_x.toDouble()
        val power = 1.0
        drive.move(power, vector, turn * power, MecanumDrive.TurnBehavior.ADDSUBTRACT)

        telemetry += "=== DRIVE ==="
        telemetry += "(%.2f, %.2f) @ %.2f, Turn = %.2f" % arrayOf(vector.x, vector.y, power, turn)
        telemetry += ""
    }
})