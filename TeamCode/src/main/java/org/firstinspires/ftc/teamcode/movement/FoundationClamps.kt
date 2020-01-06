package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Helper class representing our robot's foundation clamps, which grip onto the foundation and pull it across the field.
 *
 * This class uses a [Servo] and a [CRServo] to grip the foundation; ask Tycho and Max why this is the case.
 * Together, these servos extend and hook beams onto the ridges of the foundation.
 *
 * > It's not our fault! - Tycho and Max (on mixing servo types)
 */
class FoundationClamps(
        val servo: Servo,
        val crServo: CRServo
) {
    /**
     * Extension function for [LinearOpMode]s which moves the continuous rotation servo at a specified power
     * for a specified amount of time.
     *
     * If you are programming an iterative op mode, or do not need to wait a specified amount of time, use
     * [CRServo.setPower] instead.
     */
    val move: LinearOpMode.(Double, Long) -> Unit = { power: Double, ms: Long ->
        crServo.power = power
        sleep(ms)
        crServo.power = 0.0
    }

    companion object {
        /**
         * Returns a FoundationClamps object preconfigured for our robot.
         * @param hardwareMap hardware map of the robot.
         * @return a FoundationClamps object preconfigured for our robot.
         */
        @JvmStatic fun standard(hardwareMap: HardwareMap): FoundationClamps = FoundationClamps(
                hardwareMap[Servo::class.java, "clamp2"],
                hardwareMap[CRServo::class.java, "clamp1"]
        )
    }
}