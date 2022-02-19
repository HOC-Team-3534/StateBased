package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.RobotContainer.Axes;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

import frc.robot.Constants;

public class SwerveDrive extends BaseSubsystem {

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
	// Creating my odometry object from the kinematics object. Here,
	// our starting pose is 5 meters along the long end of the field and in the
	// center of the field along the short end, facing forward.
	SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics,
			getGyroHeading(), new Pose2d(5.0, 13.5, new Rotation2d()));

	@Override
	public void process() {
		super.process();
		if (getStateRequiringName() == "DRIVE") {
			drive(Axes.Drive_LeftRight.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_ForwardBackward.getAxis() * Constants.MAX_VELOCITY_METERS_PER_SECOND,
					Axes.Drive_Rotation.getAxis() * Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
					true);
		}
	}

	@Override
	public void neutral() {
		drive(0.0, 0.0, 0.0, false);
	}

	public Rotation2d getGyroHeading() {
		return RobotMap.navx.getRotation2d();
	}

	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		var swerveModuleStates = m_kinematics.toSwerveModuleStates(
				fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
						ySpeed, xSpeed, rot, getGyroHeading())
						: new ChassisSpeeds(ySpeed, xSpeed, rot));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.MAX_VELOCITY_METERS_PER_SECOND);
		if (Math.abs(swerveModuleStates[0].speedMetersPerSecond + swerveModuleStates[1].speedMetersPerSecond
				+ swerveModuleStates[2].speedMetersPerSecond + swerveModuleStates[3].speedMetersPerSecond) > .001) {
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

	@Override
	public boolean abort() {
		// TODO Auto-generated method stub
		return false;
	}
}
