package frc.robot.sequences;

public abstract class BaseSequence<S extends IState> implements ISequence<S> {

    S state = null;
    S nextState = null;
    S neutralState = null;
    S startState = null;

    long timeAtStartOfSequence = 0;
    long timeAtStartOfState = 0;

    public BaseSequence(S neutralState, S startState){
        setNeutralState(neutralState);
        setStartState(startState);
        setState(neutralState);
    }

    /**
     * must be called at the beginning of the child class start function
     */
    void init(){
        updateSequenceStartTime();
    }

    void start() {
        if (getState() == getNeutralState()) {
            init();
            setState(getStartState());
        }
    }

    void setState(S state) {
        if(state.requireSubsystems(this)){
            this.state = state;
            updateStateStartTime();
        }
    }

    public S getState() {
        return this.state;
    }

    void setNextState(S state) {
        nextState = state;
    }

    S getNextState() {
        return nextState;
    }

    void setNeutralState(S state){
        neutralState = state;
    }

    S getNeutralState(){
        return neutralState;
    }

    void setStartState(S state) {
		startState = state;
	}

    S getStartState() {
		return startState;
	}

    void updateState() {
        setState(nextState);
    }

    void updateSequenceStartTime(){
        timeAtStartOfSequence = System.currentTimeMillis();
    }

    long getTimeSinceStartOfSequence(){
        return System.currentTimeMillis() - timeAtStartOfSequence;
    }

    void updateStateStartTime(){
        timeAtStartOfState = System.currentTimeMillis();
    }

    long getTimeSinceStartOfState(){
        return System.currentTimeMillis() - timeAtStartOfState;
    }

}
