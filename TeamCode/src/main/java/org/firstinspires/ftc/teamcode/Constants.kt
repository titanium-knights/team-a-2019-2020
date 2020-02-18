@file:JvmName("Constants")

package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import org.firstinspires.ftc.teamcode.auto.PIDController

/**
 * Number of milliseconds the robot takes to move one inch.
 */
const val MILLISECONDS_PER_INCH = 3000.0 / 66.0 // 3 seconds to move 66 inches

@Config
object OdometryConfig {
    @JvmField var x = PIDController(0.05, 0.0, 0.01)
    @JvmField var y = PIDController(0.05, 0.0, 0.01)
    @JvmField var r = PIDController(0.02, 0.0, 0.0)
    @JvmField var distThreshold = 4.0
    @JvmField var rotThreshold = 2.0
}