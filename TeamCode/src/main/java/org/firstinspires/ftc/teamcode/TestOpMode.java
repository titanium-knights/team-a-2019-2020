package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.movement.Arm;
import org.firstinspires.ftc.teamcode.movement.FoundationClamps;
import org.firstinspires.ftc.teamcode.movement.Grabber;
import org.firstinspires.ftc.teamcode.movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Button;

@TeleOp(name = "Test Op Mode (better)", group = "Tests")
public class TestOpMode extends OpMode {
    private MecanumDrive drive;
    private Arm arm;
    private Grabber grabber;
    private FoundationClamps clamps;

    private Button liftButton;
    private Button grabButton;
    private Button grabberDecrementButton;
    private Button grabberIncrementButton;
    private Button clampDecrementButton;
    private Button clampIncrementButton;

    private final double grabberMoveAmount = 0.1;

    @Override
    public void init() {
        gamepad1.setJoystickDeadzone(0.2F);
        gamepad2.setJoystickDeadzone(0.2F);

        drive = MecanumDrive.standard(hardwareMap);
        arm = Arm.standard(hardwareMap);
        grabber = Grabber.standard(hardwareMap);
        clamps = FoundationClamps.standard(hardwareMap);

        liftButton = Button.make(gamepad2, "y");
        grabButton = Button.make(gamepad2, "a");

        grabberDecrementButton = Button.make(gamepad2, "x");
        grabberIncrementButton = Button.make(gamepad2, "b");

        clampDecrementButton = Button.make(gamepad2, "dpad_down");
        clampIncrementButton = Button.make(gamepad2, "dpad_up");
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        drive.move(1, new MecanumDrive.Motor.Vector2D(x, -y), turn, MecanumDrive.TurnBehavior.ADDSUBTRACT);

        double horiz = gamepad2.left_stick_x;
        double vert = gamepad2.left_stick_y;
        arm.setPowers(horiz, -vert);

        liftButton.update();
        grabButton.update();
        grabberIncrementButton.update();
        grabberDecrementButton.update();
        clampDecrementButton.update();
        clampIncrementButton.update();

        if (liftButton.wasPressed()) {
            grabber.lift();
        } else if (grabButton.wasPressed()) {
            grabber.grab();
        } else if (grabberIncrementButton.wasPressed()) {
            grabber.move(grabberMoveAmount);
        } else if (grabberDecrementButton.wasPressed()) {
            grabber.move(-grabberMoveAmount);
        }

        if (gamepad2.dpad_left) {
            clamps.getCrServo().setPower(-1);
        } else if (gamepad2.dpad_right) {
            clamps.getCrServo().setPower(1);
        } else {
            clamps.getCrServo().setPower(0);
        }

        if (clampIncrementButton.wasPressed()) {
            clamps.getServo().setPosition(clamps.getServo().getPosition() + 0.1);
        } else if (clampDecrementButton.wasPressed()) {
            clamps.getServo().setPosition(clamps.getServo().getPosition() - 0.1);
        }

        telemetry.addData("Grabber", grabber.getPosition());
        telemetry.addData("Clamp", clamps.getServo().getPosition());
        telemetry.addData("Arm Pos", arm.getVerticalMotor().getCurrentPosition());
    }
}
