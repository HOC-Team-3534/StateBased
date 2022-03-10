package frc.robot.autons.parent;

import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.IState;

public interface IAutonState extends IState {

    PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence);

    boolean isPathFollowing();

}
