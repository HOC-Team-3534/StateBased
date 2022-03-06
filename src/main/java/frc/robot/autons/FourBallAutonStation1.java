package frc.robot.autons;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.sequences.parent.*;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;

public class FourBallAutonStation1 extends BaseAutonSequence<FourBallAutonStation1State> {

    int ballsShot = 0;

    public FourBallAutonStation1(FourBallAutonStation1State neutralState, FourBallAutonStation1State startState, BaseDriveSubsystem driveSubsystem, String path0, String path1) {
        super(neutralState, startState, driveSubsystem, new String[]{path0, path1});
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FourBallAutonStation1State.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (this.getTimeSinceStartOfState() > 500 && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FourBallAutonStation1State.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FourBallAutonStation1State.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 2){
                        setNextState(FourBallAutonStation1State.PICKUPBALL2);
                    }else{
                        setNextState(FourBallAutonStation1State.SHOOTBALL1);
                    }
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FourBallAutonStation1State.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if (this.getTimeSinceStartOfState() > 500 && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(FourBallAutonStation1State.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FourBallAutonStation1State.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 4){
                        setNextState(FourBallAutonStation1State.NEUTRAL);
                    }else{
                        setNextState(FourBallAutonStation1State.SHOOTBALL2);
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

enum FourBallAutonStation1State implements IAutonState {
    NEUTRAL(false, -999),
    PICKUPBALL1(true, 0, Robot.intake, Robot.swerveDrive),
    SHOOTBALL1(false, -999, Robot.shooter, Robot.intake),
    PUNCH1(false, -999, Robot.shooter),
    RESETPUNCH1(false, -999, Robot.shooter),
    PICKUPBALL2(true, 1, Robot.intake, Robot.swerveDrive),
    SHOOTBALL2(false, -999, Robot.shooter, Robot.intake),
    PUNCH2(false, -999, Robot.shooter),
    RESETPUNCH2(false, -999, Robot.shooter);

    boolean isPathFollowing;
    int pathIndex;
    List<BaseSubsystem> requiredSubsystems;

    FourBallAutonStation1State(boolean isPathFollowing, int pathIndex, BaseSubsystem... subsystems) {
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