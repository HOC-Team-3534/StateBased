package frc.robot.subsystems;

<<<<<<< HEAD
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.controller.PIDController;
=======
>>>>>>> 1.3
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer.Axes;
import frc.robot.RobotMap;
import frc.robot.sequences.pathplannerfollower.CalculatedDriveVelocities;
import frc.robot.sequences.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.pathplannerfollower.PathStateController;
import frc.robot.subsystems.parent.BaseSubsystem;

<<<<<<< HEAD
import frc.robot.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

=======
>>>>>>> 1.3
public class SwerveDrive extends BaseSubsystem {

	String[] pathFollowingStateStrings = {"PICKUPBALL1"};
	Set<String> pathFollowingStates = new HashSet<>(Arrays.asList(pathFollowingStateStrings));

	PIDController xPID = new PIDController(2, 0, 0); //correct itself in 1/p seconds
	PIDController yPID = new PIDController(2, 0, 0);
	PIDController rotPID = new PIDController(2, 0, 0);

	PathStateController pathStateController = new PathStateController(xPID, yPID, rotPID);

	private double frontLeft_stateAngle = 0.0,
			frontRight_stateAngle = 0.0,
			backLeft_stateAngle = 0.0,
			backRight_stateAngle = 0.0;
			
	// Locations for the swerve drive modules relative to the robot center.
	private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
			// Front left
			new Translation2d(Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0,
					Constants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
			// Front right
			new Translation2d(Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0,
					-Constants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
			// Back left
			new Translation2d(-Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0,
					Constants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
			// Back right
			new Translation2d(-Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0,
					-Constants.DRIVETRAIN_WHEELBASE_METERS / 2.0));

	SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics,
			getGyroHeading(), new Pose2d(0.0, 0.0, new Rotation2d()));

	@Override
	public void process() {

		super.process();
		updateOdometry();

		if (getStateRequiringName() == "DRIVE") {
			drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
					true);
		}else if (getStateRequiringName() == "CREEP") {
			drive(Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
					Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_CREEP_METERS_PER_SECOND,
					Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_CREEP_RADIANS_PER_SECOND,
					true);
		}else if(pathFollowingStates.contains(getStateRequiringName())){
			if(getStateFirstRunThrough()){
				//TODO check if the start of the path is near current odometry for safety
			}
			if(this.pathStateController.getPathPlannerFollower() != null) {
				driveOnPath();
			}else{
				System.out.println("DRIVE PATH NOT SET. MUST CREATE PATHPLANNERFOLLOWER IN AUTON AND SET IN SWERVEDRIVE SUBSYSTEM");
			}
		}else{
			neutral();
		}
	}

	public Rotation2d getGyroHeading() {
		return RobotMap.navx.getRotation2d();
	}

	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		var swerveModuleStates = m_kinematics.toSwerveModuleStates(
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

	private void driveOnPath() {
		if (!this.pathStateController.getPathPlannerFollower().isFinished()) {
			CalculatedDriveVelocities velocities = this.pathStateController
					.getVelocitiesAtCurrentState(this.getOdometry(), this.getGyroHeading());

			drive(velocities.getXVel(), velocities.getYVel(), velocities.getRotVel(), true);
		}else{
			neutral();
		}
	}

	public void setPathPlannerFollower(PathPlannerFollower ppf){
		this.pathStateController.setPathPlannerFollower(ppf);
	}

	public void updateOdometry() {

		m_odometry.update(
				getGyroHeading(),
				new SwerveModuleState(RobotMap.m_frontLeftModule.getDriveVelocity(),
						new Rotation2d(RobotMap.m_frontLeftModule.getSteerAngle())),
				new SwerveModuleState(RobotMap.m_frontRightModule.getDriveVelocity(),
						new Rotation2d(RobotMap.m_frontRightModule.getSteerAngle())),
				new SwerveModuleState(RobotMap.m_backLeftModule.getDriveVelocity(),
						new Rotation2d(RobotMap.m_backLeftModule.getSteerAngle())),
				new SwerveModuleState(RobotMap.m_backRightModule.getDriveVelocity(),
						new Rotation2d(RobotMap.m_backRightModule.getSteerAngle())));
	}

	public void resetOdometry(double x, double y){
		m_odometry.resetPosition(new Pose2d(x, y, new Rotation2d()), getGyroHeading());
	}

	public SwerveDriveOdometry getOdometry(){
		return m_odometry;
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
