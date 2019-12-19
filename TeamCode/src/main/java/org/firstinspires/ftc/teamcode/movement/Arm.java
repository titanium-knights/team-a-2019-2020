package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm {
    private DcMotor horizontalMotor;
    private DcMotor verticalMotor;

    private double horizontalMultiplier;
    private double verticalMultiplier;

    public Arm(DcMotor horizontal, DcMotor vertical,
               double horizontalMultiplier, double verticalMultiplier) {
        horizontalMotor = horizontal;
        verticalMotor = vertical;

        this.horizontalMultiplier = horizontalMultiplier;
        this.verticalMultiplier = verticalMultiplier;
    }

    public DcMotor getHorizontalMotor() {
        return horizontalMotor;
    }

    public DcMotor getVerticalMotor() {
        return verticalMotor;
    }

    public void setHorizontalPower(double power) {
        horizontalMotor.setPower(power);
    }

    public void setVerticalPower(double power) {
        verticalMotor.setPower(power);
    }

    public void setPowers(double horizontal, double vertical) {
        horizontalMotor.setPower(horizontal);
        verticalMotor.setPower(vertical);
    }

    public void stop() {
        setPowers(0, 0);
    }

    private static String ARM_HORIZONTAL_NAME = "arm_horizontal";
    private static String ARM_VERTICAL_NAME = "arm_vertical";

    private static double ARM_HORIZONTAL_MULTIPLIER = -1;
    private static double ARM_VERTICAL_MULTIPLIER = 1;

    public static Arm standard(HardwareMap hardwareMap) {
        return new Arm(
                hardwareMap.get(DcMotor.class, ARM_HORIZONTAL_NAME),
                hardwareMap.get(DcMotor.class, ARM_VERTICAL_NAME),
                ARM_HORIZONTAL_MULTIPLIER,
                ARM_VERTICAL_MULTIPLIER);
    }
}
