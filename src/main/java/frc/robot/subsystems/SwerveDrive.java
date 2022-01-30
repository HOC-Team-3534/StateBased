package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Axes;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

public class SwerveDrive extends BaseSubsystem {

    public static final double kMaxSpeed = 1.0; // 3 meters per second, ~4.95 actual max
    public static final double kMaxAngularSpeed = Math.PI / 2; // 1/2 rotation per second

    public static final double ksideLength = 0.59719;
    // Locations for the swerve drive modules relative to the robot center.
    Translation2d m_frontLeftLocation = new Translation2d(0.2986, 0.2986);
    Translation2d m_frontRightLocation = new Translation2d(0.2986, -0.2986);
    Translation2d m_backLeftLocation = new Translation2d(-0.2986, 0.2986);
    Translation2d m_backRightLocation = new Translation2d(-0.2986, -0.2986);

    private final SwerveModule m_frontLeft = new SwerveModule(RobotMap.frontLeftDrive, RobotMap.frontLeftRotate,
            RobotMap.frontLeftEncoder); // 2,1
    private final SwerveModule m_frontRight = new SwerveModule(RobotMap.frontRightDrive, RobotMap.frontRightRotate,
            RobotMap.frontRightEncoder); // 8.7
    private final SwerveModule m_backLeft = new SwerveModule(RobotMap.backLeftDrive, RobotMap.backLeftRotate,
            RobotMap.backLeftEncoder); // 4,3
    private final SwerveModule m_backRight = new SwerveModule(RobotMap.backRightDrive, RobotMap.backRightRotate,
            RobotMap.backRightEncoder); // 6,5

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
                m_backRight.getState());
    }
}
