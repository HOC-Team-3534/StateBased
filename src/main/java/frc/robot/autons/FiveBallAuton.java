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

import static frc.robot.autons.FiveBallAutonPhase.*;

enum FiveBallAutonPhase implements IAutonPhase {
    NEUTRAL(-999),
    DRIVE1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED)),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH1(-999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL1(1, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL2(-999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH2(-999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH2(-999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL2(2, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT)),
    WAITFORINTAKE(-999, new IntakeReq(IntakeState.HOLDPOSITION)),
    PICKUPBALL3(3, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.HOLDPOSITION), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL3(-999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH3(-999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH3(-999, new ShooterReq(ShooterState.RESETPUNCH));

    int pathIndex;
    SequencePhase state;

    FiveBallAutonPhase(int pathIndex, SubsystemRequirement... requirements) {
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

public class FiveBallAuton extends BaseAutonSequence<FiveBallAutonPhase> {

    int ballsShot = 0;

    public FiveBallAuton(FiveBallAutonPhase neutralState, FiveBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
//                setInitialPoseFromCurrentPath();
                ballsShot = 0;
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
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfPhase() > 500))
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
                if (ballsShot == 3) {
                    setNextPhase(PICKUPBALL2);
                } else if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(SHOOTBALL2);
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(WAITFORINTAKE);
                }
                break;
            case WAITFORINTAKE:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(PICKUPBALL3);
                }
            case PICKUPBALL3:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL3);
                }
                break;
            case SHOOTBALL3:
                if ((ballsShot == 3 || (ballsShot == 4 && this.getTimeSinceStartOfPhase() > 500))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextPhase(PUNCH3);
                }
                break;
            case PUNCH3:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH3);
                    ballsShot++;
                }
                break;
            case RESETPUNCH3:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    if (ballsShot == 5) {
                        setNextPhase(NEUTRAL);
                    } else {
                        setNextPhase(SHOOTBALL3);
                    }
                }
                break;
            case NEUTRAL:
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