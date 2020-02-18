package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.sensors.Gyro

interface Localizer {
    fun update(): Position?
}

val fieldSize = 144

class DistanceSensorLocalizer(
        val xNeg: DistanceSensor,
        val xPos: DistanceSensor,
        val yPos: DistanceSensor,
        val gyro: Gyro
): Localizer {
    override fun update(): Position {
        val xNegPosition = xNeg.getDistance(DistanceUnit.INCH)
        val xPosPosition = xPos.getDistance(DistanceUnit.INCH)
        val yPosPosition = yPos.getDistance(DistanceUnit.INCH)
        return Position(
                if (xNegPosition < xPosPosition) fieldSize - xNegPosition else xPosPosition,
                yPosPosition,
                gyro.angle
        )
    }
}