package frc.robot.sequences.parent;


import frc.robot.subsystems.parent.BaseSubsystem;

public abstract class BaseSequence<S extends IState> implements ISequence<S> {

    S state = null;
    S nextState = null;
    S neutralState = null;
    S startState = null;

    long timeAtStartOfSequence = 0;
    long timeAtStartOfState = 0;

    public BaseSequence(S neutralState, S startState) {
        setNeutralState(neutralState);
        setStartState(startState);
        setNextState(neutralState);
        setState(neutralState);
    }

    /**
     * must be called at the beginning of the child class start function
     */
    void init() {
        updateSequenceStartTime();
    }

    public boolean reset(){
        setNextState(getNeutralState());
        return updateState();
    }

    public void start() {
        if (isNeutral()) {
            init();
            setNextState(getStartState());
            setState(getStartState());
        }
    }

    public void start(BaseSubsystem ...subsystems) {
        if (isNeutral()) {
            init();
            for(BaseSubsystem subsystem : subsystems){
                if(!subsystem.forceRelease()){
                    return;
                }
            }
            setNextState(getStartState());
            setState(getStartState());
        }
    }

    public boolean isNeutral(){
        return getState() == getNeutralState();
    }

    boolean setState(S state) {
        if (state.requireSubsystems(this)) {
            this.state = state;
            updateStateStartTime();
            return true;
        }
        return false;
    }

    public S getState() {
        return this.state;
    }

    protected void setNextState(S state) {
        nextState = state;
    }

    S getNextState() {
        return nextState;
    }

    void setNeutralState(S state) {
        neutralState = state;
    }

    public S getNeutralState() {
        return neutralState;
    }

    void setStartState(S state) {
        startState = state;
    }

    S getStartState() {
        return startState;
    }

    protected boolean updateState() {
        if (getState() != getNextState()) {
            return setState(nextState);
        }
        return false;
    }

    void updateSequenceStartTime() {
        timeAtStartOfSequence = System.currentTimeMillis();
    }

    public long getTimeSinceStartOfSequence() {
        return System.currentTimeMillis() - timeAtStartOfSequence;
    }

    void updateStateStartTime() {
        timeAtStartOfState = System.currentTimeMillis();
    }

    public long getTimeSinceStartOfState() {
        return System.currentTimeMillis() - timeAtStartOfState;
    }

}