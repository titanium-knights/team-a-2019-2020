@file:JvmName("VuforiaAuthKey")

package org.firstinspires.ftc.teamcode.sensors

import android.content.Context
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.R
import java.lang.Exception

fun getVuforiaAuthKey(context: Context): String? = try {
    val stream = context.resources.openRawResource(R.raw.vuforia)
    val reader = stream.bufferedReader()
    val key = reader.readLine().replace("\n", "")
    stream.close()

    key
} catch (e: Exception) {
    null
}

fun getVuforiaAuthKey(hardwareMap: HardwareMap) = getVuforiaAuthKey(hardwareMap.appContext)