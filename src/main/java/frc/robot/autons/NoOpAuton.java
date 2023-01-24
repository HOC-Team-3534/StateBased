package frc.robot.autons;

import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

enum NoOpAutonPhase implements ISequencePhase {
	NEUTRAL;

	SequencePhase phase;

	NoOpAutonPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class NoOpAuton extends BaseAutonSequence<NoOpAutonPhase> {
	public NoOpAuton(	NoOpAutonPhase neutralState, NoOpAutonPhase startState,
						BaseDriveSubsystem driveSubsystem) {
		super(neutralState, startState, driveSubsystem);
	}

	@Override
	public void process() {
		switch (getPhase()) {
			case NEUTRAL:
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