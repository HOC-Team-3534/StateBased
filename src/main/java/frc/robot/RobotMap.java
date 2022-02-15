package frc.robot;

import static frc.robot.Constants.BACK_LEFT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.BACK_LEFT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.BACK_RIGHT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.BACK_RIGHT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.FRONT_LEFT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.FRONT_LEFT_MODULE_STEER_OFFSET;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_DRIVE_MOTOR;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_ENCODER;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_MOTOR;
import static frc.robot.Constants.FRONT_RIGHT_MODULE_STEER_OFFSET;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	/**
	 * EXAMPLE public static DoubleSolenoid elevatorCylinderOne; //first value ->
	 * PCM A, CHANNEL 0, 1
	 */

	public static SwerveModule m_frontLeftModule;
	public static SwerveModule m_frontRightModule;
	public static SwerveModule m_backLeftModule;
	public static SwerveModule m_backRightModule;

	public static PneumaticsControlModule m_climbPCM;
	
	public static DoubleSolenoid m_l1Claw;
	public static DoubleSolenoid m_h2Claw;
	public static DoubleSolenoid m_l3Claw;
	public static DoubleSolenoid m_h4Claw;

	public static DigitalInput m_l1Switch;
	public static DigitalInput m_h2Switch;
	public static DigitalInput m_l3Switch;
	public static DigitalInput m_h4Switch;

	public static WPI_TalonFX m_climbMotor;

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


		ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

		// There are 4 methods you can call to create your swerve modules.
		// The method you use depends on what motors you are using.
		//
		// Mk3SwerveModuleHelper.createFalcon500(...)
		// Your module has two Falcon 500s on it. One for steering and one for driving.
		//
		// Mk3SwerveModuleHelper.createNeo(...)
		// Your module has two NEOs on it. One for steering and one for driving.
		//
		// Mk3SwerveModuleHelper.createFalcon500Neo(...)
		// Your module has a Falcon 500 and a NEO on it. The Falcon 500 is for driving
		// and the NEO is for steering.
		//
		// Mk3SwerveModuleHelper.createNeoFalcon500(...)
		// Your module has a NEO and a Falcon 500 on it. The NEO is for driving and the
		// Falcon 500 is for steering.
		//
		// Similar helpers also exist for Mk4 modules using the Mk4SwerveModuleHelper
		// class.

		// By default we will use Falcon 500s in standard configuration. But if you use
		// a different configuration or motors
		// you MUST change it. If you do not, your code will crash on startup.
		// FIXME Setup motor configuration

		// m_frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
		// 		// This parameter is optional, but will allow you to see the current state of
		// 		// the module on the dashboard.
		// 		tab.getLayout("Front Left Module", BuiltInLayouts.kList)
		// 				.withSize(2, 4)
		// 				.withPosition(0, 0),
		// 		// This can either be STANDARD or FAST depending on your gear configuration
		// 		Mk4SwerveModuleHelper.GearRatio.L2,
		// 		// This is the ID of the drive motor
		// 		FRONT_LEFT_MODULE_DRIVE_MOTOR,
		// 		// This is the ID of the steer motor
		// 		FRONT_LEFT_MODULE_STEER_MOTOR,
		// 		// This is the ID of the steer encoder
		// 		FRONT_LEFT_MODULE_STEER_ENCODER,
		// 		// This is how much the steer encoder is offset from true zero (In our case,
		// 		// zero is facing straight forward)
		// 		FRONT_LEFT_MODULE_STEER_OFFSET);

		// // We will do the same for the other modules
		// m_frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
		// 		tab.getLayout("Front Right Module", BuiltInLayouts.kList)
		// 				.withSize(2, 4)
		// 				.withPosition(2, 0),
		// 		Mk4SwerveModuleHelper.GearRatio.L2,
		// 		FRONT_RIGHT_MODULE_DRIVE_MOTOR,
		// 		FRONT_RIGHT_MODULE_STEER_MOTOR,
		// 		FRONT_RIGHT_MODULE_STEER_ENCODER,
		// 		FRONT_RIGHT_MODULE_STEER_OFFSET);

		// m_backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
		// 		tab.getLayout("Back Left Module", BuiltInLayouts.kList)
		// 				.withSize(2, 4)
		// 				.withPosition(4, 0),
		// 		Mk4SwerveModuleHelper.GearRatio.L2,
		// 		BACK_LEFT_MODULE_DRIVE_MOTOR,
		// 		BACK_LEFT_MODULE_STEER_MOTOR,
		// 		BACK_LEFT_MODULE_STEER_ENCODER,
		// 		BACK_LEFT_MODULE_STEER_OFFSET);

		// m_backRightModule = Mk4SwerveModuleHelper.createFalcon500(
		// 		tab.getLayout("Back Right Module", BuiltInLayouts.kList)
		// 				.withSize(2, 4)
		// 				.withPosition(6, 0),
		// 		Mk4SwerveModuleHelper.GearRatio.L2,
		// 		BACK_RIGHT_MODULE_DRIVE_MOTOR,
		// 		BACK_RIGHT_MODULE_STEER_MOTOR,
		// 		BACK_RIGHT_MODULE_STEER_ENCODER,
		// 		BACK_RIGHT_MODULE_STEER_OFFSET);

		navx = new AHRS(SPI.Port.kMXP);

		m_l1Switch = new DigitalInput(Constants.L1_SWITCH);
		m_h2Switch = new DigitalInput(Constants.H2_SWITCH);
		m_l3Switch = new DigitalInput(Constants.L3_SWITCH);
		m_h4Switch = new DigitalInput(Constants.H4_SWITCH);

		m_l1Claw = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.L1_FORWARD, Constants.L1_REVERSE);
		m_h2Claw = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.H2_FORWARD, Constants.H2_REVERSE);
		m_l3Claw = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.L3_FORWARD, Constants.L3_REVERSE);
		m_h4Claw = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.H4_FORWARD, Constants.H4_REVERSE);

		m_climbMotor = new WPI_TalonFX(Constants.CLIMB_ARM_MOTOR);
		m_climbMotor.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0);
		m_climbMotor.configRemoteFeedbackFilter(Constants.CLIMB_ARM_ENCODER, RemoteSensorSource.TalonFX_SelectedSensor, 0);
		m_climbMotor.setNeutralMode(NeutralMode.Brake);

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
