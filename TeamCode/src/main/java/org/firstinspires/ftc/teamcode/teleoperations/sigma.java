package org.firstinspires.ftc.teamcode.teleoperations;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp()
@Disabled
public class sigma extends OpMode {
    // Declare OpMode members.
    private ElapsedTime timer = new ElapsedTime();
    private int step = 0;
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
    public int setServo = 1;
    private int targetPosition = 0; // Target position for the motor


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


        //TODO: TEST to see if chatgpt did a good job or not
        inOutSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        inOutSlides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//or RUN_TO_POSITION
        inOutSlides.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
    }

    //grabber =           1   ==  open
    //grabberCorrection = 0.5 ==  vertical orientation grab
    //grabberSpin =       1   ==  Passoff position
    //placer =            0   ==  open
    //placerSpin =        1   ==  Drop position

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
        if(setServo == 1){
            grabber.setPosition(0.54);
            grabberSpin.setPosition(1);
            grabberCorrection.setPosition(0.6);
            placer.setPosition(0.5);
            placerSpin.setPosition(0.2);
            setServo = 2;
        }
        // grabber = 1 == open
        // grabberCorrection = 1 == UNPROGRAMMED (unknown)
        // grabberSpin = 1 == Passoff position
        // placer = 0 == open
        // placerSpin = 1 == Drop position

//        // horizontal retraction driven by left trigger
//        inOutSlides.setPower(gamepad2.left_trigger);
//        // horizontal extension driven by right trigger
//        inOutSlides.setPower(-gamepad2.right_trigger);


        //TODO: TEST to see if chatgpt did a good job or not
        //Check if the right trigger is pressed
        if (gamepad2.right_trigger > 0.1) {
            targetPosition = 1000;
            inOutSlides.setTargetPosition(targetPosition);
            inOutSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            inOutSlides.setPower(0.5);
        }

        // Check if the left trigger is pressed
        if (gamepad2.left_trigger > 0.1) {
            targetPosition = 100;
            inOutSlides.setTargetPosition(targetPosition);
            inOutSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            inOutSlides.setPower(0.5);
        }

        telemetry.addData("inOutSlides position", inOutSlides.getCurrentPosition());
        telemetry.addData("TargetPose : ", targetPosition);

        if(gamepad2.dpad_down){
            grabberSpin.setPosition(0);
            grabber.setPosition(1);
        }
        if(gamepad2.dpad_up){
            grabberSpin.setPosition(1);
            grabber.setPosition(0.56);
        }

        //lift logic
        if(gamepad2.left_stick_y>0.1){
            // vertical extension driven by left stick y
            upDownSlides.setPower(gamepad2.left_stick_y * -1.1);
        }else if(gamepad2.left_stick_y<0.1){
            upDownSlides.setPower(gamepad2.left_stick_y * -1.1);
        }else{
            upDownSlides.setPower(0.1);
        }

        // placer grabs sample off wall
        if(gamepad2.dpad_right){
            placerSpin.setPosition(1);//TODO: NEW SERVO FIX
        }
        if (gamepad2.square) {
            placer.setPosition(1);
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            placerSpin.setPosition(0.7);//TODO: NEW SERVO FIX
        }

        // pick up specimen when a button tapped AND TRADEOFF
//        if (gamepad2.cross) {
//            grabber.setPosition(0.54);
//            placer.setPosition(0.5);
//            placerSpin.setPosition(0.2);//TODO: NEW SERVO FIX
//            try {
//                sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            grabberSpin.setPosition(1);
//            grabberCorrection.setPosition(0.6);
//            try {
//                sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            placer.setPosition(1);
//            try {
//                sleep(400);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            grabber.setPosition(1);
//            try {
//                sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            placerSpin.setPosition(1);//TODO: NEW SERVO FIX
//        }
        if (gamepad2.cross) {
            // Run the sequence only once when button is pressed
            step = 1;
        }

        if (step == 1) {
            grabber.setPosition(0.54);
            placer.setPosition(0.5);
            placerSpin.setPosition(0.175);
            timer.reset(); // Start timing for the next step
            step++;
        }

        if (step == 2 && timer.milliseconds() >= 400) {
            grabberSpin.setPosition(1);
            grabberCorrection.setPosition(0.6);
            timer.reset();
            step++;
        }

        if (step == 3 && timer.milliseconds() >= 1000) {
            placer.setPosition(1);
            timer.reset();
            step++;
        }

        if (step == 4 && timer.milliseconds() >= 400) {
            grabber.setPosition(1);
            timer.reset();
            step++;
        }

        if (step == 5 && timer.milliseconds() >= 300) {
            placerSpin.setPosition(0.8);
            step = 0; // Reset step counter to allow re-triggering of sequence
        }
        //open placer
        if(gamepad2.y){
            placer.setPosition(0.5);
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
