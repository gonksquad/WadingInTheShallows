package org.firstinspires.ftc.teamcode.autonomous.topsecret;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Hardware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A single LinearOpMode that:
 * 1) Initializes the same hardware as "beta" TeleOp.
 * 2) During run, drives the robot from real-time gamepad1/gamepad2 inputs.
 * 3) Simultaneously records *all* those inputs to /sdcard/FIRST/joystickData.csv.
 *
 * After time expires (RECORDING_DURATION), it stops and closes the CSV file.
 */
@Autonomous(name="nostalgia", group="Linear Opmode")
public class nostalgia extends LinearOpMode {

    private static final int RECORDING_DURATION = 35;   // Record/drive for 35 seconds
    private static final int FRAME_RATE = 30;           // 30 FPS
    private static final long FRAME_INTERVAL = 1000 / FRAME_RATE; // ms

    private ElapsedTime timer = new ElapsedTime();
    private int step = 0;
    public double speed = 1;
    public Gamepad.RumbleEffect customRumbleEffect;
    public Hardware hardware;
    public int grabberrot;
    public boolean isclicked;
    public boolean rightisclicked;

    @Override
    public void runOpMode() {
        // -----------------------
        // 1) Hardware Init
        // -----------------------
        telemetry.addData("Status", "Initializing hardware...");
        hardware = new Hardware(hardwareMap);
        hardware.setReachyReachyPosition(0, 0.2);

        // If you want the same customRumbleEffect from beta:
        customRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, 1.0, 500)
                .addStep(0.0, 0.0, 300)
                .addStep(1.0, 0.0, 250)
                .addStep(0.0, 0.0, 250)
                .addStep(1.0, 0.0, 250)
                .build();

        grabberrot = 1;
        isclicked = false;
        rightisclicked = false;

        telemetry.addData("Status", "Hardware init done.");
        telemetry.update();

