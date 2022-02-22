// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
        /**
         * The left-to-right distance between the drivetrain wheels
         *
         * Should be measured from center to center.
         */
        public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.578; // FIXME Measure and set trackwidth
        /**
         * The front-to-back distance between the drivetrain wheels.
         *
         * Should be measured from center to center.
         */
        public static final double DRIVETRAIN_WHEELBASE_METERS = 0.578; // FIXME Measure and set wheelbase

        // public static final int DRIVETRAIN_PIGEON_ID = 0; // FIXME Set Pigeon ID

        public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 1; // FIXME Set front left module drive motor ID
        public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 3; // FIXME Set front left module steer motor ID
        public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 2; // FIXME Set front left steer encoder ID
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(264.8); // FIXME Measure and set
                                                                                           // front
                                                                                           // left steer offset

        public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 4; // FIXME Set front right drive motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 6; // FIXME Set front right steer motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 5; // FIXME Set front right steer encoder ID
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(185.3); // FIXME Measure and set
                                                                                            // front
                                                                                            // right steer offset

        public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 7; // FIXME Set back left drive motor ID
        public static final int BACK_LEFT_MODULE_STEER_MOTOR = 9; // FIXME Set back left steer motor ID
        public static final int BACK_LEFT_MODULE_STEER_ENCODER = 8; // FIXME Set back left steer encoder ID
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(94.2); // FIXME Measure and set back
                                                                                          // left
                                                                                          // steer offset

        public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 10; // FIXME Set back right drive motor ID
        public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 12; // FIXME Set back right steer motor ID
        public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 11; // FIXME Set back right steer encoder ID
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(206.9); // FIXME Measure and set
                                                                                            // back
                                                                                            // right steer offset
        public static final int SHOOTER_MOTOR = 13;

        public static final int INTAKE_ROLLER = 15;

        public static final int INTAKE_EXTEND = 2;
        
        public static final int INTAKE_RETRACT = 3;

        public static final double TALONFX_CPR = 2048.0;

        public static final int MAIN_PCM = 17;

        public static final int CLIMB_PCM = 16;

        public static final int PUSHER_FORWARD = 0;

        public static final int PUSHER_REVERSE = 1;

        public static final double SHOOT_MOTOR_GEAR_RATIO = 1.0;

        public static final double RPM_TO_COUNTS_PER_100MS = TALONFX_CPR / 600.0 / SHOOT_MOTOR_GEAR_RATIO;

        public static final double MAX_VOLTAGE = 12.0;

        public static final double MAX_VELOCITY_METERS_PER_SECOND = 6380.0 / 60.0 *
        
                        SdsModuleConfigurations.MK4_L2.getDriveReduction() *
                        SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI * .02;
        /**
         * The maximum angular velocity of the robot in radians per second.
         * <p>
         * This is a measure of how fast the robot can rotate in place.
         */
        // Here we calculate the theoretical maximum angular velocity. You can also
        // replace this with a measured amount.
        public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
                        Math.hypot(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0);
}
