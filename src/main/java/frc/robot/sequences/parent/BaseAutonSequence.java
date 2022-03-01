package frc.robot.sequences.parent;

import frc.robot.sequences.pathplannerfollower.PathPlannerFollower;

import java.util.List;

public abstract class BaseAutonSequence<S extends IState, A extends IAutonPathValues> extends BaseSequence<S>{

    PathPlannerFollower pathPlannerFollower;
    A chosenPathValues;

    public BaseAutonSequence(S neutralState, S startState, A chosenPathValues) {
        super(neutralState, startState);
        setChosenPathValues(chosenPathValues);
    }

    void createPathPlannerFollower(String pathName){
        this.pathPlannerFollower = new PathPlannerFollower(pathName);
    }

    PathPlannerFollower getPlannerFollower(){
        return pathPlannerFollower;
    }

    public A getChosenPathValues() {
        return chosenPathValues;
    }

    void setChosenPathValues(A chosenPathValues) {
        this.chosenPathValues = chosenPathValues;
    }
}