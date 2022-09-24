package frc.robot.sequences;

import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.statebasedcontroller.sequence.fundamental.BaseSequence;
import frc.statebasedcontroller.sequence.fundamental.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

import static frc.robot.sequences.RollInPhase.NEUTRAL;

enum RollInPhase implements ISequencePhase {
    NEUTRAL,
    ROLLIN(new IntakeReq(IntakeState.ROLLIN));

    SequencePhase state;

    RollInPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
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

