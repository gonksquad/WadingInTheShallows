package org.firstinspires.ftc.teamcode.S25.modular.tier1.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class strange extends LinearOpMode {
    public DcMotorEx zero;
    public DcMotorEx one;
    public void runOpMode() {
        zero = hardwareMap.get(DcMotorEx.class, "0");
        one = hardwareMap.get(DcMotorEx.class, "1");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Power", gamepad1.left_stick_y);
            zero.setPower(gamepad1.left_stick_y);
            one.setPower(-gamepad1.left_stick_y);
            sleep(10);
        }
    }
}
