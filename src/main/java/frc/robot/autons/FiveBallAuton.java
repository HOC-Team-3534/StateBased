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
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
//                setInitialPoseFromCurrentPath();
                ballsShot = 0;
                if (RobotMap.shooter.getClosedLoopError() < 100 && getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FiveBallAutonState.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if(ballsShot == 3){
                    setNextState(FiveBallAutonState.PICKUPBALL2);
                }else if(this.getTimeSinceStartOfState() > 500){
                    setNextState(FiveBallAutonState.SHOOTBALL2);
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.WAITFORINTAKE);
                }
                break;
            case WAITFORINTAKE:
                if(this.getTimeSinceStartOfState() > 500){
                    setNextState(FiveBallAutonState.PICKUPBALL3);
                }
            case PICKUPBALL3:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL3);
                }
                break;
            case SHOOTBALL3:
                if ((ballsShot == 3 || (ballsShot == 4 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FiveBallAutonState.PUNCH3);
                }
                break;
            case PUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH3);
                    ballsShot++;
                }
                break;
            case RESETPUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 5){
                        setNextState(FiveBallAutonState.NEUTRAL);
                    }else{
                        setNextState(FiveBallAutonState.SHOOTBALL3);
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

enum FiveBallAutonState implements IAutonState {
    NEUTRAL(false, -999),
    DRIVE1(true, 0, Robot.swerveDrive),
    SHOOTBALL1(false, -999, Robot.shooter),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter),
    PICKUPBALL1(true, 1, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL2(false, -999, Robot.shooter),
    PUNCH2(false, -999, Robot.shooter),
    RESETPUNCH2(false, -999, Robot.shooter, Robot.intake),
    PICKUPBALL2(true, 2, Robot.intake, Robot.swerveDrive),
    WAITFORINTAKE(false, -999, Robot.intake),
    PICKUPBALL3(true, 3, Robot.intake, Robot.swerveDrive, Robot.shooter),
    SHOOTBALL3(false, -999, Robot.shooter),
    PUNCH3(false, -999, Robot.shooter),
    RESETPUNCH3(false, -999, Robot.shooter, Robot.intake);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    FiveBallAutonState(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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