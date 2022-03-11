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

public class TwoBallAuton extends BaseAutonSequence<TwoBallAutonState> {

    int ballsShot = 0;

    public TwoBallAuton(TwoBallAutonState neutralState, TwoBallAutonState startState, BaseDriveSubsystem driveSubsystem, PathPlannerFollower path0) {
        super(neutralState, startState, driveSubsystem, new PathPlannerFollower[]{path0});
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(TwoBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (((ballsShot == 0 && this.getTimeSinceStartOfState() > 1000) || (ballsShot == 1 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(TwoBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(TwoBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 2){
                        setNextState(TwoBallAutonState.NEUTRAL);
                    }else{
                        setNextState(TwoBallAutonState.SHOOTBALL1);
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

enum TwoBallAutonState implements IAutonState {
    NEUTRAL(false, -999),
    PICKUPBALL1(true, 0, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL1(false, -999, Robot.shooter, Robot.intake),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    TwoBallAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        if(this.pathIndex > 0 && pathIndex < sequence.getPaths().size()){
            return sequence.getPaths().get(pathIndex);
        }
        System.out.println("ERROR: Tried to get path for state that doesn't have a valid path");
        return null;
    }

    @Override
    public boolean isPathFollowing() {
        return isPathFollowing;
    }
}