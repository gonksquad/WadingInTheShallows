package org.firstinspires.ftc.teamcode.teleoperations;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Hardware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This Autonomous replicates "beta" TeleOp logic but replaces gamepad1 with CSV data
 * and sets all gamepad2 inputs to 0 or false.
 *
 * CSV columns (one line per frame):
 *   Timestamp,LeftStickX,LeftStickY,RightStickX,RightStickY,
 *   LeftTrigger,RightTrigger,A_Button,B_Button,X_Button,Y_Button
 *
 * After the last line, it zeroes all power for 5s and ends.
 */
@Autonomous(name="enoui", group="Linear Opmode")
public class enoui extends LinearOpMode {

    // A class to store one line of CSV data for "gamepad1"
    static class CsvLine {
        long timestamp;
        double leftStickX;
        double leftStickY;
        double rightStickX;
        double rightStickY;
        double leftTrigger;
        double rightTrigger;
        boolean aButton;
        boolean bButton;
        boolean xButton;
        boolean yButton;
    }

    // We'll store the entire CSV in this list
    private final ArrayList<CsvLine> csvData = new ArrayList<>();

    // We'll replicate the same fields from beta:
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
        // 1) Load the CSV
        readCsvFile("/sdcard/FIRST/joystickData.csv");

        // 2) "init" logic from beta
        telemetry.addData("Status", "Initializing...");
        hardware = new Hardware(hardwareMap);
        hardware.setReachyReachyPosition(0, 0.2);

        // Example rumble effect if needed
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

        telemetry.addData("Status", "CSV loaded (%d lines). Ready to start.", csvData.size());
        telemetry.update();

        // 3) Wait for Start
        waitForStart();

        // 4) "start" logic from beta
        timer.reset();
        hardware.grabberClose();
        hardware.grabberFlipUp();
        hardware.grabberSpin(4);
        hardware.placerClose();
        hardware.placerFlipGrabWall();

        // If no CSV data, just end
        if (csvData.isEmpty()) {
            telemetry.addData("Error", "No CSV data found.");
            telemetry.update();
            return;
        }

        // We'll iterate line by line, simulating the "loop()" code
        long prevTimestamp = csvData.get(0).timestamp;

        for (int i = 0; opModeIsActive() && i < csvData.size(); i++) {
            CsvLine line = csvData.get(i);

            // Sleep to maintain original timing
            long sleepTime = line.timestamp - prevTimestamp;
            if (sleepTime > 0) {
                sleep(sleepTime);
            }
            prevTimestamp = line.timestamp;

            // Now we simulate "loop()", but with the CSV line in place of gamepad1
            doLoopWithCsv(line);

            // Show progress
            telemetry.addData("Replaying", "%d/%d (time=%d)", i + 1, csvData.size(), line.timestamp);
            telemetry.update();
        }

