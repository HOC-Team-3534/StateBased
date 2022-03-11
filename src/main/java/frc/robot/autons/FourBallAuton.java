package frc.robot.autons;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.*;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

public class FourBallAuton extends BaseAutonSequence<FourBallAutonState> {

    int ballsShot = 0;

    public FourBallAuton(FourBallAutonState neutralState, FourBallAutonState startState, BaseDriveSubsystem driveSubsystem, PathPlannerFollower path0, PathPlannerFollower path1) {
        super(neutralState, startState, driveSubsystem, path0, path1);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FourBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if ((ballsShot == 0 || (ballsShot == 1 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FourBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FourBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 2){
                        setNextState(FourBallAutonState.PICKUPBALL2);
                    }else{
                        setNextState(FourBallAutonState.SHOOTBALL1);
                    }
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FourBallAutonState.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 2 || (ballsShot == 3 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FourBallAutonState.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FourBallAutonState.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 4){
                        setNextState(FourBallAutonState.NEUTRAL);
                    }else{
                        setNextState(FourBallAutonState.SHOOTBALL2);
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

enum FourBallAutonState implements IAutonState {
    NEUTRAL(false, -999),
    PICKUPBALL1(true, 0, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL1(false, -999, Robot.shooter, Robot.intake),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter),
    PICKUPBALL2(true, 1, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL2(false, -999, Robot.shooter, Robot.intake),
    PUNCH2(false, -999, Robot.shooter),
    RESETPUNCH2(false, -999, Robot.shooter);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    FourBallAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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