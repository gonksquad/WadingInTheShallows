package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This OpMode sets a servo to its absolute limits.
 * Triangle button moves the servo to its maximum position.
 * Cross button moves the servo to its minimum position.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "singleservo")
public class singleservo extends OpMode {

    private Servo servo;  // Declare the servo
    private static final double MAX_POSITION = 1.0;  // Absolute maximum servo position
    private static final double MIN_POSITION = 0.0;  // Absolute minimum servo position

    @Override
    public void init() {
        // Initialize the servo hardware
        servo = hardwareMap.get(Servo.class, "servo");  // Replace "servo" with your servo's name in the configuration
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Check gamepad inputs
        if (gamepad1.triangle) {
            servo.setPosition(MAX_POSITION);  // Move to maximum position
            telemetry.addData("Servo Position", "Max: %.2f", MAX_POSITION);
        } else if (gamepad1.cross) {
            servo.setPosition(MIN_POSITION);  // Move to minimum position
            telemetry.addData("Servo Position", "Min: %.2f", MIN_POSITION);
        } else {
            telemetry.addData("Servo Position", "No Input");
        }

        telemetry.update();  // Update telemetry data
    }
}
