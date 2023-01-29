// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.statebasedcontroller.subsystem.general.swervedrive.swervelib.SDSModuleConfiguration;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
	public static final int MAIN_PCM = 17;
	public static final int PIGEON_2 = 19; // FIXME Set Pigeon ID
	public static RobotType ROBOTTYPE = RobotType.CBOT;

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

	public enum RobotType {
		PBOT,
		CBOT
	}

	public static final class Drive {
		public static final class Known {
			/**
			 * The left-to-right distance between the drivetrain wheels
			 * <p>
			 * Should be measured from center to center.
			 */
			public static final double TRACKWIDTH_METERS = 0.578; // FIXME
			/**
			 * The front-to-back distance between the drivetrain wheels.
			 * <p>
			 * Should be measured from center to center.
			 */
			public static final double WHEELBASE_METERS = 0.578; // FIXME
			public static final SDSModuleConfiguration SDS_MODULE_CONFIGURATION = SDSModuleConfiguration.SDSMK4(SDSModuleConfiguration.driveGearRatios.SDSMK4_L2);
			public static final double MAX_DRIVE_MOTOR_RPM = 6380.0; // Falcon500
																		// max
																		// rpm
		}

		public static final class Calculated {
			public static final SwerveDriveKinematics KINEMATICS = new SwerveDriveKinematics(	new Translation2d(	Drive.Known.TRACKWIDTH_METERS	/ 2.0,
																													Drive.Known.WHEELBASE_METERS / 2.0),
																								new Translation2d(	Drive.Known.TRACKWIDTH_METERS	/ 2.0,
																													-Drive.Known.WHEELBASE_METERS / 2.0),
																								new Translation2d(	-Drive.Known.TRACKWIDTH_METERS	/ 2.0,
																													Drive.Known.WHEELBASE_METERS / 2.0),
																								new Translation2d(	-Drive.Known.TRACKWIDTH_METERS	/ 2.0,
																													-Drive.Known.WHEELBASE_METERS / 2.0));
			// Drivetrain Performance Mechanical limits
			static public final double MAX_FWD_REV_SPEED_MPS_EST = Known.MAX_DRIVE_MOTOR_RPM / 60.0 / Known.SDS_MODULE_CONFIGURATION.driveGearRatio * Known.SDS_MODULE_CONFIGURATION.wheelCircumference;
			static public final double MAX_ROTATE_SPEED_RAD_PER_SEC_EST = MAX_FWD_REV_SPEED_MPS_EST / Math.hypot(	Known.TRACKWIDTH_METERS / 2.0,
																													Known.WHEELBASE_METERS / 2.0);
			static public final double MAX_ROTATE_SPEED_RAD_PER_SEC_MOTOR_EST = Known.MAX_DRIVE_MOTOR_RPM / 60 / Known.SDS_MODULE_CONFIGURATION.angleGearRatio * 2 * Math.PI;
		}

		public static final class Config {
			public static final int PIGEON2_ID = 13;
		}
	}

	public static final class Climber {
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
		public static final int CLIMB_PCM = 16;
		public static final double CLIMB_ARM_GEAR_RATIO = 716.8;
		public static final double ARM_DEGREES_TO_FALCON_TICKS = CLIMB_ARM_GEAR_RATIO * Encoder.ENC_PULSE_PER_REV / 360.0;
		public static final double FALCON_TICKS_TO_ARM_DEGREES = 1 / ARM_DEGREES_TO_FALCON_TICKS;
		public static final double MAX_ARM_VELOCITY_DEGREES_PER_SECOND = 51.0;
		public static final double MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND = 80.0;
		public static final double MAX_ARM_VELOCITY_DEGREES_PER_SECOND_SLOW = 45.0;
		public static final double MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND_SLOW = 50.0;
		public static final int MAX_ARM_VELOCITY_NATIVE_UNITS = (int) (MAX_ARM_VELOCITY_DEGREES_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
		public static final int MAX_ARM_ACCELERATION_NATIVE_UNITS = (int) (MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
		public static final int MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW = (int) (MAX_ARM_VELOCITY_DEGREES_PER_SECOND_SLOW * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
		public static final int MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW = (int) (MAX_ARM_ACCELERATION_DEGREES_PER_SECOND_PER_SECOND_SLOW * ARM_DEGREES_TO_FALCON_TICKS / 10.0);
		public static final double MIDBAR_GRAB_ANGLE_COMMAND = 94.0;
		public static final double HIGHBAR_GRAB_ANGLE_COMMAND = 265.0; // impossible
																		// to
																		// actually
																		// be at
																		// 270
		public static final double TRAVERSALBAR_GRAB_ANGLE_COMMMAND = 445.0; // impossible
																				// to
																				// actually
																				// be
																				// at
																				// 450
		public static final double SWINGTOREST_ANGLE_COMMAND = 450.0;
		public static final double MIDHIGHBAR_SLOWDOWN_ANGLE = 130.0;
		public static final double HIGHTRAVERSAL_SLOWDOWN_ANGLE = 290.0;
		public static final double MIDHIGHBAR_RECENTER_ANGLE_COMMAND = 220.0; // would
																				// be
																				// sitting
																				// perpendicular
																				// to
																				// the
																				// bars
																				// at
																				// 180
		public static final double HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND = 400.0; // would
																					// be
																					// sitting
																					// perpendicular
																					// to
																					// the
																					// bars
																					// at
																					// 360
		public static final double RECENTER_ANGLE_TOLERANCE = 5.0; // should at
																	// least be
																	// 3 degrees
																	// just for
																	// comfort,
																	// if not at
																	// least 5
		public static final double DONERELEASINGMIDBAR_ANGLE = 270.0;
	}

	public static final class Auto {
		public static final double kMaxSpeedMetersPerSecond = 2;
		public static final double kMaxAccelerationMetersPerSecondSquared = 3;
		public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
		public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
		public static final double TRAJECTORYXkP = 10;
		public static final double TRAJECTORYYkP = 10;
		public static final double THETACONTROLLERkP = 10;
		// Constraint for the motion profilied robot angle controller
		public static final TrapezoidProfile.Constraints THETACONTROLLERCONSTRAINTS = new TrapezoidProfile.Constraints(	kMaxAngularSpeedRadiansPerSecond,
																														kMaxAngularSpeedRadiansPerSecondSquared);
	}

	public static final class Robot {
		public static final double QUIESCENT_CURRENT_DRAW_A = 2.0; // Misc
																	// electronics
		public static final double BATTERY_NOMINAL_VOLTAGE = 13.2; // Nicely
																	// charged
																	// battery
		public static final double BATTERY_NOMINAL_RESISTANCE = 0.040; // 40mOhm
																		// -
																		// average
																		// battery
																		// +
																		// cabling
		/*
		 * Maximum Voltage sent to a motor controller Assumed starting location
		 * of the robot. Auto routines will pick their own location and update
		 * this.
		 */
		public static final double MAX_VOLTAGE = 12.0;
	}

	public static final class Encoder {
		/*
		 * SENSOR CONSTANTS
		 * 
		 * Sensor-related constants - pulled from datasheets for the sensors and
		 * gearboxes
		 */
		static public final int ENC_PULSE_PER_REV = 2048; // TalonFX integrated
															// sensor
		static public final int WHEEL_ENC_COUNTS_PER_WHEEL_REV = ENC_PULSE_PER_REV; // Assume
																					// 1-1
																					// gearing
																					// for
																					// now
		static public final double WHEEL_ENC_WHEEL_REVS_PER_COUNT = 1.0 / ((double) (WHEEL_ENC_COUNTS_PER_WHEEL_REV));
		static public final int STEER_ENC_COUNTS_PER_MODULE_REV = 4096; // CANCoder
		static public final double steer_ENC_MODULE_REVS_PER_COUNT = 1.0 / ((double) (STEER_ENC_COUNTS_PER_MODULE_REV));
	}

	public static final class Intake {
		public static final int INTAKE_EXTEND = 2;
		public static final int INTAKE_RETRACT = 3;
		public static final int INTAKE_ROLLER = 15;
	}

	public static final class Shooter {
		public static final int PUSHER_FORWARD = 0;
		public static final int PUSHER_REVERSE = 1;
		public static final int SHOOTER_MOTOR = 13;
		public static final int SHOOTER_BOOT = 18;
		public static final double SHOOT_MOTOR_GEAR_RATIO = 1.0;
		public static final double RPM_TO_COUNTS_PER_100MS = Encoder.ENC_PULSE_PER_REV / 600.0 / SHOOT_MOTOR_GEAR_RATIO;
	}
}
