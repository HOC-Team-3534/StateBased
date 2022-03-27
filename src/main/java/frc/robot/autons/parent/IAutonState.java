package frc.robot.autons.parent;

import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.ISequenceState;

public interface IAutonState extends ISequenceState {

    PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence);

    static PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence, int pathIndex){
        if(pathIndex >= 0 && pathIndex < sequence.getPaths().size()){
            return sequence.getPaths().get(pathIndex);
        }
        System.out.println("ERROR: Tried to get path for state that doesn't have a valid path");
        return null;
    }

}
