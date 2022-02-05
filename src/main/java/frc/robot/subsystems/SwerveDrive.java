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

	public static final double kMaxSpeed = 1.0; // 3 meters per second, ~4.95 actual max
	public static final double kMaxAngularSpeed = Math.PI / 2; // 1/2 rotation per second

	public static final double ksideLength = 0.59719;
	// Locations for the swerve drive modules relative to the robot center.
	Translation2d m_frontLeftLocation = new Translation2d(0.2986, 0.2986);
	Translation2d m_frontRightLocation = new Translation2d(0.2986, -0.2986);
	Translation2d m_backLeftLocation = new Translation2d(-0.2986, 0.2986);
	Translation2d m_backRightLocation = new Translation2d(-0.2986, -0.2986);

	// Creating my kinematics object using the module locations
	SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
			m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);
	// Creating my odometry object from the kinematics object. Here,
	// our starting pose is 5 meters along the long end of the field and in the
	// center of the field along the short end, facing forward.
	SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics,
			getGyroHeading(), new Pose2d(5.0, 13.5, new Rotation2d()));

	@Override
	public void process() {
		isStillRequired();
		if (getStateRequiringName() == "DRIVE") {
			drive(Axes.Drive_LeftRight.getAxis() * SwerveDrive.kMaxSpeed,
					Axes.Drive_ForwardBackward.getAxis() * SwerveDrive.kMaxSpeed,
					Axes.Drive_Rotation.getAxis() * SwerveDrive.kMaxAngularSpeed,
					false);
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
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
		if (swerveModuleStates[0].speedMetersPerSecond + swerveModuleStates[1].speedMetersPerSecond
				+ swerveModuleStates[2].speedMetersPerSecond + swerveModuleStates[3].speedMetersPerSecond < .03) {
			RobotMap.m_frontLeftDrive.set(ControlMode.Velocity, 0);
			RobotMap.m_frontRightDrive.set(ControlMode.Velocity, 0);
			RobotMap.m_backLeftDrive.set(ControlMode.Velocity, 0);
			RobotMap.m_backRightDrive.set(ControlMode.Velocity, 0);
		} else {
			RobotMap.m_frontLeftModule.set(swerveModuleStates[0].speedMetersPerSecond
					/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
					swerveModuleStates[0].angle.getRadians());
			RobotMap.m_frontRightModule.set(swerveModuleStates[1].speedMetersPerSecond
					/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
					swerveModuleStates[1].angle.getRadians());
			RobotMap.m_backLeftModule.set(swerveModuleStates[2].speedMetersPerSecond
					/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
					swerveModuleStates[2].angle.getRadians());
			RobotMap.m_backRightModule.set(swerveModuleStates[3].speedMetersPerSecond
					/ Constants.MAX_VELOCITY_METERS_PER_SECOND * Constants.MAX_VOLTAGE,
					swerveModuleStates[3].angle.getRadians());
		}
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
}
