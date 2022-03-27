package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import frc.robot.Constants;
import frc.robot.RobotContainer.Buttons;
import frc.robot.RobotContainer.Axes;
import frc.robot.RobotMap;
import frc.robot.autons.pathplannerfollower.CalculatedDriveVelocities;
import frc.robot.autons.pathplannerfollower.PathStateController;
import frc.robot.subsystems.parent.BaseDriveSubsystem;

public class SwerveDrive extends BaseDriveSubsystem {

	private double frontLeft_stateAngle = 0.0,
			frontRight_stateAngle = 0.0,
			backLeft_stateAngle = 0.0,
			backRight_stateAngle = 0.0;

	PIDController xPID = new PIDController(20,0,0);
	PIDController yPID = new PIDController(20,0,0);
	PIDController rotPID = new PIDController(8,0,0);

	PathStateController pathStateController = new PathStateController(xPID, yPID, rotPID);

	public SwerveDrive() {
		super(RobotMap.m_frontLeftModule, RobotMap.m_frontRightModule, RobotMap.m_backLeftModule,
				RobotMap.m_backRightModule);
		setPathStateController(pathStateController);
	}

	PIDController limelightPID = new PIDController(0.2, 0.0, 0.0);
	Rotation2d targetShootRotationAngle = new Rotation2d();

	@Override
	public void process() {

		super.process();
		setTargetShootRotationAngle();

		if (getStateRequiringName() == "DRIVE") {
			drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
					true);
		} else if (getStateRequiringName() == "CREEP") {
			drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
					Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
					Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_CREEP_RADIANS_PER_SECOND,
					true);
		} else if (getStateRequiringName() == "WAITNSPIN" || getStateRequiringName() == "PUNCH"
				|| getStateRequiringName() == "RETRACT") {
			if (RobotMap.limelight.isTargetAcquired() && Buttons.SHOOT.getButton()) {
				double normalizedAngleError = getTargetShootRotationAngleError().getDegrees() % 3.0;
				double pidOutput = limelightPID.calculate(-normalizedAngleError,0.0);
				drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
						Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
						pidOutput * Constants.MAX_ANGULAR_VELOCITY_CREEP_RADIANS_PER_SECOND,
						true);
			}else{
				if(Buttons.Creep.getButton()){
					drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
							Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
							Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_CREEP_RADIANS_PER_SECOND,
							true);
				}else{
					drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
							Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
							Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
							true);
				}
			}
		} else if (isStatePathFollowing()) {
			if (getStateFirstRunThrough()) {
				// TODO check if the start of the path is near current odometry for safety
			}
			if (this.getPathStateController().getPathPlannerFollower() != null) {
				driveOnPath();
			} else {
				System.out.println(
						"DRIVE PATH NOT SET. MUST CREATE PATHPLANNERFOLLOWER IN AUTON AND SET IN SWERVEDRIVE SUBSYSTEM");
			}
		} else {
			neutral();
		}
	}

	public void setTargetShootRotationAngle() {
		Rotation2d limelightOffset = new Rotation2d(RobotMap.limelight.getHorizontalAngleOffset() / 180 * Math.PI);
		this.targetShootRotationAngle = getGyroHeading().minus(limelightOffset);
	}

	public Rotation2d getTargetShootRotationAngleError(){ return targetShootRotationAngle.minus(getGyroHeading()); }

	@Override
	public Rotation2d getGyroHeading() {
		return RobotMap.navx.getRotation2d().plus(this.getGyroOffset());
	}

	@Override
	public void resetGyro() {
		RobotMap.navx.reset();
	}

	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		var swerveModuleStates = getSwerveDriveKinematics().toSwerveModuleStates(
				fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
						xSpeed, ySpeed, rot, getGyroHeading())
						: new ChassisSpeeds(xSpeed, ySpeed, rot));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.MAX_VELOCITY_METERS_PER_SECOND);
		if (Math.abs(swerveModuleStates[0].speedMetersPerSecond) + Math.abs(swerveModuleStates[1].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[2].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[3].speedMetersPerSecond) > 0.001) {
			frontLeft_stateAngle = swerveModuleStates[0].angle.getRadians();
			frontRight_stateAngle = swerveModuleStates[1].angle.getRadians();
			backLeft_stateAngle = swerveModuleStates[2].angle.getRadians();
			backRight_stateAngle = swerveModuleStates[3].angle.getRadians();
		}
		RobotMap.m_frontLeftModule.set(swerveModuleStates[0].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
				frontLeft_stateAngle);
		RobotMap.m_frontRightModule.set(swerveModuleStates[1].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
				frontRight_stateAngle);
		RobotMap.m_backLeftModule.set(swerveModuleStates[2].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
				backLeft_stateAngle);
		RobotMap.m_backRightModule.set(swerveModuleStates[3].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
				backRight_stateAngle);
	}

	public void driveAutonomously(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		var swerveModuleStates = getSwerveDriveKinematics().toSwerveModuleStates(
				fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
						xSpeed, ySpeed, rot, getGyroHeading())
						: new ChassisSpeeds(xSpeed, ySpeed, rot));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates,
				Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS);
		if (Math.abs(swerveModuleStates[0].speedMetersPerSecond) + Math.abs(swerveModuleStates[1].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[2].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[3].speedMetersPerSecond) > 0.001) {
			frontLeft_stateAngle = swerveModuleStates[0].angle.getRadians();
			frontRight_stateAngle = swerveModuleStates[1].angle.getRadians();
			backLeft_stateAngle = swerveModuleStates[2].angle.getRadians();
			backRight_stateAngle = swerveModuleStates[3].angle.getRadians();
		}
		RobotMap.m_frontLeftModule.set(swerveModuleStates[0].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS * Constants.MAX_VOLTAGE
				* Constants.AUTON_MAX_VELOCITY_RATIO,
				frontLeft_stateAngle);
		RobotMap.m_frontRightModule.set(swerveModuleStates[1].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS * Constants.MAX_VOLTAGE
				* Constants.AUTON_MAX_VELOCITY_RATIO,
				frontRight_stateAngle);
		RobotMap.m_backLeftModule.set(swerveModuleStates[2].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS * Constants.MAX_VOLTAGE
				* Constants.AUTON_MAX_VELOCITY_RATIO,
				backLeft_stateAngle);
		RobotMap.m_backRightModule.set(swerveModuleStates[3].speedMetersPerSecond
				/ Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS * Constants.MAX_VOLTAGE
				* Constants.AUTON_MAX_VELOCITY_RATIO,
				backRight_stateAngle);
	}

	private void driveOnPath() {
		CalculatedDriveVelocities velocities = this.getPathStateController()
				.getVelocitiesAtCurrentState(this.getSwerveDriveOdometry(), this.getGyroHeading());

		Translation2d currentPosition = getSwerveDriveOdometry().getPoseMeters().getTranslation();
		// System.out.println(String.format("Current Odometry [ X: %.2f Y:%.2f ] Heading
		// [ Rot (radians): %.2f ]", currentPosition.getX(), currentPosition.getY(),
		// getGyroHeading().getRadians()));
		// System.out.println("Current Velocity Calculations: " +
		// velocities.toString());
		driveAutonomously(velocities.getXVel(), velocities.getYVel(), velocities.getRotVel(), true);
	}

	@Override
	public void neutral() {
		drive(0.0, 0.0, 0.0, false);
	}

	@Override
	public boolean abort() {
		drive(0.0, 0.0, 0.0, false);
		return true;
	}
}
