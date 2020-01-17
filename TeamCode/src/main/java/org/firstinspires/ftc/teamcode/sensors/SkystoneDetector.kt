package org.firstinspires.ftc.teamcode.sensors

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables

class SkystoneDetector(val localizer: VuforiaLocalizer) {
    private var trackable: VuforiaTrackable? = null
    val stoneTarget get() = trackable

    private var trackables: VuforiaTrackables? = null

    fun init() {
        trackables = localizer.loadTrackablesFromAsset("Skystone")
        while (trackables!!.size > 1) {
            trackables!!.removeAt(trackables!!.size - 1)
        }

        trackables
    }
}