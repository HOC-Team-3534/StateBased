package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
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

	public static AnalogInput m_climbEncoder;

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
				Constants.FRONT_LEFT_MODULE_DRIVE_MOTOR,
				// This is the ID of the steer motor
				Constants.FRONT_LEFT_MODULE_STEER_MOTOR,
				// This is the ID of the steer encoder
				Constants.FRONT_LEFT_MODULE_STEER_ENCODER,
				// This is how much the steer encoder is offset from true zero (In our case,
				// zero is facing straight forward)
				Constants.FRONT_LEFT_MODULE_STEER_OFFSET);

		// We will do the same for the other modules
		m_frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Front Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(2, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				Constants.FRONT_RIGHT_MODULE_DRIVE_MOTOR,
				Constants.FRONT_RIGHT_MODULE_STEER_MOTOR,
				Constants.FRONT_RIGHT_MODULE_STEER_ENCODER,
				Constants.FRONT_RIGHT_MODULE_STEER_OFFSET);

		m_backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(4, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				Constants.BACK_LEFT_MODULE_DRIVE_MOTOR,
				Constants.BACK_LEFT_MODULE_STEER_MOTOR,
				Constants.BACK_LEFT_MODULE_STEER_ENCODER,
				Constants.BACK_LEFT_MODULE_STEER_OFFSET);

		m_backRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(6, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				Constants.BACK_RIGHT_MODULE_DRIVE_MOTOR,
				Constants.BACK_RIGHT_MODULE_STEER_MOTOR,
				Constants.BACK_RIGHT_MODULE_STEER_ENCODER,
				Constants.BACK_RIGHT_MODULE_STEER_OFFSET);

		navx = new AHRS(SPI.Port.kMXP);

		m_l1Switch = new DigitalInput(Constants.L1_SWITCH);
		m_h2Switch = new DigitalInput(Constants.H2_SWITCH);
		m_l3Switch = new DigitalInput(Constants.L3_SWITCH);
		m_h4Switch = new DigitalInput(Constants.H4_SWITCH);

		m_climbPCM = new PneumaticsControlModule(Constants.CLIMB_PCM);

		m_l1Claw = m_climbPCM.makeDoubleSolenoid(Constants.L1_EXTEND, Constants.L1_RETRACT);
		m_h2Claw = m_climbPCM.makeDoubleSolenoid(Constants.H2_EXTEND, Constants.H2_RETRACT);
		m_l3Claw = m_climbPCM.makeDoubleSolenoid(Constants.L3_EXTEND, Constants.L3_RETRACT);
		m_h4Claw = m_climbPCM.makeDoubleSolenoid(Constants.H4_EXTEND, Constants.H4_RETRACT);

		m_climbEncoder = new AnalogInput(Constants.CLIMB_ENCODER);

		m_climbMotor = new WPI_TalonFX(Constants.CLIMB_ARM_MOTOR);
		m_climbMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
		m_climbMotor.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy
				.valueOf((int) ((m_climbEncoder.getVoltage() - Constants.CLIMB_ANALOG_VOLTAGE_OFFSET)
						/ Constants.MAX_ANALOG_VOLTAGE * Constants.CLIMB_ARM_ROTATIONS_TO_FALCON_TICKS)));
		m_climbMotor.configMotionCruiseVelocity(Constants.MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
		m_climbMotor.configMotionAcceleration(Constants.MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
	}
}
