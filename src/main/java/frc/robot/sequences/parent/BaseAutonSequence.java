package frc.robot.sequences.parent;

import frc.robot.sequences.pathplannerfollower.PathPlannerFollower;

public abstract class BaseAutonSequence<S extends IState> extends BaseSequence<S>{

    PathPlannerFollower pathPlannerFollower;

    public BaseAutonSequence(S neutralState, S startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    void createPathPlannerFollower(String pathName){
        this.pathPlannerFollower = new PathPlannerFollower(pathName);
    }

    PathPlannerFollower getPlannerFollower(){
        return pathPlannerFollower;
    }
}