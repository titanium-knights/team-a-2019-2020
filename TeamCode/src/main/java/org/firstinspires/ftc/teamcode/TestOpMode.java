package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.movement.Arm;
import org.firstinspires.ftc.teamcode.movement.Grabber;
import org.firstinspires.ftc.teamcode.movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Button;

@TeleOp(name = "Test Op Mode (better)", group = "Tests")
public class TestOpMode extends OpMode {
    private MecanumDrive drive;
    private Arm arm;
    private Grabber grabber;

    private Button liftButton;
    private Button grabButton;
    private Button grabberDecrementButton;
    private Button grabberIncrementButton;

    private final double grabberMoveAmount = 0.1;

    @Override
    public void init() {
        gamepad1.setJoystickDeadzone(0.2F);
        gamepad2.setJoystickDeadzone(0.2F);

        drive = MecanumDrive.standard(hardwareMap);
        arm = Arm.standard(hardwareMap);
        grabber = Grabber.standard(hardwareMap);

        liftButton = Button.make(gamepad2, "y");
        grabButton = Button.make(gamepad2, "a");

        grabberDecrementButton = Button.make(gamepad2, "x");
        grabberIncrementButton = Button.make(gamepad2, "b");
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        drive.move(1, new MecanumDrive.Motor.Vector2D(x, -y), turn);

        double horiz = gamepad2.left_stick_x;
        double vert = gamepad2.left_stick_y;
        arm.setPowers(horiz, vert);

        liftButton.update();
        grabButton.update();
        grabberIncrementButton.update();
        grabberDecrementButton.update();

        if (liftButton.wasPressed()) {
            grabber.lift();
        } else if (grabButton.wasPressed()) {
            grabber.grab();
        } else if (grabberIncrementButton.wasPressed()) {
            grabber.move(grabberMoveAmount);
        } else if (grabberDecrementButton.wasPressed()) {
            grabber.move(-grabberMoveAmount);
        }

        telemetry.addData("Grabber", grabber.getPosition());
    }
}