package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import io.github.titanium_knights.stunning_waddle.EventOpMode
import io.github.titanium_knights.stunning_waddle.doWhen
import io.github.titanium_knights.stunning_waddle.makeButton

@TeleOp(name = "Andrew Toggle", group = "Andrew")
class AndrewToggle: EventOpMode({
    val powers = mutableListOf(0.0, 0.0)
    val motors = listOf(hardwareMap[DcMotor::class.java, "topIntake"], hardwareMap[DcMotor::class.java, "bottomIntake"])

    registerLoopHook {
        motors.zip(powers).forEach { it.first.power = it.second }
    }

    doWhen(makeButton(gamepad1::dpad_down).pushed) {
        powers[0] = if (powers[0] != -1.0) -1.0 else 0.0
    }

    doWhen(makeButton(gamepad1::dpad_up).pushed) {
        powers[0] = if (powers[0] != 1.0) 1.0 else 0.0
    }

    doWhen(makeButton(gamepad1::y).pushed) {
        powers[1] = if (powers[1] != -1.0) -1.0 else 0.0
    }

    doWhen(makeButton(gamepad1::a).pushed) {
        powers[1] = if (powers[1] != 1.0) 1.0 else 0.0
    }
})