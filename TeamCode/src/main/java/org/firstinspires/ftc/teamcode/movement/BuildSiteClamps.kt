package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Helper class representing our robot's foundation clamps, which grip onto the foundation and pull it across the field.
 *
 * This class uses an arbitrary number of [CRServo]s to grip the foundation; each one can move in a different direction.
 * Together, these servos extend and hook beams onto the ridges of the foundation.
 */
class BuildSiteClamps(
        /**
         * List of continuous rotation servos that spin in one direction.
         */
        val crServos: List<CRServo>,

        /**
         * List of continuous rotation servos that spin in the opposite direction.
         */
        val mirrorServos: List<CRServo>
) {
    /**
     * Begins moving all servos at a specified power, modifying direction as needed.
     * @param power power to move the servos at.
     */
    fun setPower(power: Double) {
        crServos.forEach { it.power = power }
        mirrorServos.forEach { it.power = -power }
    }

    /**
     * Stops all servos.
     */
    fun stop() = setPower(0.0)

    /**
     * Extension function for [LinearOpMode]s which moves the servos at a specified power for a specified amount of
     * time.
     *
     * If you are programming an iterative op mode, or do not need to wait a specified amount of time, use [setPower]
     * instead.
     */
    val move: LinearOpMode.(Double, Long) -> Unit = { power: Double, ms: Long ->
        setPower(power)
        sleep(ms)
        stop()
    }

    companion object {
        /**
         * Returns a FoundationClamps object preconfigured for our robot.
         * @param hardwareMap hardware map of the robot.
         * @return a FoundationClamps object preconfigured for our robot.
         */
        @JvmStatic fun standard(hardwareMap: HardwareMap): BuildSiteClamps = BuildSiteClamps(
                listOf(hardwareMap[CRServo::class.java, "clamp1"]),
                listOf(hardwareMap[CRServo::class.java, "clamp2"])
        )
    }
}