package frc.robot.subsystems.parent;

import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;

public abstract class BaseSubsystem implements ISubsystem {

    boolean required;
    BaseSequence<? extends IState> sequenceRequiring;
    IState stateRequiring;
    boolean stateChanged;
    boolean stateFirstRunThrough;

    public boolean isRequiredByAnother(BaseSequence<? extends IState> sequence) {
        if(sequenceRequiring == sequence){
            return false;
        }  
        return this.required;
    }

    public boolean require(BaseSequence<? extends IState> sequence, IState state) {
        if (!isRequiredByAnother(sequence)) {
            required = true;
            setSequenceRequiring(sequence);
            setStateRequiring(state);
            return true;
        } else if (sequenceRequiring == sequence) {
            setStateRequiring(state);
            return true;
        } else {
            return false;
        }
    }

    public void process(){
        isStillRequired();
        checkStateChanged();
    }

    private void setSequenceRequiring(BaseSequence<? extends IState> sequence){
        this.sequenceRequiring = sequence;
    }

    private void setStateRequiring(IState state){
        this.stateRequiring = state;
        stateChanged = true;
    }

    private boolean isStillRequired() {
        if (!required) {
            return false;
        } else if (!sequenceRequiring.getState().getRequiredSubsystems().contains(this)) {
            release();
            return false;
        } else {
            return true;
        }
    }

    private void checkStateChanged(){
        stateFirstRunThrough = stateChanged;
        stateChanged = false;
    }

    public boolean getStateFirstRunThrough(){
        return this.stateFirstRunThrough;
    }

    void release() {
        required = false;
        sequenceRequiring = null;
        stateRequiring = null;
    }
    
    public boolean forceRelease() {
        if(this.getSequenceRequiring().abort()){
            release();
            return true;
        }
        return false;
    }

    public BaseSequence<? extends IState> getSequenceRequiring() {
        return sequenceRequiring;
    }

    public String getStateRequiringName() {
        if(stateRequiring == null){
            return "NONE";
        }
        return stateRequiring.getName();
    }

}