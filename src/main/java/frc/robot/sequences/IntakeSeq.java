package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.IntakeState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.IntakeSeqPhase.*;

enum IntakeSeqPhase implements ISequencePhase {
	NEUTRAL,
	EXTEND(IntakeState.KICKOUT),
	RETRACT(IntakeState.RETRACT);

	SequencePhase phase;

	IntakeSeqPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class IntakeSeq extends BaseSequence<IntakeSeqPhase> {
	public IntakeSeq(IntakeSeqPhase neutralState, IntakeSeqPhase startState) {
		super(neutralState, startState);
		// TODO Auto-generated constructor stub
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
