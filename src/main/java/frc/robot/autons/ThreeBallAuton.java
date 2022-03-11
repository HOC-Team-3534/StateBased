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

public class ThreeBallAuton extends BaseAutonSequence<ThreeBallAutonState> {

    int ballsShot = 0;

    public ThreeBallAuton(ThreeBallAutonState neutralState, ThreeBallAutonState startState, BaseDriveSubsystem driveSubsystem, PathPlannerFollower path0) {
        super(neutralState, startState, driveSubsystem, path0);
    }

    @Override
    public void process() {

        switch (getState()) {
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
                setInitialPoseFromCurrentPath();
                if (RobotMap.shooter.getClosedLoopError() < 100 && getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(ThreeBallAutonState.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(ThreeBallAutonState.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if(ballsShot == 3){
                    setNextState(ThreeBallAutonState.NEUTRAL);
                }else if(this.getTimeSinceStartOfState() > 500){
                    setNextState(ThreeBallAutonState.SHOOTBALL2);
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

enum ThreeBallAutonState implements IAutonState {
    NEUTRAL(false, -999),
    SHOOTBALL1(false, 0, Robot.shooter),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter),
    PICKUPBALL1(true, 0, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL2(false, -999, Robot.shooter),
    PUNCH2(false, -999, Robot.shooter),
    RESETPUNCH2(false, -999, Robot.shooter,Robot.intake);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    ThreeBallAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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