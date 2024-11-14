package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



@TeleOp

public class TeleopScrimmage extends LinearOpMode {
    public DcMotor LF;
    public DcMotor LB;
    public DcMotor RB;
    public DcMotor RF;
    public DcMotor aimLow;
    public DcMotor aimHigh;
    public Servo grabber;
    public Servo placer;
    public Servo placerSpin;
    public Servo grabberCorrection;
    public Servo grabberSpin;

    private static ElapsedTime myStopwatch = new ElapsedTime();

    private double time;
    private double speed = 1;

    public void runOpMode() {

        LF = hardwareMap.get(DcMotor.class, "2");
        LB = hardwareMap.get(DcMotor.class, "0");
        RB = hardwareMap.get(DcMotor.class, "1");
        RF = hardwareMap.get(DcMotor.class, "3");

        aimHigh = hardwareMap.get(DcMotor.class, "E0");
        aimLow = hardwareMap.get(DcMotor.class, "E1");

        grabber = hardwareMap.get(Servo.class, "ES0");
        placer = hardwareMap.get(Servo.class, "ES1");
        placerSpin = hardwareMap.get(Servo.class, "ES2");
        grabberCorrection = hardwareMap.get(Servo.class, "ES3");
        grabberSpin = hardwareMap.get(Servo.class, "ES4");

        waitForStart();

        myStopwatch.reset();


        while (opModeIsActive()) {

            /* Drive Control */
            {
                double x = gamepad1.left_stick_x * speed;
                double y = gamepad1.left_stick_y * speed;
                double turn = gamepad1.right_stick_x * speed;

                double theta = Math.atan2(y, x);
                double power = Math.hypot(x, y);
                double sin = Math.sin(theta - Math.PI / 4);
                double cos = Math.cos(theta - Math.PI / 4);
                double max = Math.max(Math.abs(sin), Math.abs(cos));
                RF.setPower(power * cos / max + turn);
                LF.setPower(power * sin / max - turn);
                RB.setPower(power * sin / max + turn);
                LB.setPower(power * cos / max - turn);
                if ((power + Math.abs(turn)) > 1) {
                    LF.setPower((LF.getPower()) / (power + turn));
                    RF.setPower((RF.getPower()) / (power + turn));
                    RB.setPower((RB.getPower()) / (power + turn));
                    LB.setPower((LB.getPower()) / (power + turn));
                }
            }

            /* Speed Control */
            {
                speed = (-1 * gamepad1.right_trigger) + 1;
            }

            //grabber =           1 ==  open
            //grabberCorrection = 1 ==  UNPROGRAMMED (unknown)
            //grabberSpin =       1 ==  Passoff position
            //placer =            0 ==  open
            //placerSpin =        1 ==  Drop position







            if (gamepad1.a) {
                grabber.setPosition(0.8);
                grabberSpin.setPosition(0);//TODO: test for if 0 or 1, should be facing down
            }

            if (gamepad1.b) {
                grabber.setPosition(0.4);//TODO: test to see if 0 or 1, should be closed
                sleep(300);
                grabberSpin.setPosition(0.98);//TODO: opposite than a
                placer.setPosition(1);
                placerSpin.setPosition(0.3);
                sleep(1000);
                placer.setPosition(0);
            }

            if (gamepad1.x) {
                placerSpin.setPosition(0.3);//TODO: test to see if 0 or 1, should position to grab from grabber
                sleep(1000);
                placer.setPosition(1);//TODO: test to see if 0 or 1, should grab from grabber
                sleep(1000);
                grabber.setPosition(1);//TODO: opposite of b, should open
                sleep(1000);
                placerSpin.setPosition(1);//TODO: test to see if goes to place
            }

            if (gamepad1.y) {
                placerSpin.setPosition(1);
            }
            if (gamepad1.left_bumper) {
                placer.setPosition(0);
            }

            telemetry.addData("speed", speed);

            if (gamepad2.right_trigger != 0) {

            } else if (gamepad2.left_trigger != 0) {

            } else {

            }

            // plane
            if (gamepad1.y && gamepad2.y) {
                //
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    telemetry.addData("error", e);
                }
                //
            }

            if (gamepad2.left_bumper) {

            } else if (gamepad2.right_bumper) {

            } else {

            }


            time = myStopwatch.time(TimeUnit.SECONDS);

            telemetry.addData("Stopwatch timer: ", myStopwatch.time(TimeUnit.SECONDS));

            telemetry.update();
        }
    }
}