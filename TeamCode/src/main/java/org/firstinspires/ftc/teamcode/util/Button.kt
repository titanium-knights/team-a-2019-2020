package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.Gamepad
import kotlin.reflect.KProperty1

fun makeButton(gamepad: Gamepad, button: KProperty1<Gamepad, Boolean>): Button? = try {
    Button.make(gamepad, button.name)
} catch (_: Throwable) {
    null
}