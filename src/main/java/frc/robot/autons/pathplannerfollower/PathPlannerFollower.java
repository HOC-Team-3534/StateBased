package frc.robot.autons.pathplannerfollower;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

public class PathPlannerFollower {

    private final String PATH_FILE_NAME;
    private PathPlannerTrajectory path;
    private long START_TIME;

    public PathPlannerFollower(String pathName){
        PATH_FILE_NAME = pathName;
        long load_start = System.currentTimeMillis();
        loadPath(PATH_FILE_NAME);
        System.out.println(String.format("Path: [%s] took %d milliseconds.", pathName, System.currentTimeMillis() - load_start));
    }

    private void loadPath(String pathName){
        this.path = PathPlanner.loadPath(pathName, Constants.MAX_VELOCITY_METERS_PER_SECOND_AUTONOMOUS, Constants.MAX_ACCELERATION_METERS_PER_SECOND_PER_SECOND);
        System.out.println(path.toString());
    }

    public PathPlannerTrajectory.PathPlannerState getState(double seconds){
        return (PathPlannerTrajectory.PathPlannerState) path.sample(seconds);
    }

    public PathPlannerTrajectory.PathPlannerState getCurrentState(){
        double timeSinceStart = (double)(System.currentTimeMillis() - START_TIME) / 1000.0;
        return (PathPlannerTrajectory.PathPlannerState) path.sample(timeSinceStart);
    }

    public Translation2d getInitialPosition(){
        return path.getInitialState().poseMeters.getTranslation();
    }

    public Rotation2d getInitialHolonomic() { return path.getInitialState().holonomicRotation;}

    public double getRemainingTimeSeconds(){
        double timeSinceStart = (double) (System.currentTimeMillis() - START_TIME) / 1000.0;
        return path.getTotalTimeSeconds() - timeSinceStart;
    }

    public void resetStart(){
        START_TIME = System.currentTimeMillis();
    }

    public boolean isFinished() {
        double timeSinceStart = (double) (System.currentTimeMillis() - START_TIME) / 1000.0;
        return timeSinceStart >= path.getTotalTimeSeconds();
    }
}
