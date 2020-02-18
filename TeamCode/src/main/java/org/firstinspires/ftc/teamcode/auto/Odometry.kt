package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import java.lang.NullPointerException
import kotlin.math.abs

class Odometry(
        val drive: MecanumDrive,
        val localizers: Array<Localizer>,
        val xPIDController: PIDController,
        val yPIDController: PIDController,
        val rotPIDController: PIDController,
        val distThreshold: Double,
        val rotThreshold: Double
) {
    val elapsedTime = ElapsedTime()

    fun moveSync(target: Position, onLoop: (Position) -> Boolean = { true }) {
        var current: Position? = null
        var prevTime = elapsedTime.milliseconds()
        do {
            for (localizer in localizers) {
                val pos = localizer.update()
                if (pos != null) {
                    current = pos
                    break
                }
            }

            if (current == null) {
                throw NullPointerException("current is null")
            }

            val now = elapsedTime.milliseconds()
            val deltaTime = now - prevTime
            drive.move(
                    1.0,
                    MecanumDrive.Motor.Vector2D(
                            xPIDController.evaluate(current.x, target.x, deltaTime),
                            yPIDController.evaluate(current.y, target.y, deltaTime)
                    ),
                    rotPIDController.evaluate(current.rotation, target.rotation, deltaTime),
                    MecanumDrive.TurnBehavior.ADDSUBTRACT
            )
            prevTime = now
        } while (onLoop(current!!) && (current distanceTo target > distThreshold || abs(current.rotation - target.rotation) > rotThreshold))

        drive.stop()
    }
}