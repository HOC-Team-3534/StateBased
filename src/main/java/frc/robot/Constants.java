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
        public static RobotType ROBOTTYPE = RobotType.CBOT;

        /**
         * Swerve Drive Module CAN IDs and Encoder Offset Info
         */
        public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 1; // FIXME Set front left module drive motor ID
        public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 3; // FIXME Set front left module steer motor ID
        public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 2; // FIXME Set front left steer encoder ID
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(263.89) : -Math.toRadians(93.35); //85.2 // FIXME Measure and set

        public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 4; // FIXME Set front right drive motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 6; // FIXME Set front right steer motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 5; // FIXME Set front right steer encoder ID
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(173.45) : -Math.toRadians(124.05); //7.95 // FIXME Measure and set

        public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 7; // FIXME Set back left drive motor ID
        public static final int BACK_LEFT_MODULE_STEER_MOTOR = 9; // FIXME Set back left steer motor ID
        public static final int BACK_LEFT_MODULE_STEER_ENCODER = 8; // FIXME Set back left steer encoder ID
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(92.0) : -Math.toRadians(313.4); //274.04 // FIXME Measure and set back

        public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 10; // FIXME Set back right drive motor ID
        public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 12; // FIXME Set back right steer motor ID
        public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 11; // FIXME Set back right steer encoder ID
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(199.5) : -Math.toRadians(202.26); //24.5 // FIXME Measure and set

        /**
         * PCM Port and Solenoid Information //FIXME Are you using air?
         */
        public static final int L1_EXTEND = 0;
        public static final int L1_RETRACT = 1;

        public static final int H2_EXTEND = 2;
        public static final int H2_RETRACT = 3;

        public static final int L3_EXTEND = 4;
        public static final int L3_RETRACT = 5;

        public static final int H4_EXTEND = 6;
        public static final int H4_RETRACT = 7;

        public static final int PUSHER_FORWARD = 0;
        public static final int PUSHER_REVERSE = 1;

        public static final int INTAKE_EXTEND = 2;
        public static final int INTAKE_RETRACT = 3;

        /**
         * DIO Port Information
         */
        public static final int L1_SWITCH = 0;
        public static final int H2_SWITCH = 1;
        public static final int L3_SWITCH = 2;
        public static final int H4_SWITCH = 3;

        /**
         * CAN IDs
         */
        public static final int CLIMB_ARM_MOTOR = 14;

        public static final int SHOOTER_MOTOR = 13;

        public static final int SHOOTER_BOOT = 18;

        public static final int INTAKE_ROLLER = 15;

        public static final int MAIN_PCM = 17;
        public static final int CLIMB_PCM = 16;

        public static final int PIGEON_2 = 19; //FIXME Set Pigeon ID

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

        public static final int TALONFX_CPR = 2048;

        public static final double CLIMB_ARM_GEAR_RATIO = 716.8;
        public static final double ARM_DEGREES_TO_FALCON_TICKS = CLIMB_ARM_GEAR_RATIO * TALONFX_CPR / 360.0;
        public static final double FALCON_TICKS_TO_ARM_DEGREES = 1 / ARM_DEGREES_TO_FALCON_TICKS;

        public static final double MAX_ARM_VELOCITY_DEGREES_PER_SECOND = 51.0;
        public static final double MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND = 80.0;
        public static final int MAX_ARM_VELOCITY_NATIVE_UNITS = (int) (MAX_ARM_VELOCITY_DEGREES_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
        public static final int MAX_ARM_ACCELERATION_NATIVE_UNITS = (int) (MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);

        public static final double MAX_ARM_VELOCITY_DEGREES_PER_SECOND_SLOW = 45.0;
        public static final double MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND_SLOW = 50.0;
        public static final int MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW = (int) (MAX_ARM_VELOCITY_DEGREES_PER_SECOND_SLOW * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
        public static final int MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW = (int) (MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND_SLOW * ARM_DEGREES_TO_FALCON_TICKS / 10.0);

        public static final double SHOOT_MOTOR_GEAR_RATIO = 1.0;
        public static final double RPM_TO_COUNTS_PER_100MS = TALONFX_CPR / 600.0 / SHOOT_MOTOR_GEAR_RATIO;

        public static final double MAX_VOLTAGE = 12.0;

        public static final double PHYSICAL_MAX_VELOCITY = 6380.0 / 60.0 *
                SdsModuleConfigurations.MK4_L2.getDriveReduction() *
                SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;

        public static final double MAX_VELOCITY_AUTONOMOUS = 2.0;

        public static final double MAX_ACCELERATION = 3.0;

        public static final double MAX_VELOCITY_CREEP = PHYSICAL_MAX_VELOCITY * 0.25;
        /**
         * The maximum angular velocity of the robot in radians per second.
         * <p>
         * This is a measure of how fast the robot can rotate in place.
         */
        // Here we calculate the theoretical maximum angular velocity. You can also
        // replace this with a measured amount.
        public static final double PHYSICAL_MAX_ANGULAR_VELOCITY = PHYSICAL_MAX_VELOCITY /
                        Math.hypot(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0);

        public static final double MAX_ANGULAR_VELOCITY_CREEP = PHYSICAL_MAX_ANGULAR_VELOCITY * 0.25;

        public static final double MIDBAR_GRAB_ANGLE_COMMAND = 94.0;
        public static final double HIGHBAR_GRAB_ANGLE_COMMAND = 265.0; //impossible to actually be at 270
        public static final double TRAVERSALBAR_GRAB_ANGLE_COMMMAND = 445.0; //impossible to actually be at 450
        public static final double SWINGTOREST_ANGLE_COMMAND = 450.0;

        public static final double MIDHIGHBAR_SLOWDOWN_ANGLE = 130.0;
        public static final double HIGHTRAVERSAL_SLOWDOWN_ANGLE = 290.0;

        public static final double MIDHIGHBAR_RECENTER_ANGLE_COMMAND = 220.0; //would be sitting perpendicular to the bars at 180
        public static final double HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND = 400.0; //would be sitting perpendicular to the bars at 360
        public static final double RECENTER_ANGLE_TOLERANCE = 5.0; //should at least be 3 degrees just for comfort, if not at least 5

        public static final double DONERELEASINGMIDBAR_ANGLE = 270.0;

        public enum DelayToOff {

                /**
                 * the delay in seconds until the solenoids for certain ports turn off
                 * below is just an example from 2019
                 */

                CLIMB_CLAWS(1.0),
                INTAKE_KICKERS(0.5),
                SHOOTER_PUSHER(0.5);

                public long millis;

                DelayToOff(double time) {

                        this.millis = (long) time * 1000;

                }
        }

        public enum RobotType{
                PBOT, CBOT
        }
}
