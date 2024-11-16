//package com.example.meepmeeptesting;
//
//import com.acmerobotics.roadrunner.Pose2d;
//import com.noahbres.meepmeep.MeepMeep;
//import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
//import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
//
//public class MeepMeepTestingOLD {
//    public static void main(String[] args) {
//        MeepMeep meepMeep = new MeepMeep(800);
//
//        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
//                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(18, 64, Math.toRadians(90)))
//
//
//                        .lineToYSplineHeading(33, Math.toRadians(0))
//                        .waitSeconds(2)
//                        .setTangent(Math.toRadians(90))
//                        .lineToY(48)
//                        .setTangent(Math.toRadians(0))
//                        .lineToX(32)
//                        .strafeTo(new Vector2d(44.5, 30))
//                        .turn(Math.toRadians(180))
//                        .lineToX(47.5)
//                        .waitSeconds(3)
//                                .build());
//
//
//
//
//                        //TODO:blue NET side, one specimen, three yellows, park ascent zone
//                        //TODO:START POSE: Pose2d(18, 64, Math.toRadians(90))
////                        .setReversed(true)
////                        .splineTo(new Vector2d(10,34), Math.toRadians(270))
////                        .lineToSplineHeading(new Pose2d(40,40, Math.toRadians(310)))
////                        .lineToSplineHeading(new Pose2d(55,55, Math.toRadians(225)))
////                        .lineToSplineHeading(new Pose2d(50,40, Math.toRadians(310)))
////                        .lineToSplineHeading(new Pose2d(55,55, Math.toRadians(225)))
////                        .lineToSplineHeading(new Pose2d(58,40, Math.toRadians(310)))
////                        .lineToSplineHeading(new Pose2d(55,55, Math.toRadians(225)))
////                        .lineTo(new Vector2d(49, 49))
////                        .splineTo(new Vector2d(24,12), Math.toRadians(180))
////                        .build());
//
//                        //TODO:red NET side, one specimen, three yellows, park ascent zone
//                        //TODO:START POSE: Pose2d(-18, -64, Math.toRadians(270))
////                        .setReversed(true)
////                        .splineTo(new Vector2d(-10,-34), Math.toRadians(90))
////                        .lineToSplineHeading(new Pose2d(-40,-40, Math.toRadians(110)))
////                        .lineToSplineHeading(new Pose2d(-55,-55, Math.toRadians(45)))
////                        .lineToSplineHeading(new Pose2d(-50,-40, Math.toRadians(110)))
////                        .lineToSplineHeading(new Pose2d(-55,-55, Math.toRadians(45)))
////                        .lineToSplineHeading(new Pose2d(-58,-40, Math.toRadians(130)))
////                        .lineToSplineHeading(new Pose2d(-55,-55, Math.toRadians(45)))
////                        .lineTo(new Vector2d(-49, -49))
////                        .splineTo(new Vector2d(-24,-12), Math.toRadians(0))
////                        .build());
//
//                        //TODO:blue SPECIMEN side, four specimens, park ????
//                        //TODO:START POSE: Pose2d(-18, 64, Math.toRadians(90))
////                        .setReversed(true)
////                        .splineTo(new Vector2d(-10,34), Math.toRadians(270))
////                        //score first specimen
////                        .lineToSplineHeading(new Pose2d(-40,40, Math.toRadians(240)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(-50,50, Math.toRadians(270)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(-50,40, Math.toRadians(240)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(-50,50, Math.toRadians(270)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(-50,60, Math.toRadians(270)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(-10,34, Math.toRadians(90)))
////                        //score next specimen
////                        .lineToSplineHeading(new Pose2d(-58,40, Math.toRadians(230)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(-50,50, Math.toRadians(270)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(-50,60, Math.toRadians(270)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(-10,34, Math.toRadians(90)))
////                        //score next specimen
////                        .lineToSplineHeading(new Pose2d(-50,60, Math.toRadians(270)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(-10,34, Math.toRadians(90)))
////                        //score next specimen
////                        .build());
//
//                        //TODO:red SPECIMEN side, four specimens, park ????
//                        //TODO:START POSE: Pose2d(18, -64, Math.toRadians(270))
////                        .setReversed(true)
////                        .splineTo(new Vector2d(10,-34), Math.toRadians(90))
////                        //score first specimen
////                        .lineToSplineHeading(new Pose2d(40,-40, Math.toRadians(60)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(50,-50, Math.toRadians(90)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(50,-40, Math.toRadians(60)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(50,-50, Math.toRadians(90)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(50,-60, Math.toRadians(90)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(10,-34, Math.toRadians(270)))
////                        //score next specimen
////                        .lineToSplineHeading(new Pose2d(58,-40, Math.toRadians(60)))
////                        //grab next sample
////                        .lineToSplineHeading(new Pose2d(50,-50, Math.toRadians(90)))
////                        //place sample in observation zone
////                        .lineToSplineHeading(new Pose2d(50,-60, Math.toRadians(90)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(10,-34, Math.toRadians(270)))
////                        //score next specimen
////                        .lineToSplineHeading(new Pose2d(50,-60, Math.toRadians(270)))
////                        //grab next specimen
////                        .lineToSplineHeading(new Pose2d(10,-34, Math.toRadians(270)))
////                        //score next specimen
////                        .build());
//
//
//        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();
//    }
//}