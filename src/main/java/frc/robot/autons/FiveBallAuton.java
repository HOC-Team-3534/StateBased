package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.autons.FiveBallAutonState.*;

enum FiveBallAutonState implements IAutonState {
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
    SequenceState state;

    FiveBallAutonState(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        return IAutonState.getPath(sequence, pathIndex);
    }
}

public class FiveBallAuton extends BaseAutonSequence<FiveBallAutonState> {

    int ballsShot = 0;

    public FiveBallAuton(FiveBallAutonState neutralState, FiveBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if (this.getPlannerFollower().isFinished()) {
                    setNextState(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
//                setInitialPoseFromCurrentPath();
                ballsShot = 0;
                if (Robot.shooter.getShooterClosedLoopError() < 100 && getTimeSinceStartOfState() > 500) {
                    setNextState(PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                setNextState(PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextState(SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfState() > 500))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextState(PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if (ballsShot == 3) {
                    setNextState(PICKUPBALL2);
                } else if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(SHOOTBALL2);
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextState(WAITFORINTAKE);
                }
                break;
            case WAITFORINTAKE:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(PICKUPBALL3);
                }
            case PICKUPBALL3:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextState(SHOOTBALL3);
                }
                break;
            case SHOOTBALL3:
                if ((ballsShot == 3 || (ballsShot == 4 && this.getTimeSinceStartOfState() > 500))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextState(PUNCH3);
                }
                break;
            case PUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(RESETPUNCH3);
                    ballsShot++;
                }
                break;
            case RESETPUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    if (ballsShot == 5) {
                        setNextState(NEUTRAL);
                    } else {
                        setNextState(SHOOTBALL3);
                    }
                }
                break;
            case NEUTRAL:
                break;

        }
        updateState();
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}