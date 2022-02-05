package frc.robot.sequences.parent;

public abstract class BaseAutonSequence<S extends IState> extends BaseSequence<S>{

    public BaseAutonSequence(S neutralState, S startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

}