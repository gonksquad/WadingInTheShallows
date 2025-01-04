package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.HardwareRR;
import org.firstinspires.ftc.teamcode.HardwareRR_old;
import org.firstinspires.ftc.teamcode.PinpointDrive;


// BATTERY AT MAX VOLTAGE



@Autonomous
@Disabled
public class GoudaAuto extends LinearOpMode {
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
    public HardwareRR hardware;

    String position = "none";
    String startposition = "none";
    Action goToPlace1;
    Action goToGrabA;
    Action goToGrabB;
    Action goToPlace2;
    Action goToPark;

    Action toSamplePickup;
    Action turnPlace;
    Action turnGrab;

    Action r1;
    Action r2;
    Action r3;

//    private void upDownGoTO(int position,double power){
//        upDownSlides.setTargetPosition(position);
//        upDownSlides.setPower(power);
//    }

    @Override
    public void runOpMode() {
        LF = hardwareMap.get(DcMotor.class, "2");
        LB = hardwareMap.get(DcMotor.class, "0");
        RB = hardwareMap.get(DcMotor.class, "1");
        RF = hardwareMap.get(DcMotor.class, "3");

//        RB.setDirection(DcMotor.Direction.REVERSE);
//        LB.setDirection(DcMotor.Direction.REVERSE);

        hardware = new HardwareRR(hardwareMap);

//        upDownSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        upDownSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        PinpointDrive drive = new PinpointDrive(hardwareMap, new Pose2d(-18, 64, Math.toRadians(90)));

        Pose2d pose1 = new Pose2d(-10, 38, Math.toRadians(270));
        Pose2d pose2 = new Pose2d(-30,45, Math.toRadians(220));
        Pose2d pose3 = new Pose2d(-30,45, Math.toRadians(120));
        Pose2d pose4 = new Pose2d(-40,45, Math.toRadians(220));
        Pose2d pose5 = new Pose2d(12, 33, Math.toRadians(180));
        Pose2d pose6 = new Pose2d(-48,60, Math.toRadians(270));
        Pose2d pose7 = new Pose2d(0,38, Math.toRadians(90));

        goToPlace1 = drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(-10,40), Math.toRadians(270))
                .build();

        toSamplePickup = drive.actionBuilder(pose1)
                .strafeToSplineHeading(new Vector2d(-32,44), Math.toRadians(220))
                .build();

        turnPlace = drive.actionBuilder(pose2)
                .turnTo(Math.toRadians(120))
                .build();

        turnGrab = drive.actionBuilder(pose3)
                .strafeToSplineHeading(new Vector2d(-41,44), Math.toRadians(220))
                .build();

        goToGrabA = drive.actionBuilder(pose4)
                .strafeToSplineHeading(new Vector2d(-48,50), Math.toRadians(270))
                .build();

        goToGrabB = drive.actionBuilder(pose5)
                .strafeToSplineHeading(new Vector2d(-48,60), Math.toRadians(270))
                .build();

        goToPlace2 = drive.actionBuilder(pose6)
                .strafeToSplineHeading(new Vector2d(0,38), Math.toRadians(90))
                .build();

        goToPark = drive.actionBuilder(pose7)
                .setReversed(false)
                .splineTo(new Vector2d(-35,30), Math.toRadians(270))
                .splineTo(new Vector2d(-35,0), Math.toRadians(170))
                .splineTo(new Vector2d(-43,15), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-48,50), Math.toRadians(90))
                .setReversed(false)
                .splineTo(new Vector2d(-45,20), Math.toRadians(270))
                .splineTo(new Vector2d(-57,10), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-60,50), Math.toRadians(90))
                .setReversed(false)
                .splineTo(new Vector2d(-55,20), Math.toRadians(270))
                .splineTo(new Vector2d(-60,10), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-65,66), Math.toRadians(90))
                .build();
//                .setReversed(false)
//                .splineTo(new Vector2d(-40,10), Math.toRadians(270))
//                .strafeTo(new Vector2d(-48,10))
//                .setReversed(true)
//                .splineTo(new Vector2d(-48,50), Math.toRadians(90))
//                .setReversed(false)
//                .splineTo(new Vector2d(-45,10), Math.toRadians(270))
//                .strafeTo(new Vector2d(-57,10))
//                .setReversed(true)
//                .splineTo(new Vector2d(-60,50), Math.toRadians(90))
//                .setReversed(false)
//                .splineTo(new Vector2d(-55,10), Math.toRadians(270))
//                .strafeTo(new Vector2d(-63,10))
//                .setReversed(true)
//                .splineTo(new Vector2d(-63,50), Math.toRadians(90))
//                .build();


        waitForStart();

        hardware.placerFlipIdle();
        hardware.placerClose();
        sleep(1000);
        hardware.upDownSlides.setPower(1);
        sleep(200);

        Actions.runBlocking(
                goToPlace1
        );

        hardware.upDownSlides.setPower(0.1);
        sleep(300);
        hardware.placerFlipGrabWall();
        sleep(400);
        hardware.upDownSlides.setPower(-0.9);
        sleep(400);
        hardware.placerOpen();
        sleep(200);
        hardware.placerFlipIdle();
        sleep(500);

        hardware.grabberFlipMid();
        hardware.grabberOpen();
        Actions.runBlocking(
                toSamplePickup
        );
        hardware.setReachyReachyPosition(-1000, 1);
        hardware.grabberSpin(4);
        sleep(5000);
        hardware.grabberFlipDown();
        sleep(3000);
        hardware.grabberClose();
        Actions.runBlocking(
                turnPlace
        );
        hardware.grabberOpen();
        hardware.grabberFlipMid();
        Actions.runBlocking(
                turnGrab
        );
        hardware.grabberFlipDown();
        hardware.grabberSpin(4);
        sleep(5000);
        hardware.grabberClose();
        Actions.runBlocking(
                turnPlace
        );
        hardware.grabberOpen();
        hardware.grabberFlipMid();
        hardware.setReachyReachyPosition(0, 1);

        Actions.runBlocking(
                goToGrabA
        );
        hardware.placerOpen();
        hardware.placerFlipGrabWall();
        sleep(800);
        Actions.runBlocking(
                goToGrabB
        );
        hardware.placerClose();
        sleep(300);
        hardware.placerFlipIdle();
        sleep(800);
        hardware.upDownSlides.setPower(1);
        Actions.runBlocking(
                goToPlace2
        );
        hardware.upDownSlides.setPower(0.1);
        sleep(300);
        hardware.placerFlipGrabWall();
        sleep(400);
        hardware.upDownSlides.setPower(-0.9);
        sleep(400);
        hardware.placerOpen();
        sleep(200);
        hardware.placerFlipIdle();
        sleep(300);
        Actions.runBlocking(
                goToPark
        );
//
//        hardware.upDownSlides.setPower(1);
//        hardware.placerFlipUp();
//        sleep(1200);
    }
}