        // -----------------------
        // 2) Prepare CSV file
        // -----------------------
        File file = new File("/sdcard/FIRST/joystickData.csv");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            // Write a header row. For instance, everything from beta:
            fileWriter.write(
                    "Timestamp," +
                            // gamepad1
                            "G1_LeftStickX,G1_LeftStickY,G1_RightStickX,G1_RightStickY,G1_LeftTrigger,G1_RightTrigger," +
                            "G1_LeftBumper,G1_RightBumper,G1_A,G1_B,G1_X,G1_Y,G1_DpadUp,G1_DpadDown,G1_DpadLeft,G1_DpadRight," +
                            // gamepad2
                            "G2_LeftStickX,G2_LeftStickY,G2_RightStickX,G2_RightStickY,G2_LeftTrigger,G2_RightTrigger," +
                            "G2_LeftBumper,G2_RightBumper,G2_Square,G2_Circle,G2_Cross,G2_Triangle," +
                            "G2_DpadUp,G2_DpadDown,G2_DpadLeft,G2_DpadRight\n"
            );
        } catch (IOException e) {
            telemetry.addData("Error", "FileWriter init failed: " + e.getMessage());
            telemetry.update();
            return; // If we can't open the file, no point in continuing
        }

        telemetry.addData("Status", "CSV open, waiting for start.");
        telemetry.update();

        // -----------------------
        // 3) Wait for Start
        // -----------------------
        waitForStart();

        // -----------------------
        // 4) "start()" logic from beta
        // -----------------------
        timer.reset();
        hardware.grabberClose();
        hardware.grabberFlipUp();
        hardware.grabberSpin(4);
        hardware.placerClose();
        hardware.placerFlipGrabWall();

        long startTime = System.currentTimeMillis();

        // -----------------------
        // 5) Main Loop
        // -----------------------
        while (opModeIsActive() &&
                (System.currentTimeMillis() - startTime) < RECORDING_DURATION * 1000) {

            long timestamp = System.currentTimeMillis() - startTime;

            // A) Read ALL relevant gamepad1 inputs
            double g1LSX = gamepad1.left_stick_x;
            double g1LSY = gamepad1.left_stick_y;
            double g1RSX = gamepad1.right_stick_x;
            double g1RSY = gamepad1.right_stick_y;
            double g1LT  = gamepad1.left_trigger;
            double g1RT  = gamepad1.right_trigger;

            boolean g1LeftBumper  = gamepad1.left_bumper;
            boolean g1RightBumper = gamepad1.right_bumper;
            boolean g1A = gamepad1.a;
            boolean g1B = gamepad1.b;
            boolean g1X = gamepad1.x;
            boolean g1Y = gamepad1.y;
            boolean g1DpadUp    = gamepad1.dpad_up;
            boolean g1DpadDown  = gamepad1.dpad_down;
            boolean g1DpadLeft  = gamepad1.dpad_left;
            boolean g1DpadRight = gamepad1.dpad_right;

            // B) Read ALL relevant gamepad2 inputs
            double g2LSX = gamepad2.left_stick_x;
            double g2LSY = gamepad2.left_stick_y;
            double g2RSX = gamepad2.right_stick_x;
            double g2RSY = gamepad2.right_stick_y;
            double g2LT  = gamepad2.left_trigger;
            double g2RT  = gamepad2.right_trigger;

            boolean g2LeftBumper  = gamepad2.left_bumper;
            boolean g2RightBumper = gamepad2.right_bumper;
            boolean g2Square   = gamepad2.square;
            boolean g2Circle   = gamepad2.circle;
            boolean g2Cross    = gamepad2.cross;
            boolean g2Triangle = gamepad2.triangle;
            boolean g2DpadUp    = gamepad2.dpad_up;
            boolean g2DpadDown  = gamepad2.dpad_down;
            boolean g2DpadLeft  = gamepad2.dpad_left;
            boolean g2DpadRight = gamepad2.dpad_right;

            // ----------------------------------------------------------------
            // C) EXACT "beta" TeleOp Logic (using the above variables)
            // ----------------------------------------------------------------

            // -- Drive control
            if (g1RightBumper) {
                speed = 0.3;
            } else {
                speed = 1;
            }
            double x = -g1LSX * speed;
            double y = -g1LSY * speed;
            double turn = g1RSX * speed * 0.8;

            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta - Math.PI / 4);
            double cos = Math.cos(theta - Math.PI / 4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));

            double rfPower = power * cos / max - turn;
            double lfPower = power * sin / max - turn;
            double rbPower = power * sin / max + turn;
            double lbPower = power * cos / max + turn;

            if ((power + Math.abs(turn)) > 1) {
                rfPower /= (power - turn);
                lfPower /= (power - turn);
                rbPower /= (power - turn);
                lbPower /= (power - turn);
            }
            hardware.RF.setPower(rfPower);
            hardware.LF.setPower(lfPower);
            hardware.RR.setPower(rbPower);
            hardware.LR.setPower(lbPower);

            // spinLeft/spinRight (gamepad2 square/circle)
            if (g2Square) {
                hardware.spinLeft();
            } else if (g2Circle) {
                hardware.spinRight();
            }

            // inOutSlides with triggers (gamepad2)
            if (g2RT > 0.1) {
                hardware.setReachyReachyPosition(-1200, 0.5);
            }
            if (g2LT > 0.1) {
                hardware.setReachyReachyPosition(-250, 0.5);
            }

            telemetry.addData("inOutSlides pos", hardware.inOutSlides.getCurrentPosition());
            telemetry.addData("inOutSlides tgt", hardware.inOutSlides.getTargetPosition());

            // Grabber (gamepad2 dpad)
            if (g2DpadDown) {
                hardware.grabberFlipDown();
                hardware.grabberOpen();
            }
            if (g2DpadUp) {
                hardware.grabberFlipUp();
                hardware.grabberClose();
            }

            // Lift logic (combo of g1 triggers and g2 left stick)
            if (Math.abs(g1LT) > 0.3) {
                hardware.upDownSlides.setPower(g1LT * -1.3);
            } else if (Math.abs(g1RT) > 0.3) {
                hardware.upDownSlides.setPower(g1RT * 1.3);
            } else if (Math.abs(g2LSY) > 0.2) {
                hardware.upDownSlides.setPower(g2LSY * -1.3);
            } else {
                hardware.upDownSlides.setPower(0.1);
            }

            // Placer (gamepad2 dpad_right / square)
            if (g2DpadRight) {
                hardware.placerFlipTransfer();
            }
            if (g2Square) {
                hardware.placerClose();
            }

            // Automated sequence (gamepad2 cross)
            if (g2Cross) {
                step = 1;
                gamepad1.rumble(1.0, 1.0, 200);
            }
            if (step == 1) {
                hardware.grabberClose();
                hardware.placerOpen();
                hardware.placerFlipTransfer();
                timer.reset();
                step++;
            }
            if (step == 2 && timer.milliseconds() >= 500) {
                hardware.grabberFlipUp();
                grabberrot = 3;
                hardware.setReachyReachyPosition(-250, 1);
                timer.reset();
                step++;
            }
            if (step == 3 && timer.milliseconds() >= 1000) {
                hardware.placerClose();
                timer.reset();
                step++;
            }
            if (step == 4 && timer.milliseconds() >= 400) {
                hardware.grabberOpen();
                timer.reset();
                step++;
            }
            if (step == 5 && timer.milliseconds() >= 300) {
                hardware.placerFlipPlace();
                step = 0;
            }

            // Open placer if (gamepad2 triangle OR gamepad1 cross?)
            if (g2Triangle || g1B) {
                hardware.placerOpen();
                hardware.placerFlipGrabWall();
            }

            // Bumpers to rotate grabber (gamepad2)
            if (g2LeftBumper && !isclicked) {
                isclicked = true;
                if (grabberrot < 4) {
                    grabberrot++;
                } else {
                    grabberrot = 1;
                }
            } else if (!g2LeftBumper) {
                isclicked = false;
            }
            if (g2RightBumper && !rightisclicked) {
                rightisclicked = true;
                if (grabberrot > 1) {
                    grabberrot--;
                } else {
                    grabberrot = 4;
                }
            } else if (!g2RightBumper) {
                rightisclicked = false;
            }

            hardware.grabberSpin(grabberrot);
            telemetry.addData("grabberrot", grabberrot);

            // ----------------------------------------------------------------
            // D) WRITE to CSV
            // ----------------------------------------------------------------
            try {
                fileWriter.write(
                        timestamp + "," +
                                // gamepad1
                                g1LSX + "," + g1LSY + "," + g1RSX + "," + g1RSY + "," +
                                g1LT  + "," + g1RT  + "," +
                                g1LeftBumper + "," + g1RightBumper + "," +
                                g1A + "," + g1B + "," + g1X + "," + g1Y + "," +
                                g1DpadUp + "," + g1DpadDown + "," + g1DpadLeft + "," + g1DpadRight + "," +
                                // gamepad2
                                g2LSX + "," + g2LSY + "," + g2RSX + "," + g2RSY + "," +
                                g2LT  + "," + g2RT  + "," +
                                g2LeftBumper + "," + g2RightBumper + "," +
                                g2Square + "," + g2Circle + "," + g2Cross + "," + g2Triangle + "," +
                                g2DpadUp + "," + g2DpadDown + "," + g2DpadLeft + "," + g2DpadRight +
                                "\n"
                );
            } catch (IOException e) {
                telemetry.addData("Error", "File write failed: " + e.getMessage());
            }

            telemetry.update();
            sleep(FRAME_INTERVAL);
        }

        // -----------------------
        // 6) End: close file, stop motors
        // -----------------------
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            telemetry.addData("Error", "File close failed: " + e.getMessage());
        }

        // Set motors safe
        hardware.RF.setPower(0);
        hardware.LF.setPower(0);
        hardware.RR.setPower(0);
        hardware.LR.setPower(0);
        hardware.upDownSlides.setPower(0);

        telemetry.addData("Status", "Done! CSV closed.");
        telemetry.update();
    }
}
