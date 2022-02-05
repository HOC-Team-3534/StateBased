package frc.robot.subsystems.parent;

import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;

public abstract class BaseSubsystem implements ISubsystem {

    boolean required;
    BaseSequence<? extends IState> sequenceRequiring;
    IState stateRequiring;

    public boolean isRequiredByAnother(BaseSequence<? extends IState> sequence) {
        if(sequenceRequiring == sequence){
            return false;
        }  
        return this.required;
    }

    public boolean require(BaseSequence<? extends IState> sequence, IState state) {
        if (!isRequiredByAnother(sequence)) {
            required = true;
            sequenceRequiring = sequence;
            stateRequiring = state;
            return true;
        } else if (sequenceRequiring == sequence) {
            stateRequiring = state;
            return true;
        } else {
            return false;
        }
    }

    public boolean isStillRequired() {
        if (!required) {
            return false;
        } else if (!sequenceRequiring.getState().getRequiredSubsystems().contains(this)) {
            release();
            return false;
        } else {
            return true;
        }
    }

    void release() {
        required = false;
        sequenceRequiring = null;
        stateRequiring = null;
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
