package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

import java.util.Arrays;
import java.util.List;

public class StraightTestAuton extends BaseAutonSequence<StraightTestAutonState> {

    int ballsShot = 0;

    public StraightTestAuton(StraightTestAutonState neutralState, StraightTestAutonState startState, BaseDriveSubsystem driveSubsystem, PathPlannerFollower path0) {
        super(neutralState, startState, driveSubsystem, path0);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(StraightTestAutonState.NEUTRAL);
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

enum StraightTestAutonState implements IAutonState {
    NEUTRAL(false, -999),
    PICKUPBALL1(true, 0, Robot.swerveDrive);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    StraightTestAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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