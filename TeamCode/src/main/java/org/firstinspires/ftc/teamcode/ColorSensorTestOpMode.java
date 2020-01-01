package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.teamcode.sensors.StandardSensors;

@Autonomous(name = "Color Sensor Test Op Mode", group = "Test")
public class ColorSensorTestOpMode extends OpMode {
    ColorSensor sensor;

    @Override
    public void init() {
        sensor = new StandardSensors(hardwareMap).getColorSensor();
    }

    @Override
    public void loop() {
        telemetry.addData("R", sensor.red());
        telemetry.addData("G", sensor.green());
        telemetry.addData("B", sensor.blue());

        telemetry.addData("Luminosity", sensor.alpha());
        telemetry.addData("Hue", sensor.argb());
    }
}
