package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

import java.util.Arrays;
import java.util.List;

public class OneBallAuton extends BaseAutonSequence<OneBallAutonState> {

    public OneBallAuton(OneBallAutonState neutralState, OneBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(OneBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if ((this.getTimeSinceStartOfState() > 1000)
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(OneBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(OneBallAutonState.RESETPUNCH1);
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(OneBallAutonState.NEUTRAL);
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

enum OneBallAutonState implements IAutonState {
    NEUTRAL(false, -999),
    DRIVE1(true, 0, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL1(false, -999, Robot.shooter),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    OneBallAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
        this.isPathFollowing = isPathFollowing;
        this.pathIndex = pathIndex;
        requiredSubsystems = Arrays.asList(subsystems);
    }

    @Override
    public List<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends IState> sequence) {
        return IState.requireSubsystems(sequence, requiredSubsystems, this);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        return IAutonState.getPath(sequence, pathIndex);
    }

    @Override
    public boolean isPathFollowing() {
        return isPathFollowing;
    }
}