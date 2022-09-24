package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.statebasedcontroller.sequence.fundamental.BaseSequence;
import frc.statebasedcontroller.sequence.fundamental.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

import static frc.robot.sequences.ExtakePhase.NEUTRAL;

enum ExtakePhase implements ISequencePhase {
    NEUTRAL,
    EXTAKE(new IntakeReq(IntakeState.EXTAKE));

    SequencePhase state;

    ExtakePhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
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

