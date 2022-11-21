// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

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
    public static final int PIGEON_2 = 19; //FIXME Set Pigeon ID
    public static RobotType ROBOTTYPE = RobotType.CBOT;

    public static final int DESIGNATED_LOOP_PERIOD = 20;

    // public enum DelayToOff {

    //     /**
    //      * the delay in seconds until the solenoids for certain ports turn off
    //      * below is just an example from 2019
    //      */

    //     CLIMB_CLAWS(1.0),
    //     INTAKE_KICKERS(0.5),
    //     SHOOTER_PUSHER(0.5);

    //     public long millis;

    //     DelayToOff(double time) {

    //         this.millis = (long) time * 1000;

    //     }
    // }

    public enum RobotType {
        PBOT, CBOT
    }

    public static final class DRIVE {
        /**
         * The left-to-right distance between the drivetrain wheels
         * <p>
         * Should be measured from center to center.
         */
        public static final double TRACKWIDTH_METERS = 0.578; // FIXME Measure and set trackwidth
        /**
         * The front-to-back distance between the drivetrain wheels.
         * <p>
         * Should be measured from center to center.
         */
        public static final double WHEELBASE_METERS = 0.578; // FIXME Measure and set wheelbase

        public static final SwerveDriveKinematics KINEMATICS = new SwerveDriveKinematics(
                new Translation2d(DRIVE.TRACKWIDTH_METERS / 2.0, DRIVE.WHEELBASE_METERS / 2.0),
                new Translation2d(DRIVE.TRACKWIDTH_METERS / 2.0, -DRIVE.WHEELBASE_METERS / 2.0),
                new Translation2d(-DRIVE.TRACKWIDTH_METERS / 2.0, DRIVE.WHEELBASE_METERS / 2.0),
                new Translation2d(-DRIVE.TRACKWIDTH_METERS / 2.0, -DRIVE.WHEELBASE_METERS / 2.0)
        );

        public static final double WHEEL_DIAMETER_METERS = SdsModuleConfigurations.MK4_L2.getWheelDiameter(); // .10033 = ~4 inches
        public static final double WHEEL_CIRCUMFERENCE_METERS = WHEEL_DIAMETER_METERS * Math.PI;

        /**
         * Swerve Drive Module CAN IDs
         */
        public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 1; // FIXME Set front left module drive motor ID
        public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 3; // FIXME Set front left module steer motor ID
        public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 2; // FIXME Set front left steer encoder ID
        public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 4; // FIXME Set front right drive motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 6; // FIXME Set front right steer motor ID
        public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 5; // FIXME Set front right steer encoder ID
        public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 7; // FIXME Set back left drive motor ID
        public static final int BACK_LEFT_MODULE_STEER_MOTOR = 9; // FIXME Set back left steer motor ID
        public static final int BACK_LEFT_MODULE_STEER_ENCODER = 8; // FIXME Set back left steer encoder ID
        public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 10; // FIXME Set back right drive motor ID
        public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 12; // FIXME Set back right steer motor ID
        public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 11; // FIXME Set back right steer encoder ID

        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(263.89) : -Math.toRadians(93.35); //85.2 // FIXME Measure and set
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(173.45) : -Math.toRadians(124.05); //7.95 // FIXME Measure and set
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(92.0) : -Math.toRadians(313.4); //274.04 // FIXME Measure and set back
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = ROBOTTYPE == RobotType.PBOT ? -Math.toRadians(199.5) : -Math.toRadians(202.26); //24.5 // FIXME Measure and set


        // Drivetrain Performance Mechanical limits
        static public final double MAX_FWD_REV_SPEED_MPS = Units.feetToMeters(16.3); // Measured
        static public final double MAX_FWD_REV_SPEED_MPS_EST = 6380.0 / 60.0 * SdsModuleConfigurations.MK4_L2.getDriveReduction() * SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;
        static public final double MAX_STRAFE_SPEED_MPS = Units.feetToMeters(16.3); // Unused
        static public final double MAX_ROTATE_SPEED_RAD_PER_SEC = Units.degreesToRadians(500); // FIXME Measured
        static public final double MAX_ROTATE_SPEED_RAD_PER_SEC_EST = MAX_FWD_REV_SPEED_MPS_EST / Math.hypot(TRACKWIDTH_METERS / 2.0, WHEELBASE_METERS / 2.0);

        // Fine control speed limits
        static public final double MAX_FWD_REV_SPEED_FAST = 0.5; // Percent of output power
        static public final double MAX_STRAFE_SPEED_FAST = 0.5;
        static public final double MAX_ROTATE_SPEED_FAST = 0.35;
        static public final double MAX_FWD_REV_SPEED_SLOW = 0.25; // Percent of output power
        static public final double MAX_STRAFE_SPEED_SLOW = 0.25;
        static public final double MAX_ROTATE_SPEED_SLOW = 0.25;
    }

    public static final class AUTO {
        public static final double kMaxSpeedMetersPerSecond = 2;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

        public static final double TRAJECTORYXkP = 10;
        public static final double TRAJECTORYYkP = 10;
        public static final double THETACONTROLLERkP = 10;

        // Constraint for the motion profilied robot angle controller
        public static final TrapezoidProfile.Constraints THETACONTROLLERCONSTRAINTS =
                new TrapezoidProfile.Constraints(
                        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }

    public static final class ROBOT {
        public static final double QUIESCENT_CURRENT_DRAW_A = 2.0; //Misc electronics
        public static final double BATTERY_NOMINAL_VOLTAGE = 13.2; //Nicely charged battery
        public static final double BATTERY_NOMINAL_RESISTANCE = 0.040; //40mOhm - average battery + cabling
        public static final double MAX_VOLTAGE = 12.0; // Maximum Voltage sent to a motor controller

        // Assumed starting location of the robot. Auto routines will pick their own location and update this.
        static public final Pose2d DFLT_START_POSE = new Pose2d(Units.feetToMeters(24.0), Units.feetToMeters(10.0), Rotation2d.fromDegrees(0));

        static public final double ROBOT_MASS_kg = Units.lbsToKilograms(140);
        static public final double ROBOT_MOI_KGM2 = 1.0 / 12.0 * ROBOT_MASS_kg * Math.pow((DRIVE.WHEELBASE_METERS * 1.1), 2) * 2; //Model moment of intertia as a square slab slightly bigger than wheelbase with axis through center
    }

    public static final class ENCODER {
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // SENSOR CONSTANTS
        // Sensor-related constants - pulled from datasheets for the sensors and gearboxes
        static public final int ENC_PULSE_PER_REV = 2048; // TalonFX integrated sensor
        static public final int WHEEL_ENC_COUNTS_PER_WHEEL_REV = ENC_PULSE_PER_REV;  //Assume 1-1 gearing for now
        static public final double WHEEL_ENC_WHEEL_REVS_PER_COUNT = 1.0 / ((double) (WHEEL_ENC_COUNTS_PER_WHEEL_REV));
        static public final int STEER_ENC_COUNTS_PER_MODULE_REV = 4096; // CANCoder
        static public final double steer_ENC_MODULE_REVS_PER_COUNT = 1.0 / ((double) (STEER_ENC_COUNTS_PER_MODULE_REV));
    }

    /** TODO add subsystem related constants */
}
