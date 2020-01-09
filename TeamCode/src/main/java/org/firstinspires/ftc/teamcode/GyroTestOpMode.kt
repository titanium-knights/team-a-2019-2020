package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.teamcode.sensors.BNO055IMUGyro
import org.firstinspires.ftc.teamcode.util.set

@Autonomous(name = "Gyro Test Op Mode", group = "Tests")
class GyroTestOpMode: OpMode() {
    var gyro: BNO055IMUGyro? = null

    override fun init() {
        gyro = BNO055IMUGyro.standard(hardwareMap)
        gyro!!.initialize()
    }

    override fun loop() {
        val angles = gyro!!.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES)
        telemetry["X"] = angles.firstAngle
        telemetry["Y"] = angles.secondAngle
        telemetry["Z"] = angles.thirdAngle
    }
}