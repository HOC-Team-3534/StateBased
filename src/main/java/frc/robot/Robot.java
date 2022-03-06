// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.autons.Auton;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.sequences.Burp;
import frc.robot.sequences.SequenceProcessor;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static SwerveDrive swerveDrive;
	public static Shooter shooter;
	public static Burp burp;
	public static Intake intake;
	public static Climber climber;
	public static SequenceProcessor sequenceProcessor;
	public static RobotContainer robotContainer;

	public static boolean isAutonomous = false;

	private int loopCnt = 0;
	private int loopPeriod = 0;
	private int logCounter = 0;

	public static double designatedLoopPeriod = 20;

	public static BaseAutonSequence<? extends IAutonState> chosenAuton;
	private SendableChooser<Auton> sendableChooser;

	@Override
	public void robotInit() {

		RobotMap.init();

		robotContainer = new RobotContainer();

		swerveDrive = new SwerveDrive();

		shooter = new Shooter(d -> 611.5 + 2982.5 * Math.log(d));

		intake = new Intake();

		climber = new Climber();

		sequenceProcessor = new SequenceProcessor();

		sendableChooser.setDefaultOption("Station 1: 4 Ball", Auton.STATION3_4BALL);

		SmartDashboard.putData(sendableChooser);
	}
	

	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledInit() {
		swerveDrive.forceRelease();
		shooter.forceRelease();
		intake.forceRelease();
		climber.forceRelease();
	}

	@Override
	public void disabledPeriodic() {
		log();
	}

	@Override
	public void autonomousInit() {
		this.chosenAuton = sendableChooser.getSelected().getAuton();
		chosenAuton.start();
	}

	@Override
	public void autonomousPeriodic() {
		log();

		isAutonomous = this.isAutonomous();

		long prevLoopTime = 0;

		while (this.isAutonomous() && this.isEnabled()) {

			log();

			long currentTime = System.currentTimeMillis();

			if (currentTime - prevLoopTime >= designatedLoopPeriod) {

				loopPeriod = (int) (currentTime - prevLoopTime);
				prevLoopTime = currentTime;
				loopCnt++;

				chosenAuton.process();
				// run processes

				/** Run subsystem process methods here */
				swerveDrive.process();
				shooter.process();
				intake.process();
				//climber.process();
			}

			Timer.delay(0.001);
		}
	}

	@Override
	public void teleopInit() {
		swerveDrive.forceRelease();
		shooter.forceRelease();
		intake.forceRelease();
		climber.forceRelease();
	}

	@Override
	public void teleopPeriodic() {

		log();

		isAutonomous = this.isAutonomous();

		long prevLoopTime = 0;

		while (this.isTeleop() && this.isEnabled()) {

			log();

			long currentTime = System.currentTimeMillis();

			if (currentTime - prevLoopTime >= designatedLoopPeriod) {

				loopPeriod = (int) (currentTime - prevLoopTime);
				prevLoopTime = currentTime;
				loopCnt++;

				sequenceProcessor.process();
				// run processes

				/** Run subsystem process methods here */
				swerveDrive.process();
				shooter.process();
				intake.process();
				climber.process();
			}

			Timer.delay(0.001);
		}
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

	public void log() {

		logCounter++;

		if (logCounter > 5) {

			SmartDashboard.putNumber("Encoder Voltage", RobotMap.m_climbEncoder.getVoltage());
			SmartDashboard.putBoolean("L1 switch 1", RobotMap.m_l1Switch.get());
			SmartDashboard.putBoolean("L1 switch 2", RobotMap.m_h2Switch.get());
			SmartDashboard.putBoolean("L3 switch 1", RobotMap.m_l3Switch.get());
			SmartDashboard.putBoolean("L3 switch 2", RobotMap.m_h4Switch.get());
			SmartDashboard.putNumber("Gyro", swerveDrive.getGyroHeading().getRadians());

			SmartDashboard.putNumber("tx", RobotMap.limelight.getHorOffset());
			SmartDashboard.putNumber("ty", RobotMap.limelight.getPixelAngle());
			SmartDashboard.putNumber("distance", RobotMap.limelight.getDistance());
			
			SmartDashboard.putNumber("target rotation", swerveDrive.getTargetShootRotation().getRadians());

			logCounter = 0;
		}

	}
}
