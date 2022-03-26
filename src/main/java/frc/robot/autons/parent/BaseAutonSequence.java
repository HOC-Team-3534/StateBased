package frc.robot.autons.parent;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.subsystems.parent.BaseDriveSubsystem;

import java.util.Arrays;
import java.util.List;

public abstract class BaseAutonSequence<S extends IAutonState> extends BaseSequence<S> {

    PathPlannerFollower pathPlannerFollower;
    final BaseDriveSubsystem baseDriveSubsystem;
    List<PathPlannerFollower> paths;

    public BaseAutonSequence(S neutralState, S startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState);
        this.baseDriveSubsystem = driveSubsystem;
    }

    public void setPathPlannerFollowers(PathPlannerFollower... pathPlannerFollowers){
        this.paths = Arrays.asList(pathPlannerFollowers);
    }

    public PathPlannerFollower getPlannerFollower(){
        return pathPlannerFollower;
    }

    protected void setPathPlannerFollowerAtStartOfState(boolean setInitialPositionAndHeading){
        if(this.getStateFirstRunThrough()) {
            this.pathPlannerFollower = this.getState().getPath(this);
            this.pathPlannerFollower.resetStart();
            this.getBaseDriveSubsystem().setPathPlannerFollower(getPlannerFollower(), setInitialPositionAndHeading);
        }
    }

    protected void setInitialPoseFromCurrentPath(){
        if(this.getStateFirstRunThrough()){
            this.getBaseDriveSubsystem().setInitalPoseFromFirstPathPlannerFollower(this.getState().getPath(this));
        }
    }

    protected BaseDriveSubsystem getBaseDriveSubsystem(){return baseDriveSubsystem;}

    public List<PathPlannerFollower> getPaths(){return paths;}
}