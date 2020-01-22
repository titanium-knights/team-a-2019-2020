package org.firstinspires.ftc.teamcode.sensors

import android.graphics.Bitmap
import com.vuforia.Image
import com.vuforia.PIXEL_FORMAT
import com.vuforia.Vuforia
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import java.lang.Exception

enum class SkystonePosition { LEFT, CENTER, RIGHT }

class CameraSkystoneDetector(val localizer: VuforiaLocalizer) {
    @Throws(InterruptedException::class) fun getSkystone() {
        val format = PIXEL_FORMAT.RGB565

        Vuforia.setFrameFormat(format, true)
        localizer.frameQueueCapacity = 1

        var image: Image? = null

        while (image == null) {
            var frame: VuforiaLocalizer.CloseableFrame? = null

            try {
                frame = localizer.frameQueue.take()
                for (i in 0 until frame.numImages) {
                    val current: Image? = frame.getImage(i.toInt())
                    if (current?.format == format) {
                        image = current
                        break
                    }
                }
            } catch (e: Exception) {
                frame?.close()
                throw e
            }

            frame.close()
        }

        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.RGB_565)
        bitmap.copyPixelsFromBuffer(image.pixels)


    }
}