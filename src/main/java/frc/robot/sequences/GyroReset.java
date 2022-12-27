package frc.robot.sequences;

import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.GyroResetPhase.NEUTRAL;

enum GyroResetPhase implements ISequencePhase {
	NEUTRAL,
	RESET;

	SequencePhase phase;

	GyroResetPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class GyroReset extends BaseSequence<GyroResetPhase> {
	public GyroReset(GyroResetPhase neutralState, GyroResetPhase startState) {
		super(neutralState, startState);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process() {
		switch (getPhase()) {
			case RESET:
				Robot.swerveDrive.resetGyro();
				setNextPhase(NEUTRAL);
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
