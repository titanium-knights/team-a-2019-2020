package org.firstinspires.ftc.teamcode.sensors

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaSkyStoneNavigation
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.navigation.*

class SkystoneDetector(val localizer: VuforiaLocalizer) {
    private val trackables: VuforiaTrackables by lazy { localizer.loadTrackablesFromAsset("Skystone") }

    fun init() {
        while (trackables.size > 1) {
            trackables.removeAt(trackables.size - 1)
        }

        trackables.first().location = OpenGLMatrix
                .translation(0f, 0f, 2.00f * 25.4f)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90f, 0f, -90f))

        trackables.activate()
    }

    fun getSkystonePosition(): Int {
        val listener = trackables.first().listener as? VuforiaTrackableDefaultListener
        val location = listener?.robotLocation?.translation?.get(1)

        return when {
            location == null -> 0
            location > -4.8 -> 1
            else -> 2
        }
    }
}