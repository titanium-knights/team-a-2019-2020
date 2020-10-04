package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "My Great Program")
public class MyGreatProgram extends LinearOpMode {
    @Override public void runOpMode() {
        DcMotor motor = this.hardwareMap.dcMotor.get("motor");

        this.waitForStart();

        while (this.opModeIsActive()) {
            if (this.gamepad1.dpad_up) {
                motor.setPower(1);
            } else {
                motor.setPower(0);
            }

TouchSensor sensor = hardwareMap.touchSensor.get("sensor");
if (sensor.isPressed()) {
    // boop
}

            // Integers and doubles!
            int x = 2;
            System.out.println(x + 2);

            // If statements!
            if (2 + 2 == 4) {
                System.out.println("2 + 2 = 4");
            }

            // Loops!
            for (int i = 0; i < 5; i++) {
                System.out.println("Loop " + i);
            }
        }
    }
}

class Animal {

}

class Toy {

}

class Dog extends Animal {
    public Dog() {
        sound = "bark";
    }

    private String sound;

    public void makeSound() {
        System.out.println(sound);
    }

    public Toy fetchToy() {
        return new Toy();
    }
}