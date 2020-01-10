package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Helper class representing our robot's foundation clamps, which grip onto the foundation and pull it across the field.
 *
 * This class uses an arbitrary number of [Servo]s to grip the foundation; each one can move to different positions.
 * Together, these servos extend and hook beams onto the ridges of the foundation.
 */
class BuildSiteClamps(
        /**
         * List of clamps, each representing a servo.
         */
        val clamps: List<Clamp>
) {
    data class Clamp(val servo: Servo, val downPosition: Double, val upPosition: Double)

    val servos get() = clamps.map { it.servo }

    fun moveDown() = clamps.forEach { it.servo.position = it.downPosition }

    fun moveUp() = clamps.forEach { it.servo.position = it.upPosition }

    companion object {
        /**
         * Returns a BuildSiteClamps object preconfigured for our robot.
         * @param hardwareMap hardware map of the robot.
         * @return a BuildSiteClamps object preconfigured for our robot.
         */
        @JvmStatic fun standard(hardwareMap: HardwareMap): BuildSiteClamps = BuildSiteClamps(listOf(
                Clamp(hardwareMap[Servo::class.java, "clamp1"], 0.0, 1.0),
                Clamp(hardwareMap[Servo::class.java, "clamp2"], 0.0, 1.0)
        ))
    }
}