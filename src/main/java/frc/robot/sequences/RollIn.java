package frc.robot.sequences;

import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakeState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;


import static frc.robot.sequences.RollInPhase.NEUTRAL;

enum RollInPhase implements ISequencePhase {
    NEUTRAL,
    ROLLIN(IntakeState.ROLLIN);

    SequencePhase phase;
    
    RollInPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
    }

}

public class RollIn extends BaseSequence<RollInPhase> {
    public RollIn(RollInPhase neutralState, RollInPhase startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getPhase()) {
            case NEUTRAL:
                break;
            case ROLLIN:
                if (!RobotContainer.Buttons.RollIn.getButton()) {
                    setNextPhase(NEUTRAL);
                }
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

