package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;
import frc.statebasedcontroller.sequence.fundamental.BaseAutonSequence;
import frc.statebasedcontroller.sequence.fundamental.IAutonPhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

import static frc.robot.autons.OneBallAutonPhase.*;

import frc.pathplanner.PathPlannerFollower;

enum OneBallAutonPhase implements IAutonPhase {
    NEUTRAL(-999),
    DRIVE1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED)),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH1(-999, new ShooterReq(ShooterState.RESETPUNCH));

    int pathIndex;
    SequencePhase state;

    OneBallAutonPhase(int pathIndex, SubsystemRequirement... requirements) {
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

public class OneBallAuton extends BaseAutonSequence<OneBallAutonPhase> {

    public OneBallAuton(OneBallAutonPhase neutralState, OneBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
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
                if ((this.getTimeSinceStartOfPhase() > 1000)
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextPhase(PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH1);
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(NEUTRAL);
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