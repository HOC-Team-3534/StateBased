package frc.robot.sequences.pathplannerfollower;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Constants;

public class PathPlannerFollower {

    private final String PATH_FILE_NAME;
    private Trajectory path;
    private final long START_TIME;

    public PathPlannerFollower(String pathName){
        PATH_FILE_NAME = pathName;
        loadPath(PATH_FILE_NAME);
        START_TIME = System.currentTimeMillis();
    }

    private void loadPath(String pathName){
        this.path = PathPlanner.loadPath(pathName, Constants.MAX_VELOCITY_METERS_PER_SECOND, Constants.MAX_ACCELERATION_METERS_PER_SECOND_PER_SECOND);
    }

    public Trajectory.State getState(double seconds){
        return path.sample(seconds);
    }

    public Trajectory.State getCurrentState(){
        return path.sample((double)(System.currentTimeMillis() - START_TIME) / 1000.0);
    }
}
