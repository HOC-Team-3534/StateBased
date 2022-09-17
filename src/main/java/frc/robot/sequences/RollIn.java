package frc.robot.sequences;

import frc.robot.RobotContainer;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;

import static frc.robot.sequences.RollInState.NEUTRAL;

enum RollInState implements ISequenceState {
    NEUTRAL,
    ROLLIN(new IntakeReq(IntakeState.ROLLIN));

    SequenceState state;

    RollInState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class RollIn extends BaseSequence<RollInState> {
    public RollIn(RollInState neutralState, RollInState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case NEUTRAL:
                break;
            case ROLLIN:
                if (!RobotContainer.Buttons.RollIn.getButton()) {
                    setNextState(NEUTRAL);
                }
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

