package frc.robot.sequences;

import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;


enum DrivePhase implements ISequencePhase {
    NEUTRAL,
    DRIVE(SwerveDriveState.DRIVE);

    SequencePhase phase;
    
    DrivePhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
    }

}

public class Drive extends BaseSequence<DrivePhase> {

    public Drive(DrivePhase neutralState, DrivePhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case DRIVE:
                break;
            case NEUTRAL:
                break;
            default:
                break;

        }
        updatePhase();
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}
