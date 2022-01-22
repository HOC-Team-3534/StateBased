package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static WPI_TalonFX frontLeftDrive; // 1
	public static WPI_TalonFX frontLeftRotate; // 2
	public static WPI_TalonFX backLeftDrive; // 3
	public static WPI_TalonFX backLeftRotate; // 4
	public static WPI_TalonFX backRightDrive; // 5
	public static WPI_TalonFX backRightRotate; // 6
	public static WPI_TalonFX frontRightDrive; // 7
	public static WPI_TalonFX frontRightRotate; // 8

	public static WPI_TalonSRX frontRightEncoder; //?
	public static WPI_TalonSRX frontLeftEncoder; //?
	public static WPI_TalonSRX backRightEncoder; //?
	public static WPI_TalonSRX backLeftEncoder; //?

	/**
	 * EXAMPLE public static DoubleSolenoid elevatorCylinderOne; //first value ->
	 * PCM A, CHANNEL 0, 1
	 */

	public static AHRS navx;

	public static final double spikeCurrent = 7.0;

	public static final double maxVelocity = 6.6; // meters per second
	public static final double maxAngularVelocity = Math.PI * 6; // radians per second

	// Wheel Encoder Calculations
	// public static final double typicalAcceleration = 1.0; //meters per second per
	// second
	// public static final double robotMass = 50; //kg
	public static final double wheelDiameter = .1524; // measured in meters
	public static final double gearRatio = 1 / 7.71;
	public static final int ticksPerMotorRotation = 2048; // 2048 for Falcon500 (old ecnoders 1440 if in talon, 360 if
															// into roboRIO)
	// public static final double driveWheelTorque = (wheelDiameter / 2) *
	// (robotMass / 4 * typicalAcceleration) * Math.sin(90);
	public static final double falconMaxRPM = 6380; // - 1 / 0.0007351097 * driveWheelTorque;
	public static final double maxTicksPer100ms = falconMaxRPM * ticksPerMotorRotation / 60 / 10;
	public static final double distancePerMotorRotation = gearRatio * wheelDiameter * Math.PI;
	public static final double encoderVelocityToWheelVelocity = ticksPerMotorRotation / 10 / distancePerMotorRotation; // encoder
																														// ticks
																														// per
																														// 100ms
																														// to
																														// meters
																														// per
																														// second
	// public static final double inchesPerCountMultiplier = wheelDiameter * Math.PI
	// / ticksPerRotation;
	// public static final double codesPer100MillisToInchesPerSecond =
	// inchesPerCountMultiplier * 10;

	public static void init() {

		frontLeftDrive = new WPI_TalonFX(1);
		frontLeftDrive.config_kF(0, 0.05, 0);
		frontLeftDrive.config_kP(0, 3, 0);
		frontLeftDrive.config_kI(0, 0, 0);
		frontLeftDrive.config_kD(0, 80, 0);
		frontLeftDrive.setNeutralMode(NeutralMode.Brake);

		frontLeftRotate = new WPI_TalonFX(2);
		frontLeftRotate.config_kF(0, 0.05, 0);
		frontLeftRotate.config_kP(0, 3, 0);
		frontLeftRotate.config_kI(0, 0, 0);
		frontLeftRotate.config_kD(0, 80, 0);
		frontLeftRotate.setNeutralMode(NeutralMode.Brake);

		backLeftDrive = new WPI_TalonFX(3);
		backLeftDrive.config_kF(0, 0.05, 0);
		backLeftDrive.config_kP(0, 3, 0);
		backLeftDrive.config_kI(0, 0, 0);
		backLeftDrive.config_kD(0, 80, 0);
		backLeftDrive.setNeutralMode(NeutralMode.Brake);

		backLeftRotate = new WPI_TalonFX(4);
		backLeftRotate.config_kF(0, 0.05, 0);
		backLeftRotate.config_kP(0, 3, 0);
		backLeftRotate.config_kI(0, 0, 0);
		backLeftRotate.config_kD(0, 80, 0);
		backLeftRotate.setNeutralMode(NeutralMode.Brake);

		backRightDrive = new WPI_TalonFX(5);
		backRightDrive.setInverted(true);
		backRightDrive.config_kF(0, 0.05, 0);
		backRightDrive.config_kP(0, 3, 0);
		backRightDrive.config_kI(0, 0, 0);
		backRightDrive.config_kD(0, 80, 0);
		backRightDrive.setNeutralMode(NeutralMode.Brake);

		backRightRotate = new WPI_TalonFX(6);
		backRightRotate.setInverted(true);
		backRightRotate.config_kF(0, 0.05, 0);
		backRightRotate.config_kP(0, 3, 0);
		backRightRotate.config_kI(0, 0, 0);
		backRightRotate.config_kD(0, 80, 0);
		backRightRotate.setNeutralMode(NeutralMode.Brake);

		frontRightDrive = new WPI_TalonFX(7);
		frontRightDrive.setInverted(true);
		frontRightDrive.config_kF(0, 0.05, 0);
		frontRightDrive.config_kP(0, 3, 0);
		frontRightDrive.config_kI(0, 0, 0);
		frontRightDrive.config_kD(0, 80, 0);
		frontRightDrive.setNeutralMode(NeutralMode.Brake);

		frontRightRotate = new WPI_TalonFX(8);
		frontRightRotate.setInverted(true);
		frontRightRotate.config_kF(0, 0.05, 0);
		frontRightRotate.config_kP(0, 3, 0);
		frontRightRotate.config_kI(0, 0, 0);
		frontRightRotate.config_kD(0, 80, 0);
		frontRightRotate.setNeutralMode(NeutralMode.Brake);

		frontLeftEncoder = new WPI_TalonSRX(9);

		frontRightEncoder = new WPI_TalonSRX(10);

		backLeftEncoder = new WPI_TalonSRX(11);

		backRightEncoder = new WPI_TalonSRX(12);

		// private final SwerveModule frontLeftModule = new Mk2SwerveModuleBuilder(
        //     new Vector2(TRACKWIDTH / 2.0, WHEELBASE / 2.0))
        //     .angleEncoder(new AnalogInput(RobotMap.DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER), FRONT_LEFT_ANGLE_OFFSET)
        //     .angleMotor(new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
        //             Mk2SwerveModuleBuilder.MotorType.NEO)
        //     .driveMotor(new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
        //             Mk2SwerveModuleBuilder.MotorType.NEO)
        //     .build();

		navx = new AHRS(SPI.Port.kMXP);

	}

	public enum DelayToOff {

		/**
		 * the delay in seconds until the solenoids for certain ports turn off
		 * below is just an example from 2019
		 */

		elevator_stage1a(3.0),
		elevator_stage1b(3.0),
		elevator_stage2(3.0),
		elevator_floor(3.0),
		hatchPanelApparatus_collapsed(2.0),
		hatchIntake_hold(2.0),
		armExtend_extended(3.0),
		armLift_collapsed(2.0),
		armLift_mid(2.0),
		armLift_up(2.0);

		public double time;

		private DelayToOff(double time) {

			this.time = time * 1000;

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
