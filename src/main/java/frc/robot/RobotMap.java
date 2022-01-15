package org.usfirst.frc3534.RobotBasic;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static WPI_TalonFX frontLeftMotor;		//1
	public static WPI_TalonFX backLeftMotor;		//2
	public static WPI_TalonFX backRightMotor;		//3
	public static WPI_TalonFX frontRightMotor;		//4
	public static WPI_TalonFX shooter; 				//5
	public static WPI_TalonFX shooterSlave;			//6
	//public static WPI_TalonSRX hood;				//7
	public static WPI_TalonSRX topBelt;				//8
	public static WPI_TalonSRX indexWheel;			//9
	public static WPI_TalonSRX intakeArm;			//10
	public static WPI_TalonSRX intakeRoller;		//11
	public static WPI_TalonSRX elevator;			//12
	public static WPI_TalonFX winch;				//13
	public static WPI_TalonSRX translator;			//14
	public static WPI_TalonSRX spinner;				//15

	public static ColorSensorV3 colorSensor;
	public static DigitalInput indexBottom;
	public static DigitalInput indexTop;
	public static DigitalInput shootCounter;

	public static Spark blinkin;

	/** EXAMPLE public static DoubleSolenoid elevatorCylinderOne;		//first value -> PCM A, CHANNEL 0, 1 */


	public static AHRS navx;

	public static final int shooterVelocityDrop = 0;

	public static final double spikeCurrent = 7.0;
	public static final double rollerSpikeCurrent = 55.0;
	
	public static final int elevator_maxHeight = 10000;
	public static final int elevatorCreepHeight = -500;
	public static final int winch_maxPosition = 0;
	public static final int winchLimit = 0;

	public static final double maxVelocity = 6.6; //meters per second
	public static final double maxAngularVelocity = Math.PI * 6; //radians per second

	// Wheel Encoder Calculations
	//public static final double typicalAcceleration = 1.0; //meters per second per second
	//public static final double robotMass = 50; //kg
	public static final double wheelDiameter = .1524; // measured in meters
	public static final double gearRatio = 1/7.71;
	public static final int ticksPerMotorRotation = 2048; // 2048 for Falcon500 (old ecnoders 1440 if in talon, 360 if into roboRIO)
	//public static final double driveWheelTorque = (wheelDiameter / 2) * (robotMass / 4 * typicalAcceleration) * Math.sin(90);
	public static final double falconMaxRPM = 6380; //- 1 / 0.0007351097 * driveWheelTorque;
	public static final double maxTicksPer100ms = falconMaxRPM * ticksPerMotorRotation / 60 / 10;
	public static final double distancePerMotorRotation = gearRatio * wheelDiameter * Math.PI;
	public static final double encoderVelocityToWheelVelocity =  ticksPerMotorRotation / 10 / distancePerMotorRotation; //encoder ticks per 100ms to meters per second
	//public static final double inchesPerCountMultiplier = wheelDiameter * Math.PI / ticksPerRotation;
	//public static final double codesPer100MillisToInchesPerSecond = inchesPerCountMultiplier * 10;

	public static void init() {

		frontLeftMotor = new WPI_TalonFX(1);
		frontLeftMotor.config_kF(0, 0.05, 0);
		frontLeftMotor.config_kP(0, 3, 0);
		frontLeftMotor.config_kI(0, 0, 0);
		frontLeftMotor.config_kD(0, 80, 0);
		frontLeftMotor.setNeutralMode(NeutralMode.Brake);

		backLeftMotor = new WPI_TalonFX(2);
		backLeftMotor.config_kF(0, 0.05, 0);
		backLeftMotor.config_kP(0, 3, 0);
		backLeftMotor.config_kI(0, 0, 0);
		backLeftMotor.config_kD(0, 80, 0);
		backLeftMotor.setNeutralMode(NeutralMode.Brake);

		backRightMotor = new WPI_TalonFX(3);
		backRightMotor.setInverted(true);
		backRightMotor.config_kF(0, 0.05, 0);
		backRightMotor.config_kP(0, 3, 0);
		backRightMotor.config_kI(0, 0, 0);
		backRightMotor.config_kD(0, 80, 0);
		backRightMotor.setNeutralMode(NeutralMode.Brake);

		frontRightMotor = new WPI_TalonFX(4);
		frontRightMotor.setInverted(true);
		frontRightMotor.config_kF(0, 0.05, 0);
		frontRightMotor.config_kP(0, 3, 0);
		frontRightMotor.config_kI(0, 0, 0);
		frontRightMotor.config_kD(0, 80, 0);
		frontRightMotor.setNeutralMode(NeutralMode.Brake);

		shooter = new WPI_TalonFX(5);
		shooterSlave = new WPI_TalonFX(6);
		shooterSlave.follow(shooter);
		shooter.setInverted(false);
		shooterSlave.setInverted(true);

		//hood = new WPI_TalonSRX(7);

		topBelt = new WPI_TalonSRX(8);
		topBelt.setInverted(true);
		topBelt.setNeutralMode(NeutralMode.Brake);

		indexWheel = new WPI_TalonSRX(9);
		indexWheel.setNeutralMode(NeutralMode.Brake);
		indexWheel.setInverted(true);

		intakeArm = new WPI_TalonSRX(10);
		intakeArm.setInverted(true);

		intakeRoller = new WPI_TalonSRX(11);

		elevator = new WPI_TalonSRX(12);
		//elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		//elevator.setSelectedSensorPosition(0, 0, 0);
		elevator.setInverted(true);
		elevator.setNeutralMode(NeutralMode.Brake);
		//elevator.setSensorPhase(true);
		//elevator.selectProfileSlot(0, 0);

		winch = new WPI_TalonFX(13);

		translator = new WPI_TalonSRX(14);
		translator.setNeutralMode(NeutralMode.Brake);

		spinner = new WPI_TalonSRX(15);
		
		colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
		shootCounter = new DigitalInput(4);
		indexBottom = new DigitalInput(1);
		indexTop = new DigitalInput(0);


		blinkin = new Spark(1);

		navx = new AHRS(SPI.Port.kMXP);

	}

	public enum DelayToOff{
	
		/** the delay in seconds until the solenoids for certain ports turn off
		  *	below is just an example from 2019 
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

		private DelayToOff(double time){

			this.time = time * 1000;

		}
	}

	public enum FunctionStateDelay{
	
		/** the delay in seconds between different states in the function switch statements
		  *	creates the wait time between cases in other words
		  *	below is just an example from 2019 (the examples should probably be removed 
		  *	when the next years copy of the code is made)
		  */

		cargoIntakeFloor_elevatorStage1A_to_armExtendExtended_rollerIntake(1.0),
		cargoShoot_shooterShoot_to_shooterStop(0.2),
		hatchPlace_hatchIntakeRelease_to_hatchPanelApparatusExtended(0.05), 
		hatchPlace_hatchPanelApparatusExtended_to_hatchPanelApparatusCollapsed(0.25),
		hatchPlace_hatchPanelApparatusCollapsed_to_hatchPlaceCompleted(3.0),
		xButtonReset_armLiftMid_to_armExtendCollapsed(1.0),
		intakeRoller_burpDelay(0.15);

		public double time;

		private FunctionStateDelay(double time){

			this.time = time * 1000;

		}
	}

	public enum PowerOutput{
	
		/** the power output in percentage for the different actions in the functions for the motors
		  * for example, an intake motor at a constant percentage of power while button pressed
		  *	below is just an example from 2019 (shooter was updated for RobotBasic)
		  */

		shooter_shooter_shootConstant(15500), //17750
		//shooter_shooter_shootInner(0.1127 * Math.pow((Robot.drive.getDistance()), 2) - (42.1417 * Robot.drive.getDistance()) + 17746.7581),
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

		private PowerOutput(double power){

			this.power = power;

		}

	}
}
