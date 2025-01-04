package org.firstinspires.ftc.teamcode.autonomous;

import android.media.MediaPlayer;
import android.content.Context;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;

/**
 * @Autonomous(name="Choppa Autonomous", group="Autonomous")
 *
 * **Choppa Autonomous OpMode:**
 * - Initializes robot hardware and MediaPlayer.
 * - Executes a simple autonomous routine:
 *   1. Move forward for 2 seconds.
 *   2. Turn right for 1 second.
 *   3. Play 'choppa.mp3' sound.
 *   4. Continue turning at reduced power for 2 seconds.
 *   5. Stop all movements.
 * - Provides rigorous debugging through telemetry.
 */
@Autonomous(name = "Choppa Autonomous", group = "Autonomous")
public class ChoppaAutonomous extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    // Motors

    // MediaPlayer
    private MediaPlayer mediaPlayer = null;
    private boolean isSoundPlayed = false; // Flag to ensure sound is played only once

    @Override
    public void runOpMode() {
        // Initialization phase
        telemetry.addData("Status", "Initializing Choppa Autonomous...");
        telemetry.update();

        // Set all motors to zero power initially
//        stopAllMotors();

        telemetry.addData("Hardware", "Motors Initialized.");
        telemetry.update();

        // Initialize MediaPlayer with choppa.mp3
        try {
            mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.choppa);
            if (mediaPlayer == null) {
                telemetry.addData("MediaPlayer", "Failed to initialize. Check the sound file.");
            } else {
                telemetry.addData("MediaPlayer", "Initialized successfully.");
            }
        } catch (Exception e) {
            telemetry.addData("MediaPlayer", "Exception: " + e.getMessage());
            Log.e("ChoppaAutonomous", "MediaPlayer Exception", e);
        }
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();
        waitForStart();
        runtime.reset();

        telemetry.addData("Status", "Autonomous Started");
        telemetry.update();

        // Autonomous Steps

        // Step 1: Move Forward for 2 seconds
//        moveForward(0.5); // 50% power
        telemetry.addData("Step", "1: Moving Forward");
        telemetry.update();
        sleep(2000); // 2 seconds

        // Step 2: Turn Right for 1 second
//        turnRight(0.5); // 50% power
        telemetry.addData("Step", "2: Turning Right");
        telemetry.update();
        sleep(1000); // 1 second

        // Step 3: Play 'choppa.mp3' sound
        if (mediaPlayer != null && !isSoundPlayed) {
            mediaPlayer.start();
            isSoundPlayed = true;
            telemetry.addData("Step", "3: Playing choppa.mp3");
            telemetry.update();
        } else if (mediaPlayer == null) {
            telemetry.addData("Step", "3: MediaPlayer not initialized.");
            telemetry.update();
        }

        // Step 4: Continue turning right at reduced power for 2 seconds
//        turnRight(0.3); // 30% power
        telemetry.addData("Step", "4: Continuing to Turn Right with Sound");
        telemetry.update();
        sleep(2000); // 2 seconds

        // Step 5: Stop all movements
//        stopAllMotors();
        telemetry.addData("Step", "5: Stopping All Motors");
        telemetry.update();

        // Final Telemetry
        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();

        // Ensure the OpMode doesn't terminate immediately to allow telemetry to display
        while (opModeIsActive()) {
            idle();
        }

        // Cleanup resources after OpMode is inactive
        cleanup();
    }

    /**
     * Moves the robot forward by setting all motors to the specified power.
     *
     * @param power The power level (0.0 to 1.0).
     */
    /**
     * Turns the robot to the right by setting motors to opposite powers.
     *
     * @param power The power level (0.0 to 1.0).
     */

    /**
     * Stops all motors by setting their power to zero.
     */
    /**
     * Releases MediaPlayer resources.
     */
    private void cleanup() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            telemetry.addData("MediaPlayer", "Released.");
            telemetry.update();
            Log.d("ChoppaAutonomous", "MediaPlayer released.");
        }
    }
}
