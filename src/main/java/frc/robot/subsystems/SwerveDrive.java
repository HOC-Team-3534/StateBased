package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import frc.robot.RobotMap;

public class SwerveDrive implements SystemInterface {

    public static final double kMaxSpeed = 3.0; // 3 meters per second
    public static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second

    // Locations for the swerve drive modules relative to the robot center.
    Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
    Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
    Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
    Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);

    private final SwerveModule m_frontLeft = new SwerveModule(RobotMap.frontLeftDrive, RobotMap.frontLeftRotate, RobotMap.frontLeftEncoder); //2,1
    private final SwerveModule m_frontRight = new SwerveModule(RobotMap.frontRightDrive, RobotMap.frontRightRotate, RobotMap.frontRightEncoder); //8.7
    private final SwerveModule m_backLeft = new SwerveModule(RobotMap.backLeftDrive, RobotMap.backLeftRotate, RobotMap.backLeftEncoder); //4,3
    private final SwerveModule m_backRight = new SwerveModule(RobotMap.backRightDrive, RobotMap.backRightRotate, RobotMap.backRightEncoder); //6,5

    // Creating my kinematics object using the module locations
    SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
        m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
    );
    // Creating my odometry object from the kinematics object. Here,
    // our starting pose is 5 meters along the long end of the field and in the
    // center of the field along the short end, facing forward.
    SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics,
        getGyroHeading(), new Pose2d(5.0, 13.5, new Rotation2d()));

    @Override
    public void process() {
      // TODO Auto-generated method stub
      
    }
    
	public Rotation2d getGyroHeading() {
		return RobotMap.navx.getRotation2d();
	}

    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
        var swerveModuleStates = m_kinematics.toSwerveModuleStates(
            fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                xSpeed, ySpeed, rot, getGyroHeading())
                : new ChassisSpeeds(xSpeed, ySpeed, rot)
        );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_backLeft.setDesiredState(swerveModuleStates[2]);
        m_backRight.setDesiredState(swerveModuleStates[3]);
      }

    public void updateOdometry() {
        m_odometry.update(
            getGyroHeading(),
            m_frontLeft.getState(),
            m_frontRight.getState(),
            m_backLeft.getState(),
            m_backRight.getState()
        );
    }
}
