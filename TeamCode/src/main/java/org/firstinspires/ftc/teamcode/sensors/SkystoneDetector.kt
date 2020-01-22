package org.firstinspires.ftc.teamcode.sensors

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaSkyStoneNavigation
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.navigation.*

class SkystoneDetector(val localizer: VuforiaLocalizer) {
    val trackables: VuforiaTrackables by lazy { localizer.loadTrackablesFromAsset("Skystone") }

    fun init() {
        trackables[0].location = OpenGLMatrix
                .translation(0f, 0f, 2.00f * 25.4f)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90f, 0f, -90f))

        trackables.activate()
    }

    fun getSkystonePosition(): Int {
        val listener = trackables[0].listener as? VuforiaTrackableDefaultListener
        if (listener?.isVisible != true) {
            return 0
        }

        val location = listener.robotLocation?.translation?.get(1)

        return when {
            location == null -> 0
            location > -4.8 -> 2
            else -> 1
        }
    }
}