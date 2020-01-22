package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.util.ElapsedTime

class PIDController(
        var Kp: Double,
        var Ki: Double,
        var Kd: Double
) {
    private var integral: Double = 0.0
    private var previous: Double = 0.0

    fun reset() {
        integral = 0.0
        previous = 0.0
    }

    init {
        reset()
    }

    fun evaluate(current: Double, setpoint: Double, deltaTime: Double): Double {
        val error = setpoint - current
        integral += error

        val result = Kp * error + Ki * integral * deltaTime + Kd * (error - previous) / deltaTime
        previous = error

        return result
    }
}