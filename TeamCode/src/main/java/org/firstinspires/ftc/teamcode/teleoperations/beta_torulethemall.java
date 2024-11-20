package org.firstinspires.ftc.teamcode.teleoperations;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware;

/**
 * **Gamepad 1 Controls:**
 * - **Left Stick (X and Y):** Controls robot movement (translation).
 * - **Right Stick X:** Controls robot turning.
 * - **Right Trigger:**
 * - Reduces robot speed when pressed.
 * - Moves the inOutSlides to position -1000 when pressed beyond a threshold (>
 * 0.1).
 * - **Left Trigger:**
 * - Moves the inOutSlides to position -100 when pressed beyond a threshold (>
 * 0.1).
 * - **X Button:**
 * - Activates a custom rumble effect.
 * - Sets the gamepad LED color to white for 1 second.
 * - **Circle Button:** Rumbles both motors at full power for 200 milliseconds.
 *
 * **Gamepad 2 Controls:**
 * - **D-pad Up:** Flips the grabber up and closes it.
 * - **D-pad Down:** Flips the grabber down and opens it.
 * - **Left Stick Y:** Controls the up/down slides (lift), with upward movement
 * mapped to negative power.
 * - **D-pad Right:** Flips the placer down.
 * - **Square Button:**
 * - Closes the placer.
 * - Waits for 200 milliseconds.
 * - Flips the placer up.
 * - **Cross Button:** Starts an automated sequence for object placement.
 * - **Y Button:** Opens the placer.
 */
@TeleOp()
public class beta_torulethemall extends OpMode {
    // Declare OpMode members.
    private ElapsedTime timer = new ElapsedTime();
    private int step = 0;
    public double speed = 1;
    public Gamepad.RumbleEffect customRumbleEffect;
    public Hardware hardware;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");

        // Initialize hardware
        hardware = new Hardware(hardwareMap);

        customRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, 1.0, 500) // Rumble right motor 100% for 500 mSec
                .addStep(0.0, 0.0, 300) // Pause for 300 mSec
                .addStep(1.0, 0.0, 250) // Rumble left motor 100% for 250 mSec
                .addStep(0.0, 0.0, 250) // Pause for 250 mSec
                .addStep(1.0, 0.0, 250) // Rumble left motor 100% for 250 mSec
                .build();

        telemetry.addData("Status", "Initialized.");
    }

    @Override
    public void start() {
        timer.reset();
        hardware.grabberClose();
        hardware.grabberFlipUp();
        hardware.grabberSpin(4);
        hardware.placerOpen();
        hardware.placerFlipDown();

    }

    @Override
    public void loop() {
        // Drive control
        {
            speed = (-1 * gamepad1.right_trigger) + 1;

            double x = gamepad2.left_stick_x * speed;
            double y = gamepad2.left_stick_y * speed;
            double turn = gamepad2.right_stick_x * speed;

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
        }

        // Control inOutSlides with triggers
        if (gamepad2.right_trigger > 0.1) {
            hardware.setReachyReachyPosition(-1000, 0.5);
        }

        if (gamepad2.left_trigger > 0.1) {
            hardware.setReachyReachyPosition(-350, 0.5);
        }

        telemetry.addData("inOutSlides position", hardware.inOutSlides.getCurrentPosition());
        telemetry.addData("inOutSlides target", hardware.inOutSlides.getTargetPosition());

        // Grabber control
        if (gamepad2.dpad_down) {
            hardware.grabberFlipDown();
            hardware.grabberOpen();
        }

        if (gamepad2.dpad_up) {
            hardware.grabberFlipUp();
            hardware.grabberClose();
        }

        // Lift logic
        if (Math.abs(gamepad2.right_stick_y) > 0.2) {
            hardware.upDownSlides.setPower(gamepad2.right_stick_y * -1.3);
        } else {
            hardware.upDownSlides.setPower(0.1);
        }

        // Placer control
        if (gamepad2.dpad_right) {
            hardware.placerFlipDown();
        }

        if (gamepad2.square) {
            hardware.placerClose();
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hardware.placerFlipUp();
        }

        // Automated sequence
        if (gamepad2.cross) {
            step = 1;
        }

        if (step == 1) {
            hardware.grabberClose();
            hardware.placerOpen();
            hardware.placerFlipDown();
            timer.reset();
            step++;
        }

        if (step == 2 && timer.milliseconds() >= 500) {
            hardware.grabberFlipUp();
            hardware.grabberCorrection.setPosition(0.6);
            timer.reset();
            step++;
        }

        if (step == 3 && timer.milliseconds() >= 3000) {
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
            hardware.placerFlipUp();
            step = 0;
        }

        // Open placer
        if (gamepad2.y) {
            hardware.placerOpen();
        }

        // Fun effects
        if (gamepad1.x) {
            gamepad1.runRumbleEffect(customRumbleEffect);
            gamepad1.setLedColor(255, 255, 255, 1000);
        }
        if (gamepad1.circle) {
            gamepad1.rumble(1.0, 1.0, 200);
        }
        telemetry.addData(">", "Are we RUMBLING? %s\n", gamepad1.isRumbling() ? "YES" : "no");
    }
}
