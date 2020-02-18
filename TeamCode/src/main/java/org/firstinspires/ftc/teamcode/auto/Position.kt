package org.firstinspires.ftc.teamcode.auto

import kotlin.math.pow
import kotlin.math.sqrt

data class Position(val x: Double, val y: Double, val rotation: Double)

infix fun Position.distanceTo(other: Position): Double {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
}