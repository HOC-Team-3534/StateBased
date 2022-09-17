package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;

import static frc.robot.sequences.IntakeSeqState.*;

enum IntakeSeqState implements ISequenceState {
    NEUTRAL,
    EXTEND(new IntakeReq(IntakeState.KICKOUT)),
    RETRACT(new IntakeReq(IntakeState.RETRACT));

    SequenceState state;

    IntakeSeqState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class IntakeSeq extends BaseSequence<IntakeSeqState> {

    public IntakeSeq(IntakeSeqState neutralState, IntakeSeqState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case EXTEND:
                if (!Buttons.Intake.getButton()) {
                    setNextState(RETRACT);
                }
                break;
            case RETRACT:
                if (Buttons.Intake.getButton()) {
                    setNextState(EXTEND);
                }
                if (getTimeSinceStartOfState() > 700) {
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

