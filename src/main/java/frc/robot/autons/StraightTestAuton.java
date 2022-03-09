package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

import java.util.Arrays;
import java.util.List;

public class StraightTestAuton extends BaseAutonSequence<StraightTestAutonState> {

    int ballsShot = 0;

    public StraightTestAuton(StraightTestAutonState neutralState, StraightTestAutonState startState, BaseDriveSubsystem driveSubsystem, String path0) {
        super(neutralState, startState, driveSubsystem, new String[]{path0});
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
        for (BaseSubsystem subsystem : requiredSubsystems) {
            if (subsystem.isRequiredByAnother(sequence)) {
                return false;
            }
        }
        for (BaseSubsystem subsystem : requiredSubsystems) {
            subsystem.require(sequence, this);
        }
        return true;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getPathName(BaseAutonSequence<? extends IAutonState> sequence) {
        if(this.pathIndex > 0 && pathIndex < sequence.getPathNames().size()){
            return sequence.getPathNames().get(pathIndex);
        }
        System.out.println("ERROR: Tried to get path name for state that doesn't have a valid path");
        return "";
    }

    @Override
    public boolean isPathFollowing() {
        return isPathFollowing;
    }
}