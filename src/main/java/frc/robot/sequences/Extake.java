package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;

import static frc.robot.sequences.ExtakeState.NEUTRAL;

enum ExtakeState implements ISequenceState {
    NEUTRAL,
    EXTAKE(new IntakeReq(IntakeState.EXTAKE));

    SequenceState state;

    ExtakeState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class Extake extends BaseSequence<ExtakeState> {

    public Extake(ExtakeState neutralState, ExtakeState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case EXTAKE:
                if (!Buttons.Extake.getButton()) {
                    setNextState(NEUTRAL);
                }
                break;
            case NEUTRAL:
                break;
            default:
                break;

        }
        updateState();

    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}

