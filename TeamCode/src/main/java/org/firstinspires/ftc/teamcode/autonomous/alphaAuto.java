package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.HardwareRR;
import org.firstinspires.ftc.teamcode.HardwareRR_old;
import org.firstinspires.ftc.teamcode.PinpointDrive;


// BATTERY AT MAX VOLTAGE



@Autonomous
public class alphaAuto extends LinearOpMode {
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
    public HardwareRR_old hardware;

    String position = "none";
    String startposition = "none";
    Action goToPlace;
    Action goToPark;
    Action l3;

    Action c1;
    Action c2;
    Action c3;

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

        hardware = new HardwareRR_old(hardwareMap);

//        upDownSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        upDownSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        PinpointDrive drive = new PinpointDrive(hardwareMap, new Pose2d(18, 64, Math.toRadians(90)));

        Pose2d pose1 = new Pose2d(3, 40.2, Math.toRadians(270));
        Pose2d pose2 = new Pose2d(52, 43, Math.toRadians(180));

        Pose2d pose1r = new Pose2d(12, 33, Math.toRadians(180));
        Pose2d pose2r = new Pose2d(50, 29, Math.toRadians(180));

        Pose2d pose1c = new Pose2d(16, 34, Math.toRadians(270));
        Pose2d pose2c = new Pose2d(50, 35, Math.toRadians(180));

        goToPlace = drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineTo(new Vector2d(3,40.2), Math.toRadians(270))
                .build();

        goToPark = drive.actionBuilder(pose1)
                .strafeToSplineHeading(new Vector2d(55,55), Math.toRadians(45))
                .strafeTo(new Vector2d(49, 49))
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

        waitForStart();

        hardware.placerFlipMid();
        hardware.placerClose();
        sleep(1000);
        hardware.upDownSlides.setPower(1);
        sleep(200);

        Actions.runBlocking(
                goToPlace
        );

        hardware.upDownSlides.setPower(0.1);
        sleep(1000);
        hardware.placerFlipUp();
        hardware.upDownSlides.setPower(-0.9);
        sleep(850);
        hardware.placerOpen();
        sleep(1000);
        hardware.placerFlipDown();
        sleep(1000);


        Actions.runBlocking(
                goToPark
        );

        hardware.upDownSlides.setPower(1);
        hardware.placerFlipUp();
        sleep(1200);
    }
}