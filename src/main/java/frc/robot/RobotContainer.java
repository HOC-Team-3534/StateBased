// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.concurrent.Callable;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	// private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

	final static XboxPlusPOV xbox1 = new XboxPlusPOV(0);
	final static XboxPlusPOV xbox2 = new XboxPlusPOV(1);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {

	}

	public static XboxPlusPOV getController1() {
		return xbox1;
	}

	public static XboxPlusPOV getController2() {
		return xbox2;
	}

	private static double deadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} else {
				return (value + deadband) / (1.0 - deadband);
			}
		} else {
			return 0;
		}
	}

	private static double modifyAxis(double value) {
		// Deadband
		value = deadband(value, 0.1);

		// Square the axis
		value = Math.copySign(value * value, value);

		return value;
	}

	public static enum Buttons {

		Shoot(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController2().getRightTriggerAxis() > 0.15;
			}
			
		}),
		Burp(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController2().getLeftTriggerAxis() > 0.15;
			}
			
		}),
		Creep(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController1().getLeftTriggerAxis() > 0.15;
			}
			
		}),
		Intake(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController1().getRightTriggerAxis() > 0.15;
			}

		}),
		Climb(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController2().getYButton();
			}
			
		}),
		ClimbPrep(new Callable<Boolean>(){

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return RobotContainer.getController2().getAButton();
			}
			
		});

		Callable<Boolean> callable;

		private Buttons(Callable<Boolean> callable){
	
			this.callable = callable;
	
		}
	
		public boolean getButton(){
	
			try{
	
				return callable.call().booleanValue();
	
			}catch(Exception ex){
	
				return false;
	
			}
		}

	}

	public static enum Axes {
		Drive_ForwardBackward(new Callable<Double>(){

		@Override public Double call()throws Exception{

		return -modifyAxis(RobotContainer.getController1().getLeftY());}

		}),
		Drive_LeftRight(new Callable<Double>(){

		@Override public Double call()throws Exception{

		return -modifyAxis(RobotContainer.getController1().getLeftX());}}),
		Drive_Rotation(new Callable<Double>(){@Override public Double call()throws Exception{

		return -modifyAxis(RobotContainer.getController1().getRightX());}});

		Callable<Double> callable;

		private Axes(Callable<Double> callable) {

			this.callable = callable;

		}

		public double getAxis() {

			try {

				return callable.call().doubleValue();

			} catch (Exception ex) {

				return 0.0;

			}
		}
	}
}
