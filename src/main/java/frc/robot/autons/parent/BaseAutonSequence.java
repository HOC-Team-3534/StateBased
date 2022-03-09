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
    final List<String> pathNames;

    public BaseAutonSequence(S neutralState, S startState, BaseDriveSubsystem driveSubsystem, String... pathNames) {
        super(neutralState, startState);
        this.baseDriveSubsystem = driveSubsystem;
        this.pathNames = Arrays.asList(pathNames);
    }

    protected void createPathPlannerFollower(String pathName){
        this.pathPlannerFollower = new PathPlannerFollower(pathName);
    }

    public PathPlannerFollower getPlannerFollower(){
        return pathPlannerFollower;
    }

    protected void setPathPlannerFollowerAtStartOfState(boolean setInitialPositionAndHeading){
        if(this.getStateFirstRunThrough()) {
            System.out.println("THE REQUESTED PATH NAME IS: " + this.getState().getPathName(this));
            createPathPlannerFollower(this.getState().getPathName(this));
            this.getBaseDriveSubsystem().setPathPlannerFollower(getPlannerFollower(), setInitialPositionAndHeading);
        }
    }

    protected BaseDriveSubsystem getBaseDriveSubsystem(){return baseDriveSubsystem;}

    public List<String> getPathNames(){return pathNames;}
}