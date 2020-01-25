package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.sensors.StandardSensors
import org.firstinspires.ftc.teamcode.util.set

@Autonomous(name = "Gyro Test Op Mode", group = "Tests")
class GyroTestOpMode: OpMode() {
    private val sensors: StandardSensors by lazy { StandardSensors(hardwareMap) }
    val gyro: BNO055IMUGyro by lazy { BNO055IMUGyro.standard(hardwareMap) }
    var distanceSensors: List<Pair<String, DistanceSensor>> = listOf()

    override fun init() {
        gyro.initialize()
        distanceSensors = listOf("Arm" to sensors.armDistanceSensor, "Back" to sensors.backDistanceSensor, "Left" to sensors.leftDistanceSensor, "Right" to sensors.rightDistanceSensor)
    }

    override fun loop() {
        val angles = gyro.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES)
        telemetry["X"] = angles.firstAngle
        telemetry["Y"] = angles.secondAngle
        telemetry["Z"] = angles.thirdAngle

        for (sensor in distanceSensors) {
            telemetry["Distance ${sensor.first}"] = sensor.second.getDistance(DistanceUnit.CM)
        }
    }
}