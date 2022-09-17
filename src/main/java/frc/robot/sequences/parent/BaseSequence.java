package frc.robot.sequences.parent;


import frc.robot.subsystems.parent.BaseSubsystem;

public abstract class BaseSequence<SeqS extends ISequenceState> implements ISequence<SeqS> {

    SeqS state = null;
    SeqS nextState = null;
    SeqS neutralState = null;
    SeqS startState = null;

    long timeAtStartOfSequence = 0;
    long timeAtStartOfState = 0;

    boolean stateFirstRunThrough = false;

    public BaseSequence(SeqS neutralState, SeqS startState) {
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

    boolean setState(SeqS state) {
        if (state.getState().requireSubsystems(this)) {
            this.state = state;
            updateStateStartTime();
            stateFirstRunThrough = true;
            return true;
        }
        return false;
    }

    public SeqS getState() {
        return this.state;
    }

    protected void setNextState(SeqS state) {
        nextState = state;
    }

    SeqS getNextState() {
        return nextState;
    }

    void setNeutralState(SeqS state) {
        neutralState = state;
    }

    public SeqS getNeutralState() {
        return neutralState;
    }

    void setStartState(SeqS state) {
        startState = state;
    }

    SeqS getStartState() {
        return startState;
    }

    protected boolean updateState() {
        stateFirstRunThrough = false;
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

    public boolean getStateFirstRunThrough() { return stateFirstRunThrough; }

}