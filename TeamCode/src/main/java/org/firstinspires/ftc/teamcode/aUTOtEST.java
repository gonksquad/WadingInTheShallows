package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Objects;


// BATTERY AT MAX VOLTAGE



@Autonomous
public class aUTOtEST extends OpMode {

    String position = "none";
    String startposition = "none";
    Action l1;
    Action l2;
    Action l3;

    Action c1;
    Action c2;
    Action c3;

    Action r1;
    Action r2;
    Action r3;

    @Override
    public void init() {

    }
    @Override
    public void loop() {
        PinpointDrive drive = new PinpointDrive(hardwareMap, new Pose2d(18, 64, Math.toRadians(90)));

        Pose2d pose1 = new Pose2d(10, 34, Math.toRadians(270));
        Pose2d pose2 = new Pose2d(52, 43, Math.toRadians(180));

        Pose2d pose1r = new Pose2d(12, 33, Math.toRadians(180));
        Pose2d pose2r = new Pose2d(50, 29, Math.toRadians(180));

        Pose2d pose1c = new Pose2d(16, 34, Math.toRadians(270));
        Pose2d pose2c = new Pose2d(50, 35, Math.toRadians(180));

        l1 = drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(10,34), Math.toRadians(270))
                .build();

        l2 = drive.actionBuilder(pose1)
                .setReversed(false)
                .splineTo(new Vector2d(40,40), Math.toRadians(310))
                .splineTo(new Vector2d(55,55), Math.toRadians(225))
                .splineTo(new Vector2d(50,40), Math.toRadians(310))
                .splineTo(new Vector2d(55,55), Math.toRadians(225))
                .splineTo(new Vector2d(58,40), Math.toRadians(310))
                .splineTo(new Vector2d(55,55), Math.toRadians(225))
                .splineTo(new Vector2d(24,12), Math.toRadians(180))
                .build();

        l3 = drive.actionBuilder(pose2)
                .strafeTo(new Vector2d(45, 60))
                .strafeTo(new Vector2d(60, 60))
                .build();



        r1 = drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(12, 33), Math.toRadians(180))
                .build();

        r2 = drive.actionBuilder(pose1r)
                .strafeTo(new Vector2d(50, 29))
                .build();

        r3 = drive.actionBuilder(pose2r)
                .strafeTo(new Vector2d(45, 60))
                .strafeTo(new Vector2d(60, 60))
                .build();



        c1 = drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(16, 34), Math.toRadians(270))
                .build();

        c2 = drive.actionBuilder(pose1c)
                .strafeTo(new Vector2d(50, 35))
                .turn(Math.toRadians(-90))
                .build();

        c3 = drive.actionBuilder(pose2c)
                .strafeTo(new Vector2d(45, 60))
                .strafeTo(new Vector2d(60, 60))
                .build();


        Actions.runBlocking(
                l1
        );
        Actions.runBlocking(
                l2
        );
        }
    }
