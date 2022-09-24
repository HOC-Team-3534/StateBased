package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.IntakeState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;


import static frc.robot.sequences.ExtakePhase.NEUTRAL;

enum ExtakePhase implements ISequencePhase {
    NEUTRAL,
    EXTAKE(IntakeState.EXTAKE);

    SequencePhase phase;
    
    ExtakePhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
    }

}

public class Extake extends BaseSequence<ExtakePhase> {

    public Extake(ExtakePhase neutralState, ExtakePhase startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getPhase()) {
            case EXTAKE:
                if (!Buttons.Extake.getButton()) {
                    setNextPhase(NEUTRAL);
                }
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

