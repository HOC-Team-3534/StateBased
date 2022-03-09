package frc.robot.autons.pathplannerfollower;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants;

public class PathPlannerFollower {

    private final String PATH_FILE_NAME;
    private PathPlannerTrajectory path;
    private final long START_TIME;

    public PathPlannerFollower(String pathName){
        PATH_FILE_NAME = pathName;
        loadPath(PATH_FILE_NAME);
        START_TIME = System.currentTimeMillis();
    }

    private void loadPath(String pathName){
        this.path = (PathPlannerTrajectory) PathPlanner.loadPath(pathName, Constants.MAX_VELOCITY_METERS_PER_SECOND, Constants.MAX_ACCELERATION_METERS_PER_SECOND_PER_SECOND);
        System.out.println(path.toString());
    }

    public PathPlannerTrajectory.PathPlannerState getState(double seconds){
        return (PathPlannerTrajectory.PathPlannerState) path.sample(seconds);
    }

    public PathPlannerTrajectory.PathPlannerState getCurrentState(){
        double timeSinceStart = (double)(System.currentTimeMillis() - START_TIME) / 1000.0;
        return (PathPlannerTrajectory.PathPlannerState) path.sample(timeSinceStart);
    }

    public Pose2d getInitialPosition(){
        return path.getInitialState().poseMeters;
    }

    public double getRemainingTimeSeconds(){
        double timeSinceStart = (double) (System.currentTimeMillis() - START_TIME) / 1000.0;
        return path.getTotalTimeSeconds() - timeSinceStart;
    }

    public boolean isFinished() {
        double timeSinceStart = (double) (System.currentTimeMillis() - START_TIME) / 1000.0;
        return timeSinceStart >= path.getTotalTimeSeconds();
    }
}
