package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

import static frc.robot.autons.FiveBallAutonPhase.*;

enum FiveBallAutonPhase implements ISequencePhase {
    NEUTRAL,
    DRIVE1(0, SwerveDriveState.DRIVE_AUTONOMOUSLY, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL1(ShooterState.UPTOSPEED),
    PUNCH1(ShooterState.PUNCH),
    RESETPUNCH1(ShooterState.RESETPUNCH),
    PICKUPBALL1(1, SwerveDriveState.DRIVE_AUTONOMOUSLY, IntakeState.KICKOUT, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL2(ShooterState.UPTOSPEED, IntakeState.HOLDPOSITION),
    PUNCH2(ShooterState.PUNCH, IntakeState.RETRACT),
    RESETPUNCH2(ShooterState.RESETPUNCH),
    PICKUPBALL2(2, SwerveDriveState.DRIVE_AUTONOMOUSLY, IntakeState.KICKOUT),
    WAITFORINTAKE(IntakeState.HOLDPOSITION),
    PICKUPBALL3(3, SwerveDriveState.DRIVE_AUTONOMOUSLY, IntakeState.HOLDPOSITION, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL3(ShooterState.UPTOSPEED, IntakeState.HOLDPOSITION),
    PUNCH3(ShooterState.PUNCH, IntakeState.RETRACT),
    RESETPUNCH3(ShooterState.RESETPUNCH);

    SequencePhase phase;
    
    FiveBallAutonPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    FiveBallAutonPhase(int pathIndex, ISubsystemState... states) {
        phase = new SequencePhase(pathIndex, states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
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