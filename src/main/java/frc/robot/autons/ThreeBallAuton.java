package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonPhase;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequencePhase;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.autons.ThreeBallAutonPhase.*;

enum ThreeBallAutonPhase implements IAutonPhase {
    NEUTRAL(-999),
    DRIVE1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED)),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH1(-999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL1(1, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL2(-999, new SwerveDriveReq(SwerveDriveState.AIM), new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH2(-999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH2(-999, new ShooterReq(ShooterState.RESETPUNCH)),
    BOOT1(-999, new ShooterReq(ShooterState.BOOT));

    int pathIndex;
    SequencePhase state;

    ThreeBallAutonPhase(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonPhase> sequence) {
        return IAutonPhase.getPath(sequence, pathIndex);
    }
}

public class ThreeBallAuton extends BaseAutonSequence<ThreeBallAutonPhase> {

    int ballsShot = 0;

    public ThreeBallAuton(ThreeBallAutonPhase neutralState, ThreeBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        Robot.swerveDrive.setTargetShootRotationAngle();

        switch (getPhase()) {
            case NEUTRAL:
                break;
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
                ballsShot = 0;
//                setInitialPoseFromCurrentPath();
                if (Robot.shooter.getShooterClosedLoopError() < 100 && getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                setNextPhase(PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL2);
                    Robot.limelight.resetLimelightGlobalValues();
                }
                break;
            case SHOOTBALL2:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfPhase() > 500
                        && Robot.limelight.isTargetAcquired() && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextPhase(PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if (ballsShot == 3 && this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(NEUTRAL);
                } else if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(BOOT1);
                }
                break;
            case BOOT1:
                if (this.getTimeSinceStartOfPhase() > 150) {
                    setNextPhase(SHOOTBALL2);
                }
                break;
        }
        updatePhase();
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}