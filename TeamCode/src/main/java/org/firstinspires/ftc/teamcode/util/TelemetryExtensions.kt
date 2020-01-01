package org.firstinspires.ftc.teamcode.util

import org.firstinspires.ftc.robotcore.external.Telemetry

operator fun Telemetry.set(name: String, value: Any) {
    addData(name, value)
}