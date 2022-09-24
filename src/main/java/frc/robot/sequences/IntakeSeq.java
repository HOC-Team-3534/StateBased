package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.requirements.IntakeReq;

import static frc.robot.sequences.IntakeSeqPhase.*;

import frc.BaseSequence;
import frc.ISequencePhase;
import frc.SequencePhase;
import frc.SubsystemRequirement;

enum IntakeSeqPhase implements ISequencePhase {
    NEUTRAL,
    EXTEND(new IntakeReq(IntakeState.KICKOUT)),
    RETRACT(new IntakeReq(IntakeState.RETRACT));

    SequencePhase state;

    IntakeSeqPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class IntakeSeq extends BaseSequence<IntakeSeqPhase> {

    public IntakeSeq(IntakeSeqPhase neutralState, IntakeSeqPhase startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getPhase()) {
            case EXTEND:
                if (!Buttons.Intake.getButton()) {
                    setNextPhase(RETRACT);
                }
                break;
            case RETRACT:
                if (Buttons.Intake.getButton()) {
                    setNextPhase(EXTEND);
                }
                if (getTimeSinceStartOfPhase() > 700) {
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

