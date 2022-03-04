package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Robot;
import frc.robot.sequences.parent.BaseAutonSequence;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IAutonPathValues;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Autons extends BaseAutonSequence<AutonState, AutonPathValues> {

    public Autons(AutonState neutralState, AutonState startState, AutonPathValues chosenPaths) {
        super(neutralState, startState, chosenPaths);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PICKUPBALL1:
                if(this.getStateFirstRunThrough()){
                    this.createPathPlannerFollower(getChosenPathValues().getPathNameAtIndex(0));
                }
                PathPlannerTrajectory.PathPlannerState pathState = this.getPlannerFollower().getCurrentState();
                Pose2d pose = pathState.poseMeters;
                double fwd_back_position = pose.getX(); //going down field, closer or farther from driver station
                double left_right_position = pose.getY(); //side to side, parallel with driver station wall
                Translation2d targetPosition = pose.getTranslation();
                Rotation2d targetHeading = pose.getRotation();

                break;
            case MOVETOSHOOTBALL1:
                break;
            case SHOOTBALL1:
                break;
            case PICKUPBALL2:
                break;
            case PICKUPBALL3:
                break;
            case SHOOTBALL2:
                break;
            case NEUTRAL:
                break;
            default:
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

enum AutonState implements IState {
    NEUTRAL,
    PICKUPBALL1(Robot.intake, Robot.swerveDrive),
    MOVETOSHOOTBALL1(Robot.swerveDrive),
    SHOOTBALL1(Robot.shooter, Robot.swerveDrive),
    PICKUPBALL2(Robot.intake, Robot.swerveDrive),
    PICKUPBALL3(Robot.intake, Robot.swerveDrive),
    MOVETOSHOOTBALL2(Robot.swerveDrive),
    SHOOTBALL2(Robot.shooter, Robot.swerveDrive);

    List<BaseSubsystem> requiredSubsystems;

    AutonState(BaseSubsystem... subsystems) {
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
}

enum AutonPathValues implements IAutonPathValues {
    FROMPOSITION1;
    List<String> pathNames;
    AutonPathValues(String... pathNames){
        this.pathNames = Arrays.asList(pathNames);
    }
    public String getPathNameAtIndex(int index){
        return pathNames.get(index);
    }
}
