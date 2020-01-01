package org.firstinspires.ftc.teamcode.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class StandardSensors {
    private HardwareMap hardwareMap;

    public StandardSensors(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public ColorSensor getColorSensor() {
        return hardwareMap.get(ColorSensor.class, "color");
    }
}
