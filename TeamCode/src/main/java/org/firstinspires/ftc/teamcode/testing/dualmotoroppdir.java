package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "dualmotoroppdir", group = "Examples")
public class dualmotoroppdir extends LinearOpMode {

    // Declare motor variables
    private DcMotor motor1;
    private DcMotor motor2;

    @Override
    public void runOpMode() {

        // Initialize motors from the hardware map
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");

        // Reverse one motor to ensure opposite direction motion
        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start of the OpMode
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            // Read joystick value
            double joystickY = -gamepad1.left_stick_y; // Invert Y for natural control

            // Set motor power proportional to joystick position
            motor1.setPower(joystickY);
            motor2.setPower(joystickY);

            // Telemetry for debugging
            telemetry.addData("Joystick Y", joystickY);
            telemetry.addData("Motor 1 Power", motor1.getPower());
            telemetry.addData("Motor 2 Power", motor2.getPower());
            telemetry.update();
        }
    }
}
