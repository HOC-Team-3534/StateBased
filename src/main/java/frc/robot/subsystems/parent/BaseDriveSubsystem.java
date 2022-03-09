package frc.robot.subsystems.parent;

import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.autons.pathplannerfollower.PathStateController;

public abstract class BaseDriveSubsystem extends BaseSubsystem implements IDriveSubsystem{

    final SwerveModule frontLeftModule, frontRightModule, backLeftModule, backRightModule;

    Rotation2d gyroOffset = new Rotation2d(0);

    PathStateController pathStateController;

    // Locations for the swerve drive modules relative to the robot center.
    private final SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(
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

    SwerveDriveOdometry swerveDriveOdometry = new SwerveDriveOdometry(this.swerveDriveKinematics,
            getGyroHeading(), new Pose2d(0.0, 0.0, getGyroHeading()));

    public BaseDriveSubsystem(SwerveModule frontLeftModule, SwerveModule frontRightModule, SwerveModule backLeftModule, SwerveModule backRightModule){
        this.frontLeftModule = frontLeftModule;
        this.frontRightModule = frontRightModule;
        this.backLeftModule = backLeftModule;
        this.backRightModule = backRightModule;
    }

    public void process(){
        super.process();
        updateOdometry();
    }

    protected void setPathStateController(PathStateController psc){
        this.pathStateController = psc;
    }

    public PathStateController getPathStateController() {
        return pathStateController;
    }

    public void setPathPlannerFollower(PathPlannerFollower ppf, boolean setInitialPosition){
        this.getPathStateController().setPathPlannerFollower(ppf);
        if(setInitialPosition){
            Pose2d initialPosition = ppf.getInitialPosition();
            resetOdometry(initialPosition.getTranslation());
            gyroOffset = initialPosition.getRotation();
        }
    }

    public SwerveDriveKinematics getSwerveDriveKinematics() {
        return swerveDriveKinematics;
    }

    public SwerveDriveOdometry getSwerveDriveOdometry(){
        return swerveDriveOdometry;
    }

    public void updateOdometry() {

        getSwerveDriveOdometry().update(
                getGyroHeading(),
                new SwerveModuleState(this.frontLeftModule.getDriveVelocity(),
                        new Rotation2d(this.frontLeftModule.getSteerAngle())),
                new SwerveModuleState(this.frontRightModule.getDriveVelocity(),
                        new Rotation2d(this.frontRightModule.getSteerAngle())),
                new SwerveModuleState(this.backLeftModule.getDriveVelocity(),
                        new Rotation2d(this.backLeftModule.getSteerAngle())),
                new SwerveModuleState(this.backRightModule.getDriveVelocity(),
                        new Rotation2d(this.backRightModule.getSteerAngle())));
    }

    public void resetOdometry(Translation2d position){
        getSwerveDriveOdometry().resetPosition(new Pose2d(position.getX(), position.getY(), new Rotation2d()), getGyroHeading());
    }

    public Rotation2d getGyroOffset(){
        return this.gyroOffset;
    }

    protected boolean isStatePathFollowing(){
        if(Robot.isAutonomous && required){
            return ((IAutonState)this.stateRequiring).isPathFollowing();
        }
        return false;
    }

    public double getMetersFromLocation(Translation2d location){
        return getSwerveDriveOdometry().getPoseMeters().getTranslation().getDistance(location);
    }
}
