package org.firstinspires.ftc.teamcode.sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * <p>Helper class preconfigured for easy retrieval of our robot's sensors.</p>
 *
 */
public class StandardSensors {
    private HardwareMap hardwareMap;

    /**
     * Constructs a StandardSensors object with the given hardware map.
     * @param hardwareMap hardware map of the robot.
     */
    public StandardSensors(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    /**
     * Returns a color sensor preconfigured for our robot.
     * @return a preconfigured color sensor.
     */
    public ColorSensor getColorSensor() {
        return hardwareMap.get(ColorSensor.class, "color");
    }

    private DistanceSensor getDistanceSensor(String name) {
        return hardwareMap.get(DistanceSensor.class, name);
    }

    public DistanceSensor getFrontDistanceSensor() { return getDistanceSensor("front_distance"); }
    public DistanceSensor getRightDistanceSensor() { return getDistanceSensor("right_distance"); }
    public DistanceSensor getArmDistanceSensor() { return getDistanceSensor("arm_distance"); }

    public TouchSensor getArmTouchSensor() { return hardwareMap.get(TouchSensor.class, "arm_touch"); }
}
