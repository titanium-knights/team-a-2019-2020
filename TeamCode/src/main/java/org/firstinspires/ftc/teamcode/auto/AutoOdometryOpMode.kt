package org.firstinspires.ftc.teamcode.auto

import org.firstinspires.ftc.teamcode.MILLISECONDS_PER_INCH
import org.firstinspires.ftc.teamcode.OdometryConfig

open class AutoOdometryOpMode: AutoBaseOpMode(MILLISECONDS_PER_INCH) {
    val odometry by lazy { Odometry(
            drive,
            arrayOf(DistanceSensorLocalizer(
                    standardSensors.rightDistanceSensor,
                    standardSensors.leftDistanceSensor,
                    standardSensors.backDistanceSensor,
                    gyro
            )),
            OdometryConfig.x,
            OdometryConfig.y,
            OdometryConfig.r,
            OdometryConfig.distThreshold,
            OdometryConfig.rotThreshold
    ) }

    override fun runOpMode() {
        super.runOpMode()
        odometry
    }
}

fun AutoOdometryOpMode.move(x: Double, y: Double, r: Double) {
    odometry.moveSync(Position(x, y, r)) {
        idle()
        opModeIsActive()
    }
}