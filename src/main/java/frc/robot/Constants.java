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

        public static final int L1_EXTEND = 0;
        public static final int L1_RETRACT = 1;

        public static final int H2_EXTEND = 2;
        public static final int H2_RETRACT = 3;

        public static final int L3_EXTEND = 4;
        public static final int L3_RETRACT = 5;

        public static final int H4_EXTEND = 6;
        public static final int H4_RETRACT = 7;

        public static final int L1_SWITCH = 0;
        public static final int H2_SWITCH = 1;
        public static final int L3_SWITCH = 2;
        public static final int H4_SWITCH = 3;

        public static final int CLIMB_ARM_MOTOR = 14;
        public static final int CLIMB_ENCODER = 0;

        public static final int TALONFX_CPR = 2048;

        public static final double MAX_ANALOG_VOLTAGE = 5.0;
        public static final double CLIMB_ARM_GEAR_RATIO = 716.8;
        public static final double CLIMB_ANALOG_VOLTAGE_OFFSET = 2.46;
        public static final double CLIMB_ANALOG_VOLTAGE_TO_DEGREE = 0.014556;
        public static final double CLIMB_ARM_ROTATIONS_TO_FALCON_TICKS = CLIMB_ARM_GEAR_RATIO * TALONFX_CPR;
        public static final double ARM_DEGREES_TO_FALCON_TICKS = CLIMB_ARM_GEAR_RATIO * TALONFX_CPR / 360.0;
        public static final double FALCON_TICKS_TO_ARM_DEGREES = 1 / ARM_DEGREES_TO_FALCON_TICKS;

        public static final double MAX_ARM_VELOCITY_DEGREES_PER_SECOND = 20.0;
        public static final double MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND = 40;
        public static final int MAX_ARM_VELOCITY_NATIVE_UNITS = (int) (MAX_ARM_VELOCITY_DEGREES_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
        public static final int MAX_ARM_ACCELERATION_NATIVE_UNITS = (int) (MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
        
        public static final int SHOOTER_MOTOR = 13;

        public static final int INTAKE_ROLLER = 15;

        public static final int INTAKE_EXTEND = 2;
        
        public static final int INTAKE_RETRACT = 3;

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

        public enum DelayToOff {

                /**
                 * the delay in seconds until the solenoids for certain ports turn off
                 * below is just an example from 2019
                 */

                CLIMB_CLAWS(1.0),
                INTAKE_KICKERS(0.5),
                SHOOTER_PUSHER(0.5);

                public long millis;

                private DelayToOff(double time) {

                        this.millis = (long) time * 1000;

                }
        }

        public enum FunctionStateDelay {

                /**
                 * the delay in seconds between different states in the function switch
                 * statements
                 * creates the wait time between cases in other words
                 * below is just an example from 2019 (the examples should probably be removed
                 * when the next years copy of the code is made)
                 */

                cargoIntakeFloor_elevatorStage1A_to_armExtendExtended_rollerIntake(1.0),
                cargoShoot_shooterShoot_to_shooterStop(0.2),
                hatchPlace_hatchIntakeRelease_to_hatchPanelApparatusExtended(0.05),
                hatchPlace_hatchPanelApparatusExtended_to_hatchPanelApparatusCollapsed(0.25),
                hatchPlace_hatchPanelApparatusCollapsed_to_hatchPlaceCompleted(3.0),
                xButtonReset_armLiftMid_to_armExtendCollapsed(1.0),
                intakeRoller_burpDelay(0.15);

                public double time;

                private FunctionStateDelay(double time) {

                        this.time = time * 1000;

                }
        }

        public enum PowerOutput {

                /**
                 * the power output in percentage for the different actions in the functions for
                 * the motors
                 * for example, an intake motor at a constant percentage of power while button
                 * pressed
                 * below is just an example from 2020 (shooter was updated for RobotBasic)
                 */

                shooter_shooter_shootConstant(15500), // 17750
                // shooter_shooter_shootInner(0.1127 * Math.pow((Robot.drive.getDistance()), 2)
                // - (42.1417 * Robot.drive.getDistance()) + 17746.7581),
                shooter_topBelt_feed(0.80),
                shooter_indexWheel_feed(0.40),
                shooter_indexWheel_index(0.45),
                shooter_indexWheel_reverseIndex(1.0),
                shooter_indexWheel_manualIndex(-1.0),
                shooter_hood_far(0.5),
                shooter_hood_close(-0.5),
                intake_intakeRoller_intake(.75),
                intake_intakeRoller_burp(-0.5),
                intake_intakeArm_armUp(0.80),
                intake_intakeArm_armDown(-0.80),
                elevator_elevator_maxup(0.9),
                elevator_elevator_maxdown(0.5),
                elevator_elevator_stop(0.1),
                elevator_elevator_colorWheel(0.5),
                elevator_elevator_removeResistance(0.0),
                elevator_winch_winch(1.0),
                elevator_translator_maxOutput(1.0),
                spinner_spinner_spin(1.0);

                public double power;

                private PowerOutput(double power) {

                        this.power = power;

                }

        }
}