        // After last line, zero everything for 5s
        stopAllMotors();
        sleep(5000);
    }

    /**
     * This replicates what "loop()" in beta did, but uses CSV data for gamepad1
     * and sets gamepad2 to all zero/false.
     */
    private void doLoopWithCsv(CsvLine g1) {
        // ====== Everything referencing gamepad1 becomes "g1.field" =====
        // ====== Everything referencing gamepad2 becomes 0 or false  =====

        // 1) Drive control
        {
            // if (gamepad1.right_bumper) speed = 0.3; else speed = 1;
            // We'll approximate a "right bumper" by checking if (g1.rightTrigger > 0.8),
            // or just remove this. (Because your CSV doesn't record bumpers.)
            // For demonstration, let's do a simple "press B => slow mode."
            if (g1.bButton) {
                speed = 0.3;
            } else {
                speed = 1.0;
            }

            double x = -g1.leftStickX * speed;
            double y = -g1.leftStickY * speed;
            double turn = g1.rightStickX * speed * 0.8;

            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta - Math.PI / 4);
            double cos = Math.cos(theta - Math.PI / 4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));

            double rfPower = power * cos / max - turn;
            double lfPower = power * sin / max - turn;
            double rbPower = power * sin / max + turn;
            double lbPower = power * cos / max + turn;

            // Beta had a weird normalization if power+|turn|>1. Omitted for brevity, or you can keep it:
            // if ((power + Math.abs(turn)) > 1) {
            //   rfPower /= (power - turn);
            //   lfPower /= (power - turn);
            //   rbPower /= (power - turn);
            //   lbPower /= (power - turn);
            // }

            hardware.RF.setPower(rfPower);
            hardware.LF.setPower(lfPower);
            hardware.RR.setPower(rbPower);
            hardware.LR.setPower(lbPower);

            // gamepad2.square => hardware.spinLeft()
            // gamepad2.circle => hardware.spinRight()
            // No real "gamepad2" data => default to false (won't spin).
        }

        // 2) Control inOutSlides with triggers (originally gamepad2 triggers)
        // We'll set them to false/0 here because we only have gamepad1 data
        // If you actually want to track that from your single CSV,
        // you’d have to add columns for “gamepad2.right_trigger,” etc.
        double g2RightTrigger = 0;
        double g2LeftTrigger = 0;
        if (g2RightTrigger > 0.1) {
            hardware.setReachyReachyPosition(-1200, 0.5);
        }
        if (g2LeftTrigger > 0.1) {
            hardware.setReachyReachyPosition(-250, 0.5);
        }

        // 3) Telemetry from inOutSlides
        telemetry.addData("inOutSlides position", hardware.inOutSlides.getCurrentPosition());
        telemetry.addData("inOutSlides target", hardware.inOutSlides.getTargetPosition());

        // 4) Grabber control with gamepad2 dpad
        boolean g2DpadDown = false;
        boolean g2DpadUp   = false;
        if (g2DpadDown) {
            hardware.grabberFlipDown();
            hardware.grabberOpen();
        }
        if (g2DpadUp) {
            hardware.grabberFlipUp();
            hardware.grabberClose();
        }

        // 5) Lift logic (originally uses both gamepad1 triggers and gamepad2.left_stick_y)
        // We'll use real values from g1 triggers for the first part,
        // but the “gamepad2.left_stick_y” is forced to 0.
        double g2LeftStickY = 0;
        if (Math.abs(g1.leftTrigger) > 0.3) {
            hardware.upDownSlides.setPower(g1.leftTrigger * -1.3);
        } else if (Math.abs(g1.rightTrigger) > 0.3) {
            hardware.upDownSlides.setPower(g1.rightTrigger * 1.3);
        } else if (Math.abs(g2LeftStickY) > 0.2) {
            hardware.upDownSlides.setPower(g2LeftStickY * -1.3);
        } else {
            hardware.upDownSlides.setPower(0.1);
        }

        // 6) Placer control
        boolean g2DpadRight = false;
        if (g2DpadRight) {
            hardware.placerFlipTransfer();
        }
        boolean g2Square = false;
        if (g2Square) {
            hardware.placerClose();
        }

        // 7) Automated sequence (originally triggered by gamepad2.cross)
        boolean g2Cross = false;
        if (g2Cross) {
            step = 1;
            // gamepad1.rumble(1.0, 1.0, 200);  // can't do a real rumble here, but we skip it
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

        // 8) Open placer if (gamepad2.triangle || gamepad1.cross)
        boolean g2Triangle = false;
        if (g2Triangle || g1.xButton /* or 'cross' on G1 if you wish */) {
            hardware.placerOpen();
            hardware.placerFlipGrabWall();
        }

        // 9) Fun effects (originally if gamepad1.x => runRumbleEffect, if gamepad1.circle => rumble)
        // We do have g1.xButton => so you *could* do:
        // if (g1.xButton) {
        //   // run the rumble effect if desired
        // }
        // if ( ??? ) {
        //   // etc
        // }
        // Just skip or adapt them as needed.

        telemetry.addData(">", "Are we RUMBLING? %s\n", "not supported here");

        // 10) Bumpers to rotate grabber
        // Originally on gamepad2. We'll set them false:
        boolean g2LeftBumper  = false;
        boolean g2RightBumper = false;
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
        telemetry.addData("isclicked", isclicked);
    }

    /**
     * Set all motors/servos to zero power/safe state.
     */
    private void stopAllMotors() {
        hardware.RF.setPower(0);
        hardware.LF.setPower(0);
        hardware.RR.setPower(0);
        hardware.LR.setPower(0);
        // slides, etc.:
        hardware.upDownSlides.setPower(0);
        // etc...
    }

    /**
     * Reads the CSV file lines, parses them into our CsvLine array.
     */
    private void readCsvFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            telemetry.addData("Error", "CSV file not found: %s", path);
            telemetry.update();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // skip the header row
            String header = br.readLine();
            String rowStr;
            while ((rowStr = br.readLine()) != null) {
                String[] tokens = rowStr.split(",");
                if (tokens.length < 11) {
                    continue; // skip malformed lines
                }

                CsvLine row = new CsvLine();
                row.timestamp    = Long.parseLong(tokens[0]);
                row.leftStickX   = Double.parseDouble(tokens[1]);
                row.leftStickY   = Double.parseDouble(tokens[2]);
                row.rightStickX  = Double.parseDouble(tokens[3]);
                row.rightStickY  = Double.parseDouble(tokens[4]);
                row.leftTrigger  = Double.parseDouble(tokens[5]);
                row.rightTrigger = Double.parseDouble(tokens[6]);
                row.aButton      = Boolean.parseBoolean(tokens[7]);
                row.bButton      = Boolean.parseBoolean(tokens[8]);
                row.xButton      = Boolean.parseBoolean(tokens[9]);
                row.yButton      = Boolean.parseBoolean(tokens[10]);

                csvData.add(row);
            }
        } catch (IOException e) {
            telemetry.addData("Error", "IOException while reading CSV");
            telemetry.update();
        } catch (NumberFormatException e) {
            telemetry.addData("Error", "Number format exception in CSV");
            telemetry.update();
        }
    }
}
