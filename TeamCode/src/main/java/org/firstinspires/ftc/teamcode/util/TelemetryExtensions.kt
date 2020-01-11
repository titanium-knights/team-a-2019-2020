package org.firstinspires.ftc.teamcode.util

import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Records a value in the robot's telemetry (displayed on the driver station) with a given name.
 *
 * @param name name to assign.
 * @param value value to add to the telemetry.
 */
operator fun Telemetry.set(name: String, value: Any) {
    addData(name, value)
}

operator fun Telemetry.plusAssign(line: String) {
    addLine(line)
}

operator fun String.rem(args: Array<*>): String {
    return String.format(this, *args)
}