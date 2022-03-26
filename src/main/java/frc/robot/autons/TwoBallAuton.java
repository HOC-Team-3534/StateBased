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

    public TwoBallAuton(TwoBallAutonState neutralState, TwoBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                ballsShot = 0;
                if(this.getPlannerFollower().isFinished()){
                    setNextState(TwoBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (((ballsShot == 0 && this.getTimeSinceStartOfState() > 500) || (ballsShot == 1 && this.getTimeSinceStartOfState() > 500))
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
                if (this.getTimeSinceStartOfState() > 1500) {
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
    SHOOTBALL1(false, -999, Robot.shooter),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter,Robot.intake);

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