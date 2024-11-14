package org.firstinspires.ftc.teamcode.teleoperations;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "ALPHA", group = "Ronan Presents:")
public class alpha extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor LF;
    public DcMotor LB;
    public DcMotor RB;
    public DcMotor RF;
    public DcMotor upDownSlides;
    public DcMotor inOutSlides;
    public Servo grabber;
    public Servo placer;
    public Servo placerSpin;
    public Servo grabberCorrection;
    public Servo grabberSpin;
    public double speed = 1;
    public Gamepad.RumbleEffect customRumbleEffect;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        LF = hardwareMap.get(DcMotor.class, "2");
        LB = hardwareMap.get(DcMotor.class, "0");
        RB = hardwareMap.get(DcMotor.class, "1");
        RF = hardwareMap.get(DcMotor.class, "3");

        RB.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        upDownSlides = hardwareMap.get(DcMotor.class, "E0");
        inOutSlides = hardwareMap.get(DcMotor.class, "E1");

        grabber = hardwareMap.get(Servo.class, "ES0");
        placer = hardwareMap.get(Servo.class, "ES1");
        placerSpin = hardwareMap.get(Servo.class, "ES2");
        grabberCorrection = hardwareMap.get(Servo.class, "ES3");
        grabberSpin = hardwareMap.get(Servo.class, "ES4");

        customRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, 1.0, 500) // Rumble right motor 100% for 500 mSec
                .addStep(0.0, 0.0, 300) // Pause for 300 mSec
                .addStep(1.0, 0.0, 250) // Rumble left motor 100% for 250 mSec
                .addStep(0.0, 0.0, 250) // Pause for 250 mSec
                .addStep(1.0, 0.0, 250) // Rumble left motor 100% for 250 mSec
                .build();

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // drive control
        {
            speed = (-1 * gamepad1.right_trigger) + 1;

            double x = gamepad1.left_stick_x * speed;
            double y = gamepad1.left_stick_y * speed;
            double turn = gamepad1.right_stick_x * speed;

            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta - Math.PI / 4);
            double cos = Math.cos(theta - Math.PI / 4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));
            RF.setPower(power * cos / max - turn);
            LF.setPower(power * sin / max - turn);
            RB.setPower(power * sin / max + turn);
            LB.setPower(power * cos / max + turn);
            if ((power + Math.abs(turn)) > 1) {
                LF.setPower((LF.getPower()) / (power - turn));
                RF.setPower((RF.getPower()) / (power + turn));
                RB.setPower((RB.getPower()) / (power - turn));
                LB.setPower((LB.getPower()) / (power + turn));
            }
        }

        // grabber = 1 == open
        // grabberCorrection = 1 == UNPROGRAMMED (unknown)
        // grabberSpin = 1 == Passoff position
        // placer = 0 == open
        // placerSpin = 1 == Drop position

        // horizontal retraction driven by left trigger
        inOutSlides.setPower(gamepad2.left_trigger);
        // horizontal extension driven by right trigger
        inOutSlides.setPower(-gamepad2.right_trigger);

        // vertical extension driven by left stick y
        upDownSlides.setPower(gamepad2.left_stick_y * -1.1);

        // align grabber when x button tapped
        if (gamepad2.square) {
            grabberSpin.setPosition(0);
            grabberCorrection.setPosition(1);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ;
            grabber.setPosition(0.5);
        }

        // pick up specimen when a button tapped
        if (gamepad2.cross) {
            placer.setPosition(0);
            grabberSpin.setPosition(1);
            grabberCorrection.setPosition(0.5);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ;
            placer.setPosition(1);
            grabber.setPosition(1);
        }

        // fun
        if (gamepad1.x) {
            gamepad1.runRumbleEffect(customRumbleEffect);
            gamepad1.setLedColor(255, 255, 255, 1000);
        }
        if (gamepad1.circle) {
            gamepad1.rumble(1.0, 1.0, 200);
        }
        telemetry.addData(">", "Are we RUMBLING? %s\n", gamepad1.isRumbling() ? "YES" : "no");
    }

    @Override
    public void stop() {
    }

}
