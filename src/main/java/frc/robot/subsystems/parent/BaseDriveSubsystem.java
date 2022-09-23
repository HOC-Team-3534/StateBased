package frc.robot.subsystems.parent;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.robot.Robot;
import frc.robot.Constants.DRIVE;
import frc.robot.RobotContainer.Axes;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.swervelib.SwerveConstants;
import frc.swervelib.SwerveInput;
import frc.swervelib.SwerveModule;
import frc.wpiClasses.QuadSwerveSim;

import java.util.ArrayList;

public abstract class BaseDriveSubsystem<SsS extends ISubsystemState> extends BaseSubsystem<SsS> implements IDriveSubsystem {
    static Rotation2d targetShootRotationAngle = new Rotation2d();
    // Locations for the swerve drive modules relative to the robot center.
    StateBasedSwerveDrivetrainModel dt;
    ArrayList<SwerveModule> modules = new ArrayList<SwerveModule>(QuadSwerveSim.NUM_MODULES);
    double frontLeft_stateAngle = 0.0,
            frontRight_stateAngle = 0.0,
            backLeft_stateAngle = 0.0,
            backRight_stateAngle = 0.0;
    PathPlannerFollower pathPlannerFollower;

    public BaseDriveSubsystem(StateBasedSwerveDrivetrainModel dt, SsS neutralState) {
        super(neutralState);
        this.dt = dt;
        modules = dt.getRealModules();
    }

    public void process() {
        dt.updateTelemetry();
        super.process();
        sendStates();
        if(RobotBase.isSimulation()){
        dt.update(DriverStation.isDisabled(), 13.2);
        }
    }

    public void setPathPlannerFollower(PathPlannerFollower ppf, boolean setInitialPosition) {
        this.pathPlannerFollower = ppf;
        if (setInitialPosition) {
            setInitalPoseFromFirstPathPlannerFollower(ppf);
        }
    }

    public PathPlannerFollower getPathPlannerFollower(){
        return pathPlannerFollower;
    }

    public void setInitalPoseFromFirstPathPlannerFollower(PathPlannerFollower ppf) {
        dt.setKnownState(ppf.getInitialState());
    }

    public SwerveModuleState[] getSwerveModuleStates() {
        return dt.getSwerveModuleStates();
    }

    public Vector2d getRobotCentricVelocity() {
        ChassisSpeeds chassisSpeeds = DRIVE.KINEMATICS.toChassisSpeeds(dt.getSwerveModuleStates());
        return new Vector2d(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond);
    }

    public Vector2d getTargetOrientedVelocity() {
        Vector2d robotCentricVelocity = getRobotCentricVelocity();
        robotCentricVelocity.rotate(180.0);
        return robotCentricVelocity;
    }

    public void setTargetShootRotationAngle() {
        //this.targetShootRotationAngle = getGyroHeading().plus(Robot.limelight.getLimelightShootProjection().getOffset());
        targetShootRotationAngle = dt.getGyroscopeRotation().plus(Robot.limelight.getHorizontalAngleOffset());
    }

    public Rotation2d getTargetShootRotationAngleError() {
        return targetShootRotationAngle.minus(dt.getGyroscopeRotation());
    }

    public Rotation2d getGyroRotation(){
        return dt.getGyroscopeRotation();
    }

    public void resetGyro(){
        dt.zeroGyroscope();
    }

    public Pose2d getPose(){
        return dt.getPose();
    }

    protected void setModuleStates(ChassisSpeeds chassisSpeeds){
        dt.setModuleStates(chassisSpeeds);
    }

    protected void setModuleStates(Axes x, Axes y, Axes rot){
        setFastSpeeds();
        dt.setModuleStates(new SwerveInput(x.getAxis(), y.getAxis(), rot.getAxis()));
    }

    protected void setModuleStatesCreep(Axes x, Axes y, Axes rot){
        setSlowSpeeds();
        dt.setModuleStates(new SwerveInput(x.getAxis(), y.getAxis(), rot.getAxis()));
    }

    protected void setModuleStatesCreepAim(Axes x, Axes y, double rot){
        setSlowSpeeds();
        dt.setModuleStates(new SwerveInput(x.getAxis(), y.getAxis(), rot));
    }

    public void setModuleStatesAutonomous() {
        dt.goToPose(this.getPathPlannerFollower().getCurrentState());
    }

    protected void setFastSpeeds(){
        dt.setMaxSpeeds(DRIVE.MAX_FWD_REV_SPEED_FAST, DRIVE.MAX_STRAFE_SPEED_FAST, DRIVE.MAX_ROTATE_SPEED_FAST);
    }

    protected void setSlowSpeeds(){
        dt.setMaxSpeeds(DRIVE.MAX_FWD_REV_SPEED_SLOW, DRIVE.MAX_STRAFE_SPEED_SLOW, DRIVE.MAX_ROTATE_SPEED_SLOW);
    }

    protected void sendStates() {
        SwerveModuleState[] states = dt.getSwerveModuleStates();

        if (states != null) {
        SwerveDriveKinematics.desaturateWheelSpeeds(states, SwerveConstants.MAX_FWD_REV_SPEED_MPS);

        modules.get(0).set(states[0].speedMetersPerSecond / SwerveConstants.MAX_FWD_REV_SPEED_MPS * SwerveConstants.MAX_VOLTAGE, states[0].angle.getRadians());
        modules.get(1).set(states[1].speedMetersPerSecond / SwerveConstants.MAX_FWD_REV_SPEED_MPS * SwerveConstants.MAX_VOLTAGE, states[1].angle.getRadians());
        modules.get(2).set(states[2].speedMetersPerSecond / SwerveConstants.MAX_FWD_REV_SPEED_MPS * SwerveConstants.MAX_VOLTAGE, states[2].angle.getRadians());
        modules.get(3).set(states[3].speedMetersPerSecond / SwerveConstants.MAX_FWD_REV_SPEED_MPS * SwerveConstants.MAX_VOLTAGE, states[3].angle.getRadians());

        dt.getPoseEstimator().update(dt.getGyroscopeRotation(), states[0], states[1], states[2], states[3]);
        }
    }
}
