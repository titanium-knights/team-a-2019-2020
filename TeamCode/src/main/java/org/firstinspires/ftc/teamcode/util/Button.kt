package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.Gamepad
import io.github.titanium_knights.stunning_waddle.Condition
import io.github.titanium_knights.stunning_waddle.Hook
import kotlin.reflect.KProperty1

fun makeButton(gamepad: Gamepad, button: KProperty1<Gamepad, Boolean>): Button? = try {
    Button.make(gamepad, button.name)
} catch (_: Throwable) {
    null
}

fun makeToggleButton(gamepad: Gamepad, button: KProperty1<Gamepad, Boolean>): ToggleButton? = try {
    ToggleButton.make(gamepad, button.name)
} catch (_: Throwable) {
    null
}