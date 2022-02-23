package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.kauailabs.navx.frc.AHRS;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import static frc.robot.Constants.*;

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

	public static PneumaticsControlModule m_mainPCM;
	public static PneumaticsControlModule m_climbPCM;

	public static WPI_TalonFX shooter;
	public static DoubleSolenoid pusher;

	public static WPI_TalonSRX m_intakeRoller;
	public static DoubleSolenoid m_intakeKickers;

	public static DigitalInput m_l1Switch;
	public static DigitalInput m_h2Switch;
	public static DigitalInput m_l3Switch;
	public static DigitalInput m_h4Switch;

	public static AnalogInput m_climbEncoder;

	public static DoubleSolenoid m_l1Claw;
	public static DoubleSolenoid m_h2Claw;
	public static DoubleSolenoid m_l3Claw;
	public static DoubleSolenoid m_h4Claw;

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
		
		m_frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				// This parameter is optional, but will allow you to see the current state of
				// the module on the dashboard.
				tab.getLayout("Front Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(0, 0),
				// This can either be STANDARD or FAST depending on your gear configuration
				Mk4SwerveModuleHelper.GearRatio.L2,
				// This is the ID of the drive motor
				FRONT_LEFT_MODULE_DRIVE_MOTOR,
				// This is the ID of the steer motor
				FRONT_LEFT_MODULE_STEER_MOTOR,
				// This is the ID of the steer encoder
				FRONT_LEFT_MODULE_STEER_ENCODER,
				// This is how much the steer encoder is offset from true zero (In our case,
				// zero is facing straight forward)
				FRONT_LEFT_MODULE_STEER_OFFSET);

		// We will do the same for the other modules
		m_frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Front Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(2, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				FRONT_RIGHT_MODULE_DRIVE_MOTOR,
				FRONT_RIGHT_MODULE_STEER_MOTOR,
				FRONT_RIGHT_MODULE_STEER_ENCODER,
				FRONT_RIGHT_MODULE_STEER_OFFSET);

		m_backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(4, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				BACK_LEFT_MODULE_DRIVE_MOTOR,
				BACK_LEFT_MODULE_STEER_MOTOR,
				BACK_LEFT_MODULE_STEER_ENCODER,
				BACK_LEFT_MODULE_STEER_OFFSET);

		m_backRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(6, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				BACK_RIGHT_MODULE_DRIVE_MOTOR,
				BACK_RIGHT_MODULE_STEER_MOTOR,
				BACK_RIGHT_MODULE_STEER_ENCODER,
				BACK_RIGHT_MODULE_STEER_OFFSET);

		m_mainPCM = new PneumaticsControlModule(MAIN_PCM);
		m_climbPCM = new PneumaticsControlModule(CLIMB_PCM);

		shooter = new WPI_TalonFX(SHOOTER_MOTOR);
		shooter.setInverted(true);
		shooter.config_kF(0, 0.05);
		shooter.config_kP(0, 0.2);
		shooter.config_kD(0, 3.5);

		pusher = m_mainPCM.makeDoubleSolenoid(PUSHER_FORWARD, PUSHER_REVERSE);

		m_intakeRoller = new WPI_TalonSRX(INTAKE_ROLLER);
		m_intakeRoller.setInverted(true);

		m_intakeKickers = m_mainPCM.makeDoubleSolenoid(INTAKE_EXTEND, INTAKE_RETRACT);

		m_l1Switch = new DigitalInput(L1_SWITCH);
		m_h2Switch = new DigitalInput(H2_SWITCH);
		m_l3Switch = new DigitalInput(L3_SWITCH);
		m_h4Switch = new DigitalInput(H4_SWITCH);

		m_l1Claw = m_climbPCM.makeDoubleSolenoid(L1_EXTEND, L1_RETRACT);
		m_h2Claw = m_climbPCM.makeDoubleSolenoid(H2_EXTEND, H2_RETRACT);
		m_l3Claw = m_climbPCM.makeDoubleSolenoid(L3_EXTEND, L3_RETRACT);
		m_h4Claw = m_climbPCM.makeDoubleSolenoid(H4_EXTEND, H4_RETRACT);

		m_climbEncoder = new AnalogInput(CLIMB_ENCODER);

		m_climbMotor = new WPI_TalonFX(CLIMB_ARM_MOTOR);
		m_climbMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
		m_climbMotor.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy
				.valueOf((int) ((m_climbEncoder.getVoltage() - CLIMB_ANALOG_VOLTAGE_OFFSET)
						/ MAX_ANALOG_VOLTAGE * CLIMB_ARM_ROTATIONS_TO_FALCON_TICKS)));
		m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
		m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
		m_climbMotor.config_kP(0, 0.025);
		m_climbMotor.config_kI(0, 0.0);
		m_climbMotor.config_kD(0, 0.0);
		m_climbMotor.config_kF(0, 0.0);

		navx = new AHRS(SPI.Port.kMXP);
	}
}